package com.example.ourwhatsapp.Activities.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ourwhatsapp.MyFirebaseMessagingService;
import com.example.ourwhatsapp.R;
import com.example.ourwhatsapp.Repositories.ChatRepository;
import com.example.ourwhatsapp.Utils;
import com.example.ourwhatsapp.ViewModels.ChatViewModel;
import com.example.ourwhatsapp.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ChatViewModel viewModel;
    private ActivityChatBinding binding;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private ChatRepository chatRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent activityIntent = getIntent();

        if (activityIntent == null || !activityIntent.hasExtra("userName") || !activityIntent.hasExtra("profilePicture") || !activityIntent.hasExtra("id")) {
            finish();
        }

        assert activityIntent != null;
        String userName = activityIntent.getStringExtra("userName");
        String profilePicture = activityIntent.getStringExtra("profilePicture");
        String chatID = getIntent().getStringExtra("id");

        viewModel.getDisplayName().setValue(userName);
        viewModel.getProfilePicture().setValue(profilePicture);

        messages = new ArrayList<>();

        messageAdapter = new MessageAdapter(this, R.layout.custom_messages_item, messages);
        binding.listView.setTranscriptMode((AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL));
        binding.listView.setAdapter(messageAdapter);

        chatRepository = new ChatRepository(getApplicationContext(), chatID);

        viewModel.getProfilePicture().observe(this, profilePicture1 -> {
            Utils.displayBase64Image(profilePicture1, binding.userImageProfileImage);
        });

        viewModel.getDisplayName().observe(this, displayName -> binding.userTextUserName.setText(displayName));

        viewModel.getChat().observe(this, newMessages -> {
            messages.clear();
            messages.addAll(newMessages);
            messageAdapter.notifyDataSetChanged();
            binding.listView.setSelection(messageAdapter.getCount() - 1);
        });

        binding.returnBtn.setOnClickListener(view -> finish());

        binding.sendBtn.setOnClickListener(view -> {
            chatRepository.sendMessage(binding.messageEditText.getText().toString(),
                    chatID, viewModel.getChat());
            binding.messageEditText.setText(null);
        });

        MyFirebaseMessagingService.getLiveData().observe(this, s -> {
            chatRepository.loadMessages(viewModel.getChat());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        chatRepository.loadMessages(viewModel.getChat());
    }
}
