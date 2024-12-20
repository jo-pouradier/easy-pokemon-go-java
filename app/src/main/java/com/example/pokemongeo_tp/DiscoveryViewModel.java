package com.example.pokemongeo_tp;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

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
    public String getPokemonType1() {
        return pokemon.getType1String();
    }

    @Bindable
    public int getPokemonType2Ressource() {
        return pokemon.getType2Ressource();
    }

    @Bindable
    public String getPokemonType2() {
        return pokemon.getType2String();

    }

    @Bindable
    public String getWeight() {
        return String.valueOf(pokemon.getWeight());
    }

    @Bindable
    public String getHeight() {
        return String.valueOf(pokemon.getHeight());
    }

    @Bindable
    public String getCaptureRate() {
        return String.valueOf(pokemon.getCaptureRate());
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


    public Drawable getImage(Context context, int res) {
        return ResourcesCompat.getDrawable(context.getResources(), res, context.getTheme());
    }

}