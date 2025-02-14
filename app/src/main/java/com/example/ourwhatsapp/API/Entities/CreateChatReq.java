package com.example.ourwhatsapp.API.Entities;

public class CreateChatReq {
    private String username;

    public CreateChatReq(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}