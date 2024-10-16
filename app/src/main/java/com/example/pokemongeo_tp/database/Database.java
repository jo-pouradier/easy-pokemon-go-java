package com.example.pokemongeo_tp.database;

import android.content.Context;
import android.content.res.Resources;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pokemongeo_tp.R;
import com.example.pokemongeo_tp.dao.InventoryDao;
import com.example.pokemongeo_tp.dao.ObjectDao;
import com.example.pokemongeo_tp.dao.OwnPokemonDao;
import com.example.pokemongeo_tp.dao.PokemonDao;
import com.example.pokemongeo_tp.entities.InventoryEntity;
import com.example.pokemongeo_tp.entities.ObjectEntity;
import com.example.pokemongeo_tp.entities.OwnPokemonEntity;
import com.example.pokemongeo_tp.entities.PokemonEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@androidx.room.Database(entities =
        {
            InventoryEntity.class,
            ObjectEntity.class,
            OwnPokemonEntity.class,
            PokemonEntity.class
        },
        version = 1
)
public abstract class Database extends RoomDatabase {
    public abstract PokemonDao pokemonDao();
    public abstract ObjectDao objectDao();
    public abstract InventoryDao inventoryDao();
    public abstract OwnPokemonDao ownPokemonDao();

    public static final String DATABASE_NAME = "database.db";

    private static Database db;

//    private Database(){}

    public static Database getInstance(Context context){
        System.out.println("Database.getInstance");
        System.out.println("db: " + db);
        System.out.println("context: " + context);
        if(db == null){
            db = Room.databaseBuilder(
                    context,
                    Database.class,
                    DATABASE_NAME
            ).build();
        }
        System.out.println("db: " + db);
        return db;
    }

    public static List<PokemonEntity> createPokemonListFromJson(Resources resources){
        List<PokemonEntity> pokeList = new ArrayList<>();
        // ouverture du fichier
        InputStreamReader isr = new InputStreamReader(resources.openRawResource(R.raw.poke));
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder builder = new StringBuilder();
        String data = "";
        //lecture du fichier.data == null => EOF
        while (data != null) {
            try {
                data = reader.readLine();
                builder.append(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } //Traitement du fichier
        try {
            JSONArray array = new JSONArray(builder.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("name");
                String image = object.getString("image");
                String type1 = object.getString("type1");
                String type2 = null;
                if (object.has("type2")) {
                    type2 = object.getString("type2");
                }

                PokemonEntity pokemon = new PokemonEntity();
                pokemon.id = i + 1;
                pokemon.name = name;
                pokemon.image = image;
                pokemon.type_1 = type1;
                pokemon.type_2 = type2;
                pokemon.discovered = false;
                pokeList.add(pokemon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pokeList;
    }

}
