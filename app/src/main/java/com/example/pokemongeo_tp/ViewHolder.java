package com.example.pokemongeo_tp;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemongeo_tp.databinding.PokemonItemBinding;

public class ViewHolder extends RecyclerView.ViewHolder {
    private PokemonItemBinding binding;
    private PokemonViewModel viewModel = new PokemonViewModel();

    ViewHolder(PokemonItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.setPokemonViewModel(viewModel);
    }
}
