package com.example.ourwhatsapp.Activities.Conversations;

public class Conversation {
    private final String userName;
    private final String profilePicture;
    private final String lastMassage;
    private final String lastMassageSendingTime;
    private final String chatID;
    private int hasNewMessage;

    public Conversation(String userName, String profilePicture, String lastMassage, String lastMassageSendingTime, String chatID) {
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.lastMassage = lastMassage;
        this.lastMassageSendingTime = lastMassageSendingTime;
        this.chatID = chatID;
    }

    public String getChatID() {
        return chatID;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getLastMassage() {
        return lastMassage;
    }

    public String getLastMassageSendingTime() {
        return lastMassageSendingTime;
    }

    public String getUserName() {
        return userName;
    }

    public int getHasNewMessage() {return hasNewMessage;}

    public void resetHasNewMessage() {
        hasNewMessage = 0;
    }
}
