package com.example.pokemongeo_tp.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "own_pokemon",
        foreignKeys = {
                @ForeignKey(
                        entity = PokemonEntity.class,
                        parentColumns = "id",
                        childColumns = "pokemon_id"
                )
        },
        indices = {@Index(value = {"name"}, unique = true)}
)
public class OwnPokemonEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int health;
    public int pokemon_id;
    public int level;
    public String name;

    public OwnPokemonEntity(int id, int health, int pokemon_id, int level, String name) {
        this.id = id;
        this.health = health;
        this.pokemon_id = pokemon_id;
        this.level = level;
        this.name = name;
    }

    public OwnPokemonEntity(PokemonEntity pokemon) {
        this.pokemon_id = pokemon.id;
        this.name = pokemon.name;
        this.health = pokemon.hp;
        this.level = 1;
    }

}
