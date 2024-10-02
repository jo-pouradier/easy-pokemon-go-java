package com.example.pokemongeo_tp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.pokemongeo_tp.databinding.PokemonItemBinding;
public class PokemonListAdapter extends
        RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    List<Pokemon> pokemonList;

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
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.viewModel.setPokemon(pokemon);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PokemonItemBinding binding;
        private PokemonViewModel viewModel = new PokemonViewModel();

        ViewHolder(PokemonItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setPokemonViewModel(viewModel);
        }
    }
}