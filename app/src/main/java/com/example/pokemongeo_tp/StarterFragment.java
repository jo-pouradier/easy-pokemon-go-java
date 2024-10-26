package com.example.pokemongeo_tp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.pokemongeo_tp.databinding.PokedexFragmentBinding;
import com.example.pokemongeo_tp.databinding.StarterFragmentBinding;
import com.example.pokemongeo_tp.entities.PokemonEntity;

import java.util.ArrayList;
import java.util.List;

public class StarterFragment extends Fragment {


    List<Pokemon> pokemonList = new ArrayList<>();
    PokemonListAdapter adapter;
    OnSelectStarterListener selectStarterListner;

    public void setPokemonList(List<PokemonEntity> pokemonList) {
        for (PokemonEntity poke : pokemonList) {
            this.pokemonList.add(new Pokemon(poke));
        }
    }

    public void setSelectStarterListener(OnSelectStarterListener listner) {
        this.selectStarterListner = listner;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StarterFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.starter_fragment, container, false);
        binding.pokemonStarterList.setLayoutManager(new GridLayoutManager(
                binding.getRoot().getContext(), 3));

        adapter = new PokemonListAdapter(pokemonList);
        binding.pokemonStarterList.setAdapter(adapter);
        adapter.setOnSelectStarterListener(selectStarterListner);
        return binding.getRoot();
    }
}



