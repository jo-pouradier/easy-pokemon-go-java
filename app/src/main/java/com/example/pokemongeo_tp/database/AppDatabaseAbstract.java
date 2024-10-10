package com.example.pokemongeo_tp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.pokemongeo_tp.dao.InventoryDao;
import com.example.pokemongeo_tp.dao.ObjectDao;
import com.example.pokemongeo_tp.dao.OwnPokemonDao;
import com.example.pokemongeo_tp.dao.PokemonDao;
import com.example.pokemongeo_tp.entities.InventoryEntity;
import com.example.pokemongeo_tp.entities.ObjectEntity;
import com.example.pokemongeo_tp.entities.OwnPokemonEntity;
import com.example.pokemongeo_tp.entities.PokemonEntity;


@Database(entities =
        {
            InventoryEntity.class,
            ObjectEntity.class,
            OwnPokemonEntity.class,
            PokemonEntity.class
        },
        version = 1
)
public abstract class AppDatabaseAbstract extends RoomDatabase {
    public abstract PokemonDao pokemonDao();
    public abstract ObjectDao objectDao();
    public abstract InventoryDao inventoryDao();
    public abstract OwnPokemonDao ownPokemonDao();
}
