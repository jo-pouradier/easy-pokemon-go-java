package com.example.pokemongeo_tp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemongeo_tp.databinding.PokemonDetailsFragmentBinding;
import com.example.pokemongeo_tp.databinding.PokemonItemBinding;

public class PokemonDetailsFragment extends Fragment {
    private PokemonViewModel pokemonViewModel;
    private Pokemon pokemon;

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

        return binding.getRoot();
    }

}
