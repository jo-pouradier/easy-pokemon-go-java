package com.example.pokemongeo_tp.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "item", indices = {@Index(value = {"name"}, unique = true)})
public class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    public int item_id;
    public String name;
    public String image;
}
