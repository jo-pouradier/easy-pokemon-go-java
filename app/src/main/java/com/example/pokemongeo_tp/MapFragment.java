package com.example.pokemongeo_tp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pokemongeo_tp.databinding.MapFragmentBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;


public class MapFragment extends Fragment {
    LocationListener myLocationListener;
    private MapFragmentBinding binding;
    /** Needed when we recreate the view */
    private GeoPoint playerLocation;
    private Marker playerMarker;
    private IMapController mapController;

    public MapFragment() {
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
        if (playerMarker == null) {
            initPlayerMarker();
        }
        playerLocation = newLocation;
        mapController.setCenter(playerLocation);
        playerMarker.setPosition(playerLocation);
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
}