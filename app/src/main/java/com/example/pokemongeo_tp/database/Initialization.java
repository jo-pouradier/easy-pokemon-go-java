package com.example.pokemongeo_tp.database;

import android.content.Context;
import android.util.Log;

import com.example.pokemongeo_tp.entities.InventoryEntity;
import com.example.pokemongeo_tp.entities.ItemEntity;
import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.threading.RequestPromise;
import com.example.pokemongeo_tp.threading.RequestThread;
import com.example.pokemongeo_tp.threading.ThreadEventListener;

import java.util.ArrayList;
import java.util.List;

public class Initialization {

    public static void InitPokemon(Context context) {
        // check if database has pokemon
        // if not, add our json
        RequestPromise<Context, List<PokemonEntity>> promise = new RequestPromise<>(
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
                        // set pokemon bulbizarre,salameche and carapuce to discovered:
                        pokemonList.get(0).discovered = true;
                        pokemonList.get(3).discovered = true;
                        pokemonList.get(6).discovered = true;

                        db.pokemonDao().insert(pokemonList);
                    }
                    return pokemons;
                },
                context
        );
        RequestThread instance = RequestThread.getInstance();
        if (instance.isNotRunning()) instance.start();
        instance.addRequest(promise);
    }

    public static void InitObject(Context context) {
        // check if database has object
        // if not, add objects
        RequestPromise<Context, List<ItemEntity>> promise = new RequestPromise<>(
                new ThreadEventListener<List<ItemEntity>>() {
                    @Override
                    public void OnEventInThread(List<ItemEntity> data) {
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                        Log.e("ERROR", "Rejecting request: creating objects. Error: " + error);
                    }
                },
                (Context ctx) -> {
                    Database db = Database.getInstance(ctx);
                    if (db.itemDao().getAll().isEmpty()) {
                        ItemEntity obj = new ItemEntity();
                        obj.item_id = 1;
                        obj.name = "Pokeball";
                        obj.image = "pokeball";
                        db.itemDao().insert(obj);

                        obj = new ItemEntity();
                        obj.item_id = 2;
                        obj.name = "Potion";
                        obj.image = "potion";
                        db.itemDao().insert(obj);

                    }
                    if (db.inventoryDao().getAll().isEmpty()) {
                        List<ItemEntity> itemList = db.itemDao().getAll();
                        List<InventoryEntity> invList = new ArrayList<InventoryEntity>();
                        for (ItemEntity item : itemList) {
                            invList.add(new InventoryEntity(item.item_id, item.item_id, 100));
                        }
                        db.inventoryDao().insert(invList);
                    }
                    return db.itemDao().getAll();
                },
                context
        );
        RequestThread instance = RequestThread.getInstance();
        if (instance.isNotRunning()) instance.start();
        instance.addRequest(promise);

    }
}
