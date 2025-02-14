package com.example.ourwhatsapp.Database.DatabaseDAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ourwhatsapp.Database.Entities.Settings;

@Dao
public interface SettingsDao {

    @Query("SELECT * FROM settings WHERE `key` = :key")
    Settings get(String key);

    @Update
    void update(Settings... settings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Settings settings);

    @Delete
    void delete(Settings... settings);

}
