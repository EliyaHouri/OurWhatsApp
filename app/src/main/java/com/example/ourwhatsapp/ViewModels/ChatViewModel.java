package com.example.ourwhatsapp.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ourwhatsapp.Activities.Messages.Message;
import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<String> profilePicture;
    private MutableLiveData<String> displayName;
    private MutableLiveData<List<Message>> chat;

    public MutableLiveData<String> getProfilePicture() {
        if (profilePicture == null) {
            profilePicture = new MutableLiveData<>();
        }
        return profilePicture;
    }

    public MutableLiveData<String> getDisplayName() {
        if (displayName == null) {
            displayName = new MutableLiveData<>();
        }
        return displayName;
    }

    public MutableLiveData<List<Message>> getChat() {
        if (chat == null) {
            chat = new MutableLiveData<>();
            chat.setValue(new ArrayList<>());
        }
        return chat;
    }
}
