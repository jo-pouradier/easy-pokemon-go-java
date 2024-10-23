package com.example.pokemongeo_tp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pokemongeo_tp.entities.InventoryEntity;

import java.util.List;

@Dao
public interface InventoryDao {

    @Query("SELECT * FROM inventory")
    List<InventoryEntity> getAll();

    @Query("SELECT * FROM inventory WHERE inv_id = :id")
    InventoryEntity getInventoryById(int id);

    @Query("SELECT * FROM inventory WHERE item_id = :item_id")
    InventoryEntity getInventoryByObjectId(int item_id);
    @Insert
    void insert(InventoryEntity inv);

    @Insert
    void insert(List<InventoryEntity> inv);
}
