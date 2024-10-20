package com.example.pokemongeo_tp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELEct * FROM pokemon ORDER BY RANDOM() limit 1")
    PokemonEntity getRandomPokemon();

    @Insert
    void insert(List<PokemonEntity> pokemons);

    @Insert
    void insert(PokemonEntity pokemon);

    @Update
    void update(PokemonEntity pokemon);

}
