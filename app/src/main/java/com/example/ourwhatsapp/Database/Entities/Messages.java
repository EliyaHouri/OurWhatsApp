package com.example.ourwhatsapp.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Messages {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String chatID;

    private final String content;

    private final String sender;

    private final String date;

    public Messages( String chatID, String content, String sender, String date) {
        this.chatID = chatID;
        this.content = content;
        this.sender = sender;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getChatID() {
        return chatID;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }
}
