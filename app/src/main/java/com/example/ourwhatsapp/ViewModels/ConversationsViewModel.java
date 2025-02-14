package com.example.ourwhatsapp.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ourwhatsapp.Activities.Conversations.Conversation;

import java.util.ArrayList;
import java.util.List;

public class ConversationsViewModel extends ViewModel {
    private MutableLiveData<List<Conversation>> users;

    public MutableLiveData<List<Conversation>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
            users.setValue(new ArrayList<>());
        }
        return users;
    }
}
