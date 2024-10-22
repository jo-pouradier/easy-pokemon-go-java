package com.example.pokemongeo_tp;

import androidx.fragment.app.FragmentManager;

public class ListenerFactory {

    public static OnClickOnPokemonListener getOnClickOnPokemonListener(FragmentManager manager) {
        return new OnClickOnPokemonListener() {
            @Override
            public void onClickOnPokemon(Pokemon pokemon) {
                if (pokemon.isDiscovered()){
                    PokedexFragment.showPokemonDetails(pokemon, manager);
                }
            }
        };
    }
}

