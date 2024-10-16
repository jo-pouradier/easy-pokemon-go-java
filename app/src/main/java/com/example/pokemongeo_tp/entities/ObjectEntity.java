package com.example.pokemongeo_tp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "object")
public class ObjectEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String image;
}
