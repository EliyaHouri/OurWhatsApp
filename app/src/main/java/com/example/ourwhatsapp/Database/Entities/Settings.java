package com.example.ourwhatsapp.Database.Entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(indices = {@Index(value = {"key"}, unique = true)})
public class Settings {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String key;
    private final String value;

    public Settings(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

}
