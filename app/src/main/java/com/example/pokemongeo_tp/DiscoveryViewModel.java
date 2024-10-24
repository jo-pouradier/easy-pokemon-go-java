package com.example.pokemongeo_tp;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.pokemongeo_tp.enums.POKEMON_TYPE;

public class DiscoveryViewModel extends BaseObservable {
    private Pokemon pokemon;
    private onPokemonDiscoveryEndListener onPokemonDiscoveryEndListener;

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    @Bindable
    public String getName() {
        return pokemon.getName();
    }

    @Bindable
    public int getPokemonImg() {
        return pokemon.getFrontResource();
    }

    @Bindable
    public int getPokemonType1() {
        return pokemon.getType1Resource();
    }

    @Bindable
    public int getPokemonType2Ressource() {
        return pokemon.getType2Ressource();
    }

    @Bindable
    public POKEMON_TYPE getPokemonType2() {
        return pokemon.getType2();
    }

    public void setOnClickListener(onPokemonDiscoveryEndListener onClickListener) {
        this.onPokemonDiscoveryEndListener = onClickListener;
    }

    public void onEscapeButtonClick() {
        this.onPokemonDiscoveryEndListener.onPokemonDiscoveryEnd(null);
    }

    public void onCaptureButtonClick() {
        this.onPokemonDiscoveryEndListener.onPokemonDiscoveryEnd(this.pokemon.getOrder());
    }

}