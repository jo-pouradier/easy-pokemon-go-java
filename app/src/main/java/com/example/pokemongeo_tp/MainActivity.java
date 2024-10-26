package com.example.pokemongeo_tp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pokemongeo_tp.database.Database;
import com.example.pokemongeo_tp.database.Initialization;
import com.example.pokemongeo_tp.databinding.ActivityMainBinding;
import com.example.pokemongeo_tp.entities.OwnPokemonEntity;
import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.threading.RequestPromise;
import com.example.pokemongeo_tp.threading.RequestThread;
import com.example.pokemongeo_tp.threading.ThreadEventListener;
import com.google.android.material.navigation.NavigationBarView;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MapFragment mapfragment;
    private GeoPoint playerLocation;
    private ActivityMainBinding binding;
    private final LocationListener myLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location newLocation) {
            Log.i("INFO", "Latitude: " + newLocation.getLatitude() + " Longitude: " + newLocation.getLongitude());
            if (playerLocation == null) {
                playerLocation = new GeoPoint(newLocation.getLatitude(), newLocation.getLongitude());
            }
            playerLocation.setCoords(newLocation.getLatitude(), newLocation.getLongitude());
            if (mapfragment == null) {
                return;
            }
            mapfragment.setLocation(playerLocation);
            // make spawn pokemon
            mapfragment.spawnPokemon();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }
    };
    private final NavigationBarView.OnItemSelectedListener navigationBarListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            if (item.getItemId() == R.id.pokedex) {
                fragment = new PokedexFragment(); // Replace with your fragment class
            } else if (item.getItemId() == R.id.home) {
                fragment = new HomeFragment(); // Replace with your fragment class
            } else if (item.getItemId() == R.id.inventory) {
                fragment = new InventoryFragment(); // Replace with your fragment class
            } else if (item.getItemId() == R.id.map) {
                if (mapfragment == null) {
                    mapfragment = new MapFragment();
                    mapfragment.setOnLocationChanged(myLocationListener);
                    mapfragment.setLocation(playerLocation);
                }
                fragment = mapfragment;
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment) // Replace with your fragment container's ID
                        .commit();
                return true;
            }

            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //We have permissions[0] granted do something with it make sure again we have all the permissions
            createLocationManager();
        } else {
            //display an error message?
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to hack you and know where you live but don't worry game is fun =).")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // code to go in to the settings
                    }).setNegativeButton("Cancel", (dialog, which) -> {
                        // Handle the case where the user cancels
                    }).create()
                    .show();
        }
    }

    public OnClickOnPokemonListener selectStarterListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init database
        Initialization.InitPokemon(this);
        Initialization.InitObject(this);
        Initialization.InitPokemonStats(this);

        // setup bottom navigation bar
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.binding.bottomNavigation.setOnItemSelectedListener(navigationBarListener);
        // TODO Check if we have pokemon starter in inventory
        RequestPromise<Context, List<PokemonEntity>> promise = new RequestPromise<>(
                new ThreadEventListener<List<PokemonEntity>>() {
                    @Override
                    public void OnEventInThread(List<PokemonEntity> data) {
                        Log.i("Starter", "in activity starter " + data.isEmpty());
                        if (data.isEmpty()) {
                            Log.i("Starter", "in activity  starter");
                            binding.bottomNavigation.setSelectedItemId(R.id.pokedex);

                        } else {
                            StarterFragment fragment = new StarterFragment();
                            fragment.setPokemonList(data);
                            Log.i("Starter", data.toString());
                            Log.i("Starter", "in activity no starter");
                            fragment.setSelectStarterListener(selectStarterListener);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment) // Replace with your fragment container's ID
                                    .commit();
                        }
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                        Log.e("ERROR", "while creating pokemon from PokemonEntity. " + error);
                    }
                },
                (Context context) ->
                {
                    Database db = Database.getInstance(context);
                    if (db.ownPokemonDao().getAll().isEmpty()) {
                        List<PokemonEntity> data = db.pokemonDao().getStarterPokemon();
                        Log.i("Starter", "in resquest starter " + data);
                        return data;
                    }
                    return new ArrayList<PokemonEntity>();
                },
                this
        );
        RequestThread instance = RequestThread.getInstance();
        instance.addRequest(promise);
//        this.binding.bottomNavigation.setSelectedItemId(R.id.pokedex);

        // check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
                //display explanation message why you need the permission
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("This permission is needed to hack you and know where you live but don't worry game is fun =).")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Request the permission again
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Handle the case where the user cancels
                        })
                        .create()
                        .show();
            }
        } else {
            //We already have permission do something with it
            Log.i("INFO", "yes permission 232");
            createLocationManager();
        }

        Context context = this.getApplicationContext();
        selectStarterListener = new OnClickOnPokemonListener() {
            @Override
            public void onClickOnPokemon(Pokemon pokemon) {
                RequestPromise<Context, Void> promise = new RequestPromise<>(
                        new ThreadEventListener<Void>() {
                            @Override
                            public void OnEventInThread(Void data) {
                                // refresh view
                                binding.bottomNavigation.setSelectedItemId(R.id.pokedex);
                            }

                            @Override
                            public void OnEventInThreadReject(String error) {
                                Log.e("ERROR", "while creating pokemon from PokemonEntity. " + error);
                            }
                        },
                        (Context context) -> {
                            Database db = Database.getInstance(context);
                            PokemonEntity pokeEntity = db.pokemonDao().getPokemonByName(pokemon.getName());
                            OwnPokemonEntity pokeOwn = new OwnPokemonEntity(pokeEntity);
                            db.ownPokemonDao().insert(pokeOwn);
                            return null;
                        }, context

                );
                RequestThread instance = RequestThread.getInstance();
                instance.addRequest(promise);
            }
        }


        ;
    }

    public void createLocationManager() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120, 0.5f, myLocationListener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120, 0.5f, myLocationListener);
    }
}