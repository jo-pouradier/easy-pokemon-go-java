package com.example.pokemongeo_tp.database;

import android.content.Context;

import androidx.room.Room;

import com.example.pokemongeo_tp.entities.PokemonEntity;

import java.util.List;

public class Database {
    private static Database db;
    private AppDatabaseAbstract abstractDb;
    
    private Database(Context context){
        AppDatabaseAbstract abstractDb = Room.databaseBuilder(
                context,
                AppDatabaseAbstract.class,
                "database.db"
        ).build();
    }

    public static Database getInstance(Context context){
        if(db == null){
            db = new Database(context);
        }
        return db;
    }

    public List<PokemonEntity> getAllPokemon(){
        return abstractDb.pokemonDao().getAll();
    }
}
