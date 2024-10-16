package com.example.pokemongeo_tp.database;

import android.content.Context;

import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.threading.RequestPromise;
import com.example.pokemongeo_tp.threading.RequestThread;
import com.example.pokemongeo_tp.threading.ThreadEventListener;

import java.util.List;

public class Initalization {

    public static void InitPokemon(Context context) {
        // check if database has pokemon
        // if not, add our json
        RequestPromise<Context, List<PokemonEntity>> promise = new RequestPromise<>(
                new ThreadEventListener(){
                    @Override
                    public void OnEventInThread(Object data) {}
                    @Override
                    public void OnEventInThreadReject(String error) {
                        System.out.println("Error: " + error);
                    }
                },
                (Context ctx) -> {
                    Database db = Database.getInstance(context);
                    List<PokemonEntity> pokemons = db.pokemonDao().getAll();
                    if (pokemons.isEmpty()) {
                        // add pokemon from json
                        List<PokemonEntity> pokemonList = Database.createPokemonListFromJson(context.getResources());
                        db.pokemonDao().insert(pokemonList);
                    }
                    return pokemons;
                },
                context
        );
        RequestThread instance = RequestThread.getInstance();
        instance.addRequest(promise);
    }

    public static void InitObject(Context context) {
        // check if database has object
        // if not, add objects
//        Database db = Database.getInstance(context);
//
//        if (db.objectDao().getAll().isEmpty()) {
//            ObjectEntity obj = new ObjectEntity();
//            obj.id = 1;
//            obj.name = "Pokeball";
//            obj.image = "pokeball";
//            db.objectDao().insert(obj);
//
//            obj = new ObjectEntity();
//            obj.id = 2;
//            obj.name = "Potion";
//            obj.image = "potion";
//            db.objectDao().insert(obj);
//
//        }
    }
}
