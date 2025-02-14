package com.example.ourwhatsapp.API.Entities;

import java.util.List;

public class ChatData {
    private String id;
    private final List<User> users;
    private List<Message> messages;

    public ChatData(String id, List<User> users, List<Message> messages) {
        this.id = id;
        this.users = users;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
