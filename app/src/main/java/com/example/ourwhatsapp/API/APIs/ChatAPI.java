package com.example.ourwhatsapp.API.APIs;


import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ourwhatsapp.API.Entities.Chat;
import com.example.ourwhatsapp.API.Entities.CreateChatReq;
import com.example.ourwhatsapp.API.Entities.SendMessageReq;
import com.example.ourwhatsapp.API.WebServiceAPI;
import com.example.ourwhatsapp.Activities.Conversations.Conversation;
import com.example.ourwhatsapp.Activities.Messages.Message;
import com.example.ourwhatsapp.Database.AppDatabase;
import com.example.ourwhatsapp.Database.DatabaseDAOs.MessagesDao;
import com.example.ourwhatsapp.Database.DatabaseDAOs.UserDao;
import com.example.ourwhatsapp.Database.Entities.Messages;
import com.example.ourwhatsapp.Database.Entities.User;
import com.example.ourwhatsapp.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPI {
    private final WebServiceAPI webServiceAPI;
    private final MessagesDao messagesDao;
    private final UserDao userDao;
    private final Context context;


    public ChatAPI(MessagesDao messagesDao, UserDao userDao, Context context) {
        this.messagesDao = messagesDao;
        this.userDao = userDao;
        this.context = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.getURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getConversations(String token, MutableLiveData<List<Conversation>> users) {
        Call<List<Chat>> getChats = webServiceAPI.getChats(token);
        getChats.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(@NonNull Call<List<Chat>> call, @NonNull Response<List<Chat>> response) {
                if (response.isSuccessful()) {
                    List<Chat> chats = response.body();
                    List<Conversation> conversations = new ArrayList<>();
                    List<User> newUsers = new ArrayList<>();
                    for (Chat chat : chats) {
                        if (chat.getLastMessage() != null) {
                            newUsers.add(new User(chat.getId(),
                                    chat.getUser().getDisplayName(), chat.getUser().getUsername(),
                                    chat.getUser().getProfilePic(), chat.getLastMessage().getContent(),
                                    chat.getLastMessage().getCreated()));
                            conversations.add(new Conversation(chat.getUser().getDisplayName(),
                                    chat.getUser().getProfilePic(),
                                    chat.getLastMessage().getContent(), chat.getLastMessage().getCreated(),
                                    chat.getId()));
                        } else {
                            newUsers.add(new User(chat.getId(),
                                    chat.getUser().getDisplayName(), chat.getUser().getUsername(),
                                    chat.getUser().getProfilePic(), null,
                                    null));
                            conversations.add(new Conversation(chat.getUser().getDisplayName(),
                                    chat.getUser().getProfilePic(),
                                    null, null,
                                    chat.getId()));
                        }
                    }
                    users.postValue(conversations);
                    new Thread(() -> {
                        userDao.insert(newUsers);
                    }).start();
                } else {
                    Toast.makeText(context, "code:" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(context, "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMessages(String token, String chatID, MutableLiveData<List<Message>> messages) {
        Call<List<com.example.ourwhatsapp.API.Entities.Message>> getMessages = webServiceAPI.getMessages(token, chatID);
        getMessages.enqueue(new Callback<List<com.example.ourwhatsapp.API.Entities.Message>>() {
            @Override
            public void onResponse(Call<List<com.example.ourwhatsapp.API.Entities.Message>> call, Response<List<com.example.ourwhatsapp.API.Entities.Message>> response) {
                if (response.isSuccessful()) {
                    List<com.example.ourwhatsapp.API.Entities.Message> apiMessages = response.body();
                    List<Message> chat = new ArrayList<>();
                    List<Messages> newMessages = new ArrayList<>();
                    for (com.example.ourwhatsapp.API.Entities.Message apiMessage : apiMessages) {
                        chat.add(new Message(apiMessage.getContent(), apiMessage.getCreated(),
                                apiMessage.getSender().getUsername().equals(AppDatabase.getUsername()) ? Message.MessageType.SENT : Message.MessageType.RECEIVED));
                        newMessages.add(new Messages(chatID, apiMessage.getContent(),
                                apiMessage.getSender().getUsername(), apiMessage.getCreated()));
                    }
                    messages.postValue(chat);
                    new Thread(() -> {
                        // update room
                        messagesDao.delete(chatID);
                        messagesDao.insert(newMessages);
                    }).start();
                } else {
                    Toast.makeText(context, "code:" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<com.example.ourwhatsapp.API.Entities.Message>> call, Throwable t) {
                Toast.makeText(context, "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteChat(String token, String chatID, MutableLiveData<List<Conversation>> users) {
        Call<Void> deleteChat = webServiceAPI.deleteChat(token, chatID);

        deleteChat.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    List<Conversation> newUsers = new ArrayList<>();
                    for (Conversation user : users.getValue()) {
                        if (!user.getChatID().equals(chatID)) {
                            newUsers.add(user);
                        }
                    }
                    users.postValue(newUsers);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createChat(String token, String username, MutableLiveData<List<Conversation>> users) {
        Call<Chat> createChat = webServiceAPI.createChat(token, new CreateChatReq(username));

        createChat.enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.code() == 400) {
                    Toast.makeText(context, "Username does not exists!", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 200) {
                    Chat newChat = response.body();
                    List<Conversation> newUsers = users.getValue();
                    newUsers.add(new Conversation(
                            newChat.getUser().getDisplayName(),
                            newChat.getUser().getProfilePic(),
                            null, null, newChat.getId()
                    ));
                    List<User> listOfUser = new ArrayList<>();
                    listOfUser.add(new User(
                            newChat.getId(),
                            newChat.getUser().getDisplayName(),
                            newChat.getUser().getUsername(),
                            newChat.getUser().getDisplayName(),
                            null, null
                    ));
                    new Thread(() -> {
                        userDao.insert(listOfUser);
                    }).start();
                    users.postValue(newUsers);
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                Toast.makeText(context, "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendMessage(String token, String chatID, String content, MutableLiveData<List<Message>> messages) {
        Call<com.example.ourwhatsapp.API.Entities.Message> sendMessage = webServiceAPI.sendMessage(token, chatID,
                new SendMessageReq(content));

        sendMessage.enqueue(new Callback<com.example.ourwhatsapp.API.Entities.Message>() {
            @Override
            public void onResponse(Call<com.example.ourwhatsapp.API.Entities.Message> call, Response<com.example.ourwhatsapp.API.Entities.Message> response) {
                if (response.isSuccessful()) {
                    com.example.ourwhatsapp.API.Entities.Message newMessage = response.body();
                    List<Message> newMessages = messages.getValue();
                    newMessages.add(0, new Message(
                       newMessage.getContent(),
                       newMessage.getCreated(),
                       Message.MessageType.SENT
                    ));
                    List<Messages> listOfMessage = new ArrayList<>();
                    listOfMessage.add(new Messages(
                       chatID, newMessage.getContent(), newMessage.getSender().getUsername(), newMessage.getCreated()
                    ));
                    new Thread(() -> {
                        messagesDao.insert(listOfMessage);
                    }).start();
                    messages.postValue(newMessages);
                }
            }

            @Override
            public void onFailure(Call<com.example.ourwhatsapp.API.Entities.Message> call, Throwable t) {
                Toast.makeText(context, "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}