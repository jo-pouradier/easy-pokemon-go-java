package com.example.pokemongeo_tp.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.pokemongeo_tp.InventoryFragment;

public class InventoryItemEntity {
    @Embedded
    public InventoryFragment inv;
    @Relation(
            parentColumn = "inv_id",
            entityColumn = "item_id"
    )
    public ItemEntity item;;
}