package com.example.pokemongeo_tp;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import com.example.pokemongeo_tp.enums.POKEMON_TYPE;

public class DiscoveryViewModel extends ViewModel {
    private Pokemon pokemon;

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    @Bindable
    public String getPokemonName(){
        return pokemon.getName();
    }

    @Bindable
    public int getPokemonImg(){
        return pokemon.getFrontResource();
    }

    @Bindable
    public int getPokemonType1(){
        return pokemon.getType1Resource();
    }

    @Bindable
    public int getPokemonType2Ressource(){
        return pokemon.getType2Ressource();
    }

    @Bindable
    public POKEMON_TYPE getPokemonType2(){
        return pokemon.getType2();
    }
}