package com.example.pokemongeo_tp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pokemongeo_tp.databinding.FragmentDiscoveryBinding;

public class DiscoveryFragment extends Fragment {

    private final DiscoveryViewModel discoveryViewModel = new DiscoveryViewModel();
    /**
     * Listener to return to the map after action on pokemon
     */
    private onPokemonDiscoveryEndListener listener;

    public DiscoveryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        discoveryViewModel.setOnClickListener(listener);
        FragmentDiscoveryBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_discovery, container, false);
        binding.setDiscoveryViewModel(discoveryViewModel);

        return binding.getRoot();
    }

    public void setPokemon(Pokemon pokemon) {
        discoveryViewModel.setPokemon(pokemon);
    }

    public void setListener(onPokemonDiscoveryEndListener listener) {
        this.listener = listener;
    }


}