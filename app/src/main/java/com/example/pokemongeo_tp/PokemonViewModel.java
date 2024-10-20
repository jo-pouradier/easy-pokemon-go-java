package com.example.pokemongeo_tp;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class PokemonViewModel extends BaseObservable {
    private Pokemon pokemon = new Pokemon();

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        notifyChange();
    }

    @Bindable
    public int getFront() {
        return pokemon.getFrontResource();
    }

    @Bindable
    public String getName() {
        return pokemon.getName();
    }

    @Bindable
    public String getType1() {
        return pokemon.getType1String();
    }

    @Bindable
    public int getType1Resource() {
        return pokemon.getType1Resource();
    }

    @Bindable
    public String getType2() {
        if (pokemon.getType2() != null) return pokemon.getType2String();
        return "";
    }

    @Bindable
    public int getType2Ressource() {
        if (pokemon.getType2() != null) return pokemon.getType2Ressource();
        return -1;
    }

    @Bindable
    public String getNumber() {
        return "#" + pokemon.getOrder();
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Drawable getImage(Context context, int res) {
        if (res != -1)
            return ResourcesCompat.getDrawable(context.getResources(), res, context.getTheme());
        else return null;
    }
}