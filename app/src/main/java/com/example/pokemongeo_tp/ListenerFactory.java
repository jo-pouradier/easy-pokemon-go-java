package com.example.pokemongeo_tp;

import androidx.fragment.app.FragmentManager;

import com.example.pokemongeo_tp.databinding.MapFragmentBinding;
import com.example.pokemongeo_tp.databinding.StarterFragmentBinding;
import com.example.pokemongeo_tp.threading.ThreadEventListener;

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

