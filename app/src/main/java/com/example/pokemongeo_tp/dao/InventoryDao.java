package com.example.pokemongeo_tp.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.pokemongeo_tp.entities.InventoryEntity;

import java.util.List;

@Dao
public interface InventoryDao {

    @Query("SELECT * FROM inventory")
    List<InventoryEntity> getAll();

    @Query("SELECT * FROM inventory WHERE id = :id")
    InventoryEntity getInventoryById(int id);

    @Query("SELECT * FROM inventory WHERE object_id = :object_id")
    InventoryEntity getInventoryByObjectId(int object_id);

}
