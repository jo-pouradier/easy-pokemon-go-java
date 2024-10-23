package com.example.pokemongeo_tp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pokemongeo_tp.entities.ItemEntity;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item")
    List<ItemEntity> getAll();

    @Query("SELECT * FROM item WHERE item_id = :id")
    ItemEntity getObjectById(int id);

    @Query("SELECT * FROM item WHERE name = :name")
    ItemEntity getObjectByName(String name);

    @Insert
    void insert(List<ItemEntity> objects);

    @Insert
    void insert(ItemEntity object);
}
