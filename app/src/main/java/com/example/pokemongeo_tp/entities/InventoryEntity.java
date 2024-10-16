package com.example.pokemongeo_tp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(tableName = "inventory",
    foreignKeys = {
        @ForeignKey(
            entity = ObjectEntity.class,
            parentColumns = "id",
            childColumns = "object_id"
        )
    }
)
public class InventoryEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int object_id;
    public int quantity;
}
