package com.example.ourwhatsapp.API.Entities;

public class LastMessage {
    private String id;

    private final String created;

    private String content;

    public LastMessage(String id, String created, String content) {
        this.id = id;
        this.created = created;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
