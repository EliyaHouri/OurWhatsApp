package com.example.ourwhatsapp.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.ourwhatsapp.API.APIs.ChatAPI;
import com.example.ourwhatsapp.Activities.Conversations.Conversation;
import com.example.ourwhatsapp.Database.AppDatabase;
import com.example.ourwhatsapp.Database.DatabaseDAOs.MessagesDao;
import com.example.ourwhatsapp.Database.DatabaseDAOs.UserDao;
import com.example.ourwhatsapp.Database.Entities.User;

import java.util.ArrayList;
import java.util.List;


public class ConversationRepository {
    private final UserDao userDao;
    private final MessagesDao messagesDao;
    private final ChatAPI chatAPI;

    public ConversationRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        messagesDao = db.messageDao();
        String token = AppDatabase.getToken();
        chatAPI = new ChatAPI(messagesDao, userDao, context);
    }

    public void loadConversations(MutableLiveData<List<Conversation>> users) {
        new Thread(() -> {
            // load from ROOM
            List<User> chats = userDao.getChats();
            List<Conversation> conversations = new ArrayList<>();
            for (User chat : chats) {
                conversations.add(new Conversation(chat.getDisplayName(), chat.getProfilePhoto(),
                        chat.getLastMessage(), chat.getLastMassageSendingTime(), chat.getChatID()));
            }
            users.postValue(conversations);
            // update from API
            chatAPI.getConversations(AppDatabase.getToken(), users);
        }).start();
    }

    public void deleteChat(String chatID, MutableLiveData<List<Conversation>> users) {
        new Thread(() -> {
            // delete from room
            userDao.deleteByChatID(chatID);
            messagesDao.delete(chatID);
            // send delete to server
            chatAPI.deleteChat(AppDatabase.getToken(), chatID, users);
        }).start();
    }

    public void createChat(String username, MutableLiveData<List<Conversation>> users) {
        new Thread(() -> {
            // send delete to server
            chatAPI.createChat(AppDatabase.getToken(), username, users);
        }).start();
    }
}