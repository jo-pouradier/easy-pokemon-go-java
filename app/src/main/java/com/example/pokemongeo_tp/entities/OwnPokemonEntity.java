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

    public int pokemon_id;
    public int level;
    public String name;
}
