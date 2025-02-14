package com.example.ourwhatsapp.API.Entities;

public class Message {
    private String id;
    private final String created;
    private final User sender;
    private String content;

    public Message(String id, String created, User sender, String content) {
        this.id = id;
        this.created = created;
        this.sender = sender;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }
    
    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
