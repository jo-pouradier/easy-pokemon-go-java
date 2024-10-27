package com.example.pokemongeo_tp.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "pokemon", indices = {@Index(value = {"name"}, unique = true)})
public class PokemonEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String image;
    public int attack;
    public int special_attack;
    public int defense;
    public int special_defense;
    public int hp;
    public int speed;
    public String type_1;
    public String type_2;
    public int weight;
    public int height;
    public int capture_rate;
    public boolean discovered;

    @NonNull
    @Override
    public String toString() {
        return "Pokemon: name="+name+", id="+id+";";
    }

    public void setStat(String statName, Integer statValue) throws NoSuchFieldException, IllegalAccessException {
        // set stat by name
        Field field = this.getClass().getDeclaredField(statName);
        field.setInt(this, statValue);
    }
}
