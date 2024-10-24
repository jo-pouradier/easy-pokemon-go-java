package com.example.pokemongeo_tp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DiscoveryFragment extends Fragment {

    private DiscoveryViewModel discoveryViewModel;
    /**
     * Listener to return to the map after action on pokemon
     */
    private onPokemonDiscoveryEndListener listener;

    public static DiscoveryFragment newInstance() {
        return new DiscoveryFragment();
    }

    public DiscoveryFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        discoveryViewModel = new ViewModelProvider(this).get(DiscoveryViewModel.class);
        // TODO: Use the ViewModel
    }

    public void setPokemon(Pokemon pokemon){
        discoveryViewModel.setPokemon(pokemon);
    }

    public void setListener(onPokemonDiscoveryEndListener listener){
        this.listener = listener;
    }

}