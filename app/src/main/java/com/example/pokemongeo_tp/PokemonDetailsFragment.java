package com.example.pokemongeo_tp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pokemongeo_tp.databinding.PokemonDetailsFragmentBinding;

public class PokemonDetailsFragment extends Fragment {
    private final PokemonViewModel pokemonViewModel;
    private final Pokemon pokemon;

    public PokemonDetailsFragment(Pokemon pokemon) {
        pokemonViewModel = new PokemonViewModel();
        pokemonViewModel.setPokemon(pokemon);
        this.pokemon = pokemon;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PokemonDetailsFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.pokemon_details_fragment, container, false);
        binding.setPokemonViewModel(pokemonViewModel);
        if (pokemon.getType2() == null) {

            binding.type2Text.setVisibility(View.GONE);
        }


        return binding.getRoot();
    }

}
