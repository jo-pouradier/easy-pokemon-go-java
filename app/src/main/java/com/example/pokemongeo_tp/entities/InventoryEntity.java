package com.example.pokemongeo_tp.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "inventory",
        foreignKeys = {
                @ForeignKey(
                        entity = ItemEntity.class,
                        parentColumns = "item_id",
                        childColumns = "item_id"
                )
        }
)
public class InventoryEntity {
    @PrimaryKey(autoGenerate = true)
    public int inv_id;
    public int item_id;
    public int quantity;

    public InventoryEntity(int inv_id, int item_id, int quantity){
        this.inv_id = inv_id;
        this.quantity = quantity;
        this.item_id = item_id;
    }
    }

