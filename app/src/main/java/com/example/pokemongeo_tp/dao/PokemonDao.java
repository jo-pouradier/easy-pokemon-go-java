package com.example.pokemongeo_tp.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.pokemongeo_tp.entities.PokemonEntity;

import java.util.List;

@Dao
public interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    List<PokemonEntity> getAll();

    @Query("SELECT * FROM pokemon WHERE id = :id")
    PokemonEntity getPokemonById(int id);

    @Query("SELECT * FROM pokemon WHERE name = :name")
    PokemonEntity getPokemonByName(String name);
}
