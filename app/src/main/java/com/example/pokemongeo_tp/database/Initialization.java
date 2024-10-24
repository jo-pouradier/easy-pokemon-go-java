package com.example.pokemongeo_tp.database;

import android.content.Context;
import android.util.Log;

import com.example.pokemongeo_tp.entities.ItemEntity;
import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.threading.RequestPromise;
import com.example.pokemongeo_tp.threading.RequestThread;
import com.example.pokemongeo_tp.threading.ThreadEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
                        obj.quantity = 10;
                        db.itemDao().insert(obj);

                        obj = new ItemEntity();
                        obj.item_id = 2;
                        obj.name = "Potion";
                        obj.image = "potion";
                        obj.quantity = 10;
                        db.itemDao().insert(obj);

                    }

                    return db.itemDao().getAll();
                },
                context
        );
        RequestThread instance = RequestThread.getInstance();
        instance.addRequest(promise);
    }

    public static void InitPokemonStats(Context context) {
        // simplest thread
        new Thread(() -> {
            try {
                Initialization.getPokemonStatsFromPokeApi(context);
            } catch (IOException e) {
                Log.e("PokeAPIERROR", "error fetching poke api", e);
            }
        }).start();
    }

    private static void getPokemonStatsFromPokeApi(Context context) throws IOException {
        URL url = new URL("https://beta.pokeapi.co/graphql/v1beta");

        HttpURLConnection c;
        c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("POST");
        c.setDoOutput(true);
        c.setRequestProperty("Accept","*/*");

        OutputStream ostream = c.getOutputStream();
        DataOutputStream dos = new DataOutputStream(ostream);
        String data ="{ \"operationName\": \"getPokemonStats\", \"query\":\"query getPokemonStats { pokemons: pokemon_v2_pokemon(where:{id:{_lte: 151}},order_by:{id: asc}){name,id, height, pokemon_v2_pokemonstats{base_stat, pokemon_v2_stat{name}}}}\", \"variables\": null}";
        dos.writeBytes(data);
        dos.flush();
        ostream.close();

        if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(c.getResponseMessage());
        }

        InputStream instream = c.getInputStream();
        InputStreamReader isr = new InputStreamReader(instream);
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        Log.i("PokeAPI", response.toString());

        br.close();
        isr.close();
        instream.close();
        c.disconnect();
        JSONObject obj = null;
        JSONArray pokemons = null;
        try {
            obj = new JSONObject(response.toString());
            pokemons = obj.getJSONObject("data").getJSONArray("pokemons");
        } catch (JSONException e) {
            Log.e("JSONERROR", "error parsing json", e);
        }
        assert pokemons != null;

    }
}
