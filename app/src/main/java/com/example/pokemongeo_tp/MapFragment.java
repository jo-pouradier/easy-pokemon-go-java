package com.example.pokemongeo_tp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;

import com.example.pokemongeo_tp.databinding.MapFragmentBinding;
import com.example.pokemongeo_tp.databinding.PokemonDetailsFragmentBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    private MapFragmentBinding binding;
    private Location myLocation;

    public MapFragment() {
        // Required empty public constructor
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
    LocationListener myLocationListener;
    public void setOnLocationChanged(LocationListener listener) {
        this.myLocationListener = listener;
    }

   public void setLocation(Location newLocation){
        myLocation = newLocation;
        if(binding != null) {
            System.out.println("Latitude: " + newLocation.getLatitude() + " Longitude: " + newLocation.getLongitude());
            IMapController mapController = binding.mapView.getController();
            mapController.setZoom(21);
            GeoPoint startPoint = new GeoPoint(newLocation.getLatitude(), newLocation.getLongitude());
            mapController.setCenter(startPoint);
            Marker marker = new Marker(binding.mapView);
            marker.setPosition(new GeoPoint(newLocation.getLatitude(), newLocation.getLongitude()));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            marker.setIcon(getResources().getDrawable(R.drawable.acier));
            marker.setTitle("Start point");
            binding.mapView.getOverlays().add(marker);
        }
   }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.map_fragment, container, false);
        //Récupération du context dans le cas d'un Fragment
        Context context = binding.getRoot().getContext();
        //dans les deux cas
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        //source des données cartographique
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = binding.mapView.getController();
        mapController.setZoom(21);
//        GeoPoint startPoint = new GeoPoint(45.763420, 4.834277);
//        mapController.setCenter(startPoint);
        binding.mapView.setBuiltInZoomControls(true);
        binding.mapView.setMultiTouchControls(true);
        setLocation(myLocation);

        return binding.getRoot();
    }
}