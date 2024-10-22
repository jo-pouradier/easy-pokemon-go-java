package com.example.pokemongeo_tp.database;

import android.content.Context;
import android.util.Log;

import com.example.pokemongeo_tp.entities.ObjectEntity;
import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.threading.RequestPromise;
import com.example.pokemongeo_tp.threading.RequestThread;
import com.example.pokemongeo_tp.threading.ThreadEventListener;

import java.util.List;

public class Initalization {

    public static void InitPokemon(Context context) {
        // check if database has pokemon
        // if not, add our json
        RequestPromise<Context, List<PokemonEntity>> promise = new RequestPromise<Context, List<PokemonEntity>>(
                new ThreadEventListener<List<PokemonEntity>>() {
                    @Override
                    public void OnEventInThread(List<PokemonEntity> data) {
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                    }
                },
                (Context ctx) -> {
                    Database db = Database.getInstance(ctx);
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
        if (!instance.isRunning()) instance.start();
        instance.start();
        instance.addRequest(promise);
    }

    public static void InitObject(Context context) {
        // check if database has object
        // if not, add objects
        RequestPromise<Context, List<ObjectEntity>> promise = new RequestPromise<Context, List<ObjectEntity>>(
                new ThreadEventListener<List<ObjectEntity>>() {
                    @Override
                    public void OnEventInThread(List<ObjectEntity> data) {
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                        Log.e("ERROR", "Rejecting request: creating objects. Error: " + error);
                    }
                },
                (Context ctx) -> {
                    Database db = Database.getInstance(ctx);
                    List<ObjectEntity> objects = db.objectDao().getAll();
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
                    return db.objectDao().getAll();
                },
                context
        );
        RequestThread instance = RequestThread.getInstance();
        if (!instance.isRunning()) instance.start();
        instance.addRequest(promise);

    }
}
