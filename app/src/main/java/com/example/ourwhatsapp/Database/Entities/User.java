package com.example.ourwhatsapp.Database.Entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"username"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String chatID;
    private String displayName;
    private final String username;
    private final String profilePhoto;
    private final String lastMessage;
    private final String lastMassageSendingTime;
    public User(String chatID, String displayName, String username, String profilePhoto, String lastMessage, String lastMassageSendingTime) {
        this.chatID = chatID;
        this.displayName = displayName;
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.lastMessage = lastMessage;
        this.lastMassageSendingTime = lastMassageSendingTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMassageSendingTime() {
        return lastMassageSendingTime;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getChatID() {
        return chatID;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setDisplayName(String newName) {
        this.displayName = newName;
    }

    public void setId(int id) {
        this.id = id;
    }
}
