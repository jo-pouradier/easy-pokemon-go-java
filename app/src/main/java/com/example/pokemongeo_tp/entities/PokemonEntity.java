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

    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "image")
    public String image;
    @ColumnInfo(name = "attack")
    public int attack;
    @ColumnInfo(name = "special_attack")
    public int special_attack;
    @ColumnInfo(name = "defense")
    public int defense;
    @ColumnInfo(name = "special_defense")
    public int special_defense;
    @ColumnInfo(name = "hp")
    public int hp;
    @ColumnInfo(name = "speed")
    public int speed;
    @ColumnInfo(name = "type_1")
    public String type_1;
    @ColumnInfo(name = "type_2")
    public String type_2;
    @ColumnInfo(name = "discovered")
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
