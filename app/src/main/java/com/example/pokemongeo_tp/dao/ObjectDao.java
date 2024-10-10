package com.example.pokemongeo_tp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pokemongeo_tp.entities.ObjectEntity;

import java.util.List;

@Dao
public interface ObjectDao {

    @Query("SELECT * FROM object")
    List<ObjectEntity> getAll();

    @Query("SELECT * FROM object WHERE id = :id")
    ObjectEntity getObjectById(int id);

    @Query("SELECT * FROM object WHERE name = :name")
    ObjectEntity getObjectByName(String name);

    @Insert
    void insert(List<ObjectEntity> objects);

    @Insert
    void insert(ObjectEntity object);
}
