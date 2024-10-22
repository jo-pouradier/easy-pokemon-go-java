package com.example.pokemongeo_tp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.pokemongeo_tp.database.Database;
import com.example.pokemongeo_tp.databinding.MapFragmentBinding;
import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.threading.RequestPromise;
import com.example.pokemongeo_tp.threading.RequestThread;
import com.example.pokemongeo_tp.threading.ThreadEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private static final int MAX_POKEMON_DISTANCE = 200;
    private final List<PokemonMarkerData> pokemonMarkerData;
    LocationListener myLocationListener;
    private MapFragmentBinding binding;
    /**
     * Needed when we recreate the view
     */
    private GeoPoint playerLocation;
    private Marker playerMarker;
    private IMapController mapController;

    public MapFragment() {
        pokemonMarkerData = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
        // add markers
        initPlayerMarker(playerLocation);
        for (PokemonMarkerData dataMarker : pokemonMarkerData) {
            // recreate marker from data
            Marker newMarker = new Marker(binding.mapView);
            newMarker.setPosition(dataMarker.position);
            newMarker.setTitle(dataMarker.name);
            newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER); // center-center because we only have "round" img
            newMarker.setIcon(dataMarker.icon);
            dataMarker.marker = newMarker;
            binding.mapView.getOverlays().add(newMarker);
        }
        mapController.setCenter(playerLocation);
        mapController.setZoom(20.0);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    public void setOnLocationChanged(LocationListener listener) {
        this.myLocationListener = listener;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setLocation(GeoPoint newLocation) {
        if (binding == null) {
            return;
        }
        if (newLocation == null) {
            return;
        }
        if (playerMarker == null) {
            initPlayerMarker(newLocation);
        }
        playerLocation = newLocation;

        if (playerMarker.getPosition().equals(newLocation)) {
            return;
        }
        changePositionSmoothly(playerMarker, playerLocation);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initPlayerMarker(GeoPoint newLocation) {
        playerMarker = new Marker(binding.mapView);
        playerMarker.setPosition(newLocation);
        playerMarker.setTitle("Player point");
        playerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        playerMarker.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.acier));
        binding.mapView.getOverlays().add(playerMarker);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false);
        //Récupération du context dans le cas d'un Fragment
        Context context = binding.getRoot().getContext();
        //dans les deux cas
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        //source des données cartographique
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapController = binding.mapView.getController();
        mapController.setZoom(20.0);
        if (playerLocation == null) playerLocation = new GeoPoint(45.763420, 4.834277);
        binding.mapView.setMultiTouchControls(true);
        setLocation(playerLocation);

        return binding.getRoot();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void spawnPokemon() {
        // if no player location, don't spawn
        if (playerLocation == null) {
            return;
        }
        // delete pokemon that are too far away
        List<PokemonMarkerData> toRemove = new ArrayList<>();
        for (PokemonMarkerData dataMarker : pokemonMarkerData) {
            double distance = playerLocation.distanceToAsDouble(dataMarker.position);
            if (Math.abs(distance) >= MAX_POKEMON_DISTANCE) {
                toRemove.add(dataMarker);
                binding.mapView.getOverlays().remove(dataMarker.marker);
            }
        }
        pokemonMarkerData.removeAll(toRemove);

        // if more than 5 pokemon, don't spawn
        int MAX_POKEMON_MARKERS = 5;
        if (pokemonMarkerData.size() >= MAX_POKEMON_MARKERS) {
            return;
        }
        // create pokemon around the player in thread
        int numPokemonToSpawn = MAX_POKEMON_MARKERS - pokemonMarkerData.size();
        RequestPromise<Integer, List<PokemonMarkerData>> promise = new RequestPromise<>(
                new ThreadEventListener<List<PokemonMarkerData>>() {
                    @Override
                    public void OnEventInThread(List<PokemonMarkerData> data) {
                        for (PokemonMarkerData dataMarker : data) {
                            binding.mapView.getOverlays().add(dataMarker.marker);
                            pokemonMarkerData.add(dataMarker);
                        }
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                        Log.e("ERROR", "error while creating random pokemon: " + error);
                    }
                },
                (Integer numberPokemon) -> {
                    Database db = Database.getInstance(getContext());
                    List<PokemonMarkerData> threadDataPokemonMarker = new ArrayList<>();
                    for (int i = 0; i < numberPokemon; i++) {
                        // create a random position in a circle of 100 meters of the player
                        double angleDegree = Math.random() * 360; // angle in degrees
                        double distance = Math.random() * MAX_POKEMON_DISTANCE; // distance in meters

                        GeoPoint position = playerLocation.destinationPoint(distance, angleDegree);
                        Marker marker = new Marker(binding.mapView);
                        marker.setPosition(position);
                        // get random pokemon in the database

                        PokemonEntity pokemon = db.pokemonDao().getRandomPokemon();
                        marker.setTitle(pokemon.name);
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                        int iconID;
                        try {
                            iconID = R.drawable.class.getDeclaredField(pokemon.image).getInt(null);
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            Log.e("ERROR", "error while getting image: " + e.getMessage());
                            iconID = R.drawable.normal;
                        }
                        Drawable icon = ContextCompat.getDrawable(requireContext(), iconID);
                        marker.setIcon(icon);

                        // save data for onResume
                        threadDataPokemonMarker.add(new PokemonMarkerData(
                                pokemon.name,
                                pokemon.image,
                                position,
                                icon,
                                marker
                        ));
                    }
                    return threadDataPokemonMarker;
                },
                numPokemonToSpawn
        );
        RequestThread instance = RequestThread.getInstance();
        if (instance.isNotRunning()) instance.start();
        instance.addRequest(promise);
    }

    public void changePositionSmoothly(Marker marker, GeoPoint newPosition) {
        ThreadEventListener<GeoPoint> listener = new ThreadEventListener<GeoPoint>() {
            @Override
            public void OnEventInThread(GeoPoint data) {
                if (data == null) {
                    return;
                }
                Log.d("DEBUG", "moving marker to: " + data);
                // ? why do I need this ??
                requireActivity().runOnUiThread(() -> {
                    playerMarker.setPosition(data);
                    mapController.setCenter(data);
                });
            }

            @Override
            public void OnEventInThreadReject(String error) {
                Log.e("ERROR", "error while moving marker: " + error);
            }
        };

        RequestPromise<Void, GeoPoint> promise = new RequestPromise<>(
                listener,
                (Void) -> {
                    GeoPoint positionBefore = marker.getPosition();
                    double latitude = positionBefore.getLatitude();
                    double longitude = positionBefore.getLongitude();
                    double numberOfSteps = 10;
                    int time = 1000; // 1 second in millis
                    long dt = (long) (time / numberOfSteps);

                    double dLatitude = (newPosition.getLatitude() - latitude) / 10;
                    double dLongitude = (newPosition.getLongitude() - longitude) / 10;

                    GeoPoint rollingPosition = new GeoPoint(positionBefore);
                    for (int i = 0; i < numberOfSteps; i++) {
                        rollingPosition = new GeoPoint(rollingPosition.getLatitude() + dLatitude,
                                rollingPosition.getLongitude() + dLongitude);
                        listener.OnEventInThread(new GeoPoint(rollingPosition));
                        try {
                            Thread.sleep(dt);
                        } catch (InterruptedException e) {
                            Log.i("ERROR", "error while moving marker: " + e.getMessage());
                        }
                    }
                    listener.OnEventInThread(playerLocation);
                    // TODO: add listener to launch the "collision" with other pokemon only at the end of the movement
                    launchPokemonCollisionDetection();
                    return null;
                },
                null
        );
        RequestThread instance = RequestThread.getInstance();
        if (instance.isNotRunning()) instance.start();
        instance.addRequest(promise);

    }

    private void launchPokemonCollisionDetection() {
        ThreadEventListener<List<PokemonMarkerData>> listener = new ThreadEventListener<List<PokemonMarkerData>>() {
            @Override
            public void OnEventInThread(List<PokemonMarkerData> data) {
                // refresh map markers
                // TODO: Delete or Battle ?
                requireActivity().runOnUiThread(() -> {
                    for (PokemonMarkerData pokeM : data) {
                        binding.mapView.getOverlays().remove(pokeM.marker);
                        pokemonMarkerData.remove(pokeM);
                    }
                    // refresh map
                    binding.mapView.invalidate();
                });

            }

            @Override
            public void OnEventInThreadReject(String error) {
                Log.e("ERROR", "error while detecting collision: " + error);
            }
        };
        RequestPromise<List<PokemonMarkerData>, List<PokemonMarkerData>> promise = new RequestPromise<>(
                listener, // TODO: at the end refresh map markers?
                (List<PokemonMarkerData> pmd) -> {
                    Database db = Database.getInstance(requireContext());
                    GeoPoint playerCp = new GeoPoint(playerLocation);
                    ArrayList<PokemonMarkerData> collisions = new ArrayList<>(pmd.size());

                    for (PokemonMarkerData pokeM : pmd) {
                        if (playerCp.distanceToAsDouble(pokeM.position) < 10) { // TODO: change distance to Constant
                            PokemonEntity pokemon = db.pokemonDao().getPokemonByName(pokeM.name);
                            pokemon.discovered = true;
                            Log.i("INFO", "Collision with " + pokemon.toString());
                            try {
                                db.pokemonDao().update(pokemon);
                                collisions.add(pokeM);
                            } catch (Exception e) {
                                Log.e("ERROR", "error updating pokemon after collision", e);
                            }
                        }
                    }
                    return collisions;
                },
                pokemonMarkerData
        );
        RequestThread instance = RequestThread.getInstance();
        if (instance.isNotRunning()) instance.start();
        instance.addRequest(promise);

    }

    private static class PokemonMarkerData {
        public String name;
        public String image;
        public GeoPoint position;
        public Drawable icon;
        public Marker marker;

        public PokemonMarkerData(String name, String image, GeoPoint position, Drawable icon, Marker marker) {
            this.name = name;
            this.image = image;
            this.position = position;
            this.icon = icon;
            this.marker = marker;
        }
    }
}