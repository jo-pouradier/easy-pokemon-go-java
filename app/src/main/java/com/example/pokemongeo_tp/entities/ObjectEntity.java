package com.example.pokemongeo_tp.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "object", indices = {@Index(value = {"name"}, unique = true)})
public class ObjectEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String image;
}
