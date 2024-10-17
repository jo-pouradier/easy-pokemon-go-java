package com.example.pokemongeo_tp;

import android.content.Context;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.os.CancellationSignal;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pokemongeo_tp.database.Initalization;
import com.example.pokemongeo_tp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MapFragment mapfragment;
    private Location myLocation;
    OnClickOnPokemonListener listener = new OnClickOnPokemonListener() {
        @Override
        public void onClickOnPokemon(Pokemon Pokemon) {
            showPokemonDetails(Pokemon);
        }
    };

    LocationListener myLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location newLocation) {
            System.out.println("Latitude: " + newLocation.getLatitude() + " Longitude: " + newLocation.getLongitude());
            myLocation = newLocation;
            if (mapfragment == null){
                return;
            }
            mapfragment.setLocation(newLocation);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("status changed");
        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public void setOnLocationChanged(LocationListener listener) {
        System.out.println("set loc");
        this.myLocationListener = listener;
//        listener.onLocationChanged(new Location("gps"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull int[] grantResults) {
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
                    }).create().show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init database
        Initalization.InitPokemon(this);
        Initalization.InitObject(this);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationBarListener(getSupportFragmentManager()));
        binding.bottomNavigation.setSelectedItemId(R.id.pokedex);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                if (item.getItemId() == R.id.pokedex) {
                    fragment = new PokedexFragment(); // Replace with your fragment class
                } else if (item.getItemId() == R.id.home) {
                    fragment = new HomeFragment(); // Replace with your fragment class
                } else if (item.getItemId() == R.id.map){
                    System.out.println("map");
                    fragment = new MapFragment();
                    mapfragment = (MapFragment) fragment;
                    if (myLocation != null)
                        mapfragment.setLocation(myLocation);
//                    mapfragment.setOnLocationChanged(myLocationListener);
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment) // Replace with your fragment container's ID
                            .commit();
                    return true;
                }

                return false;
            }
        });
        showStartup();
    }

    public void showStartup() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    1);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Handle the case where the user cancels
                        })
                        .create()
                        .show();
            }
        } else {
            //We already have permission do something with it
            System.out.println("yes permission 232");
            createLocationManager();
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PokedexFragment fragment = new PokedexFragment();
        fragment.setOnClickOnPokemonListener(listener);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void showPokemonDetails(Pokemon pokemon) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PokemonDetailsFragment fragment = new PokemonDetailsFragment(pokemon);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void createLocationManager() {
        //Get System Service
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Request Location updates 120ms 100m
        System.out.println("no permission");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120L, 100F,  myLocationListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120L, 100F,  myLocationListener);
            //remove updates
//            manager.removeUpdates( myLocationListener);
            //manager.getCurrentLocation(LocationManager.GPS_PROVIDER, (CancellationSignal) null, (Executor) this, myLocationListener);
            return;
        }
        System.out.println("yes permission");
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120L, 100F,  myLocationListener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120L, 100F,  myLocationListener);
        //remove updates
//        manager.removeUpdates( myLocationListener);
        setOnLocationChanged(myLocationListener);
    }
}