package com.example.pokemongeo_tp;

import androidx.fragment.app.FragmentManager;

import com.example.pokemongeo_tp.threading.ThreadEventListener;

public class ListenerFactory {
    public OnSelectStarterListener getOnSelectStarterListener(FragmentManager manager) {
        return new OnSelectStarterListener() {
            @Override
            public void onSelectStarter(Pokemon pokemon) {
                StarterFragment.SelectStarter(pokemon, manager);
            }
        };
    }
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

    public static ThreadEventListener<Void> getVoidListener(){
        return new ThreadEventListener<Void>() {
            @Override
            public void OnEventInThread(Void data) {
            }

            @Override
            public void OnEventInThreadReject(String error) {
            }
        };
    }
}

