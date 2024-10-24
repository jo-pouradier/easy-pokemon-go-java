package com.example.pokemongeo_tp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pokemongeo_tp.entities.OwnPokemonEntity;

import java.util.List;

@Dao
public interface OwnPokemonDao {

    @Query("SELECT * FROM own_pokemon")
    List<OwnPokemonEntity> getAll();

    @Query("SELECT * FROM own_pokemon WHERE id = :id")
    OwnPokemonEntity getOwnPokemonById(int id);

    @Query("SELECT * FROM own_pokemon WHERE pokemon_id = :pokemon_id")
    OwnPokemonEntity getOwnPokemonByPokemonId(int pokemon_id);

    @Query("SELECT * FROM own_pokemon WHERE name = :name")
    OwnPokemonEntity getOwnPokemonByName(String name);

    @Insert
    void insert(OwnPokemonEntity ownPokemon);

}
