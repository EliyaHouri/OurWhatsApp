package com.example.ourwhatsapp.Activities.Messages;

public class Message {
    private final String content;
    private final String time;
    private final MessageType messageType;

    public Message(String content, String time, MessageType messageType) {
        this.content = content;
        this.time = time;
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public enum MessageType {
        SENT,
        RECEIVED
    }
}
