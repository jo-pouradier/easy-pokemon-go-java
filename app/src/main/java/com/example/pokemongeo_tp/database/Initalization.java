package com.example.pokemongeo_tp.database;

import android.content.Context;

import com.example.pokemongeo_tp.entities.ObjectEntity;
import com.example.pokemongeo_tp.entities.PokemonEntity;

import java.util.List;

public class Initalization {

    public static void InitPokemon(Context context){
        // check if database has pokemon
        // if not, add our json

        Database db = Database.getInstance(context);
        List<PokemonEntity> pokemons = db.pokemonDao().getAll();
        if(pokemons.isEmpty()){
            // add pokemon
            List<PokemonEntity> pokemonList = Database.createPokemonListFromJson(context.getResources());
            db.pokemonDao().insert(pokemonList);
        }
    }

    public static void InitObject(Context context){
        // check if database has object
        // if not, add objects
        Database db = Database.getInstance(context);

        if (db.objectDao().getAll().isEmpty()) {
            ObjectEntity obj = new ObjectEntity();
            obj.id = 1;
            obj.name = "Pokeball";
            obj.image = "pokeball";
            db.objectDao().insert(obj);

            obj = new ObjectEntity();
            obj.id = 2;
            obj.name = "Potion";
            obj.image = "potion";
            db.objectDao().insert(obj);

        }
    }
}
