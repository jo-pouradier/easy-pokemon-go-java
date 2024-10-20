package com.example.pokemongeo_tp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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

import kotlin.jvm.internal.Lambda;


public class MapFragment extends Fragment {
    private static final int MAX_POKEMON_DISTANCE = 200;
    private final List<Marker> pokemonMarkers;
    LocationListener myLocationListener;
    private MapFragmentBinding binding;
    /**
     * Needed when we recreate the view
     */
    private GeoPoint playerLocation;
    private Marker playerMarker;
    private IMapController mapController;

    public MapFragment() {
        pokemonMarkers = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
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
            initPlayerMarker();
        }
//        playerLocation = newLocation;
//        playerMarker.setPosition(playerLocation);
        changePositionSmoothly(playerMarker, playerLocation);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initPlayerMarker() {
        playerMarker = new Marker(binding.mapView);
        playerMarker.setTitle("Player point");
        playerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        playerMarker.setIcon(getResources().getDrawable(R.drawable.acier));
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
        mapController.setZoom(20);
        if (playerLocation == null) playerLocation = new GeoPoint(45.763420, 4.834277);
        binding.mapView.setBuiltInZoomControls(true);
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
        List<Marker> toRemove = new ArrayList<>();
        for (Marker marker : pokemonMarkers) {
            double distance = playerLocation.distanceToAsDouble(marker.getPosition());
            if (Math.abs(distance) >= MAX_POKEMON_DISTANCE) {
                toRemove.add(marker);
                binding.mapView.getOverlays().remove(marker);
            }
        }
        pokemonMarkers.removeAll(toRemove);

        // if more than 5 pokemon, don't spawn
        int MAX_POKEMON_MARKERS = 5;
        if (pokemonMarkers.size() >= MAX_POKEMON_MARKERS) {
            return;
        }
        // create pokemon around the player in thread
        int numPokemonToSpawn = MAX_POKEMON_MARKERS - pokemonMarkers.size();
        RequestPromise<Integer, List<Marker>> promise = new RequestPromise<>(
                new ThreadEventListener<List<Marker>>() {
                    @Override
                    public void OnEventInThread(List<Marker> data) {
                        for (Marker marker : data) {
                            binding.mapView.getOverlays().add(marker);
                            pokemonMarkers.add(marker);
                        }
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                        Log.e("ERROR", "error while creating random pokemon: " + error);
                    }
                },
                (Integer numberPokemon) -> {
                    Database db = Database.getInstance(getContext());
                    List<Marker> markers = new ArrayList<>();
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
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        marker.setIcon(getResources().getDrawable(getResources().getIdentifier(pokemon.image,
                                "drawable",
                                binding.getRoot().getContext().getPackageName())));

                        markers.add(marker);
                    }
                    return markers;
                },
                numPokemonToSpawn
        );
        RequestThread instance = RequestThread.getInstance();
        instance.addRequest(promise);
    }

    public void changePositionSmoothly(Marker marker, GeoPoint newPosition) {
//        GeoPoint positionBefore = marker.getPosition();

        ThreadEventListener<GeoPoint> listener = new ThreadEventListener<GeoPoint>() {
            @Override
            public void OnEventInThread(GeoPoint data) {
                Log.d("DEBUG", "moving marker to: " + data);
                marker.setPosition(data);
                mapController.setCenter(data);
            }

            @Override
            public void OnEventInThreadReject(String error) {
                Log.e("ERROR", "error while moving marker: " + error);
            }
        };

        RequestPromise<Marker, GeoPoint> promise = new RequestPromise<>(
                listener,
                (Marker marker2) -> {
                    GeoPoint positionBefore = marker.getPosition();
                    double distance = positionBefore.distanceToAsDouble(newPosition) ;
                    double time = 1000; // 1 second
                    double speed = distance / time;

                    double dx = ((newPosition.getLatitude() - positionBefore.getLatitude()) / distance) * speed;
                    double dy = ((newPosition.getLongitude() - positionBefore.getLongitude()) / distance) * speed;

                    while (positionBefore.distanceToAsDouble(newPosition) > speed) {
                        positionBefore = new GeoPoint(positionBefore.getLatitude() + dx, positionBefore.getLongitude() + dy);
//                        marker.setPosition(positionBefore);
                        listener.OnEventInThread(positionBefore);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Log.e("ERROR", "error while moving marker: " + e.getMessage());
                        }
                    }
                    return null;
                },
                marker
        );
        RequestThread instance = RequestThread.getInstance();
        instance.addRequest(promise);

    }


    private double calculateLatitudeDifference(double lat1, double lat2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371000 * c;
    }

}