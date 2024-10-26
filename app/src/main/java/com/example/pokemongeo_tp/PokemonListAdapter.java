package com.example.pokemongeo_tp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemongeo_tp.databinding.PokemonItemBinding;

import java.util.List;

public class PokemonListAdapter extends
        RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    List<Pokemon> pokemonList;

    private OnClickOnPokemonListener Clicklistener;
    private OnSelectStarterListener selectStarterListener;
    public void setOnClickOnPokemonListener(OnClickOnPokemonListener listener) {
        this.Clicklistener = listener;

    }
    public void setOnSelectStarterListener(OnSelectStarterListener listener) {
        this.selectStarterListener = listener;
    }

    public PokemonListAdapter(List<Pokemon> pokemonList) {
        assert pokemonList != null;
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PokemonItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.pokemon_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        viewHolder.setOnClickOnPokemonListener(Clicklistener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);

        if (pokemon.isDiscovered()) {
            holder.viewModel.setPokemon(pokemon);
        } else {
            holder.viewModel.setPokemon(new Pokemon(pokemon.getOrder()));
        }
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final PokemonItemBinding binding;
        private final PokemonViewModel viewModel = new PokemonViewModel();
        private OnClickOnPokemonListener listener;
        private OnSelectStarterListener selectStarterListener;

        public void setOnClickOnPokemonListener(OnClickOnPokemonListener listener) {
            this.listener = listener;
        }
        public void setOnSelectStarterListener(OnSelectStarterListener listener) {
            this.selectStarterListener = listener;
        }
        ViewHolder(PokemonItemBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickOnPokemon(viewModel.getPokemon());
                }
            });
            this.binding = binding;
            this.binding.setPokemonViewModel(viewModel);

        }
    }
}