package com.example.ourwhatsapp.Activities.Conversations;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ourwhatsapp.Activities.Messages.ChatActivity;
import com.example.ourwhatsapp.Activities.Settings.SettingsActivity;
import com.example.ourwhatsapp.Database.AppDatabase;
import com.example.ourwhatsapp.Database.Entities.User;
import com.example.ourwhatsapp.MainActivity;
import com.example.ourwhatsapp.MyFirebaseMessagingService;
import com.example.ourwhatsapp.Repositories.ConversationRepository;
import com.example.ourwhatsapp.ViewModels.ConversationsViewModel;
import com.example.ourwhatsapp.databinding.ActivityListBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class ConversationsActivity extends AppCompatActivity {
    private ConversationsViewModel viewModel;
    private ConversationsAdapter adapter;
    private ArrayList<Conversation> conversations;
    private ConversationRepository conversationRepository;

    private final AppDatabase db = AppDatabase.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.ourwhatsapp.databinding.ActivityListBinding binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        conversationRepository = new ConversationRepository(getApplicationContext());

        viewModel = new ViewModelProvider(this).get(ConversationsViewModel.class);

        conversations = new ArrayList<>();

        viewModel.getUsers().observe(this, newConversations -> {
            newConversations.sort((o1, o2) -> {
                String timeOfO1 = o1.getLastMassageSendingTime();
                String timeOfO2 = o2.getLastMassageSendingTime();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                try {
                    if (timeOfO1 != null && timeOfO2 != null) {
                        Date date1 = sdf.parse(timeOfO1);
                        Date date2 = sdf.parse(timeOfO2);

                        if (date1 != null && date2 != null)
                            return date2.compareTo(date1); // for descending order
                        else if (date1 != null) // date2 is null
                            return -1;
                        else if (date2 != null) // date1 is null
                            return 1;
                    } else if (timeOfO1 != null) // timeOfO2 is null
                        return -1;
                    else if (timeOfO2 != null) // timeOfO1 is null
                        return 1;

                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }

                return 0;
            });

            conversations.clear();
            conversations.addAll(newConversations);
            for (Conversation conversation : newConversations) {
                FirebaseMessaging.getInstance().subscribeToTopic(conversation.getChatID() + "_" + AppDatabase.getUsername());
            }
            adapter.notifyDataSetChanged();
        });

        adapter = new ConversationsAdapter(getApplicationContext(), conversations);

        binding.listView.setAdapter(adapter);
        binding.listView.setClickable(true);

        binding.listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

            Conversation currentConversation = conversations.get(i);
            currentConversation.resetHasNewMessage();

            intent.putExtra("userName", currentConversation.getUserName());
            intent.putExtra("profilePicture", currentConversation.getProfilePicture());
            intent.putExtra("lastMassage", currentConversation.getLastMassage());
            intent.putExtra("time", currentConversation.getLastMassageSendingTime());
            intent.putExtra("id", currentConversation.getChatID());

            startActivity(intent);
        });

        binding.listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            new AlertDialog.Builder(ConversationsActivity.this)
                    .setTitle("Delete chat")
                    .setMessage("Are you sure you want to delete this chat?")

                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            conversationRepository.deleteChat(conversations.get(i).getChatID(), viewModel.getUsers()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        });

        binding.addBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create new chat - enter username");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                String newUsername = input.getText().toString();
                if (newUsername.equals(AppDatabase.getUsername())) {
                    Toast.makeText(getApplicationContext(), "You can not create chat with yourself!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newUsername.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Conversation user : conversations) {
                    if (user.getUserName().equals(newUsername)) {
                        Toast.makeText(getApplicationContext(), "Chat already exists!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                conversationRepository.createChat(newUsername, viewModel.getUsers());
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        binding.settingsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.putExtra("SHOW_LOGOUT", true);
            intent.putExtra("SHOW_URL", false);
            startActivity(intent);
        });

        MyFirebaseMessagingService.getLiveData().observe(this, s -> conversationRepository.loadConversations(viewModel.getUsers()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        conversationRepository.loadConversations(viewModel.getUsers());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    new Thread(() -> {
                        List<User> chats = db.userDao().getChats();
                        for (User chat : chats) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(chat.getChatID() + "_" + AppDatabase.getUsername());
                        }
                        db.clearAllTables();
                    }).start();
                    Intent intent = new Intent(ConversationsActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    finishAffinity();
                })
                .setNegativeButton("No", null)
                .show();
    }

}
