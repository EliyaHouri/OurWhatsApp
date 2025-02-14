package com.example.ourwhatsapp.Activities.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.ourwhatsapp.Database.AppDatabase;
import com.example.ourwhatsapp.Database.Entities.User;
import com.example.ourwhatsapp.MainActivity;
import com.example.ourwhatsapp.Utils;
import com.example.ourwhatsapp.databinding.ActivitySettingsBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private final AppDatabase db = AppDatabase.getInstance(this);

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        sharedPreferences = getSharedPreferences("OurLocalPlace", MODE_PRIVATE);

        boolean showLogout = getIntent().getBooleanExtra("SHOW_LOGOUT", false);
        binding.logoutButton.setVisibility(showLogout ? View.VISIBLE : View.GONE);

        boolean showServerURL = getIntent().getBooleanExtra("SHOW_URL", false);
        binding.serverPort.setVisibility(showServerURL ? View.VISIBLE : View.GONE);
        binding.serverTag.setVisibility(showServerURL ? View.VISIBLE : View.GONE);

        binding.serverPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isValidURL(s.toString())) {
                    binding.serverPort.setTextColor(Color.RED);
                } else {
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        // Night mode is enabled
                        binding.serverPort.setTextColor(Color.WHITE);
                    } else {
                        // Night mode is disabled
                        binding.serverPort.setTextColor(Color.BLACK);
                    }
                }
            }
        });
        setContentView(binding.getRoot());

        // Load saved theme
        String savedTheme = sharedPreferences.getString("theme", "Classic");
        if ("Dark".equals(savedTheme)) {
            binding.themeSpinner.setSelection(1);
        } else {
            binding.themeSpinner.setSelection(0);
        }

        String savedURL = sharedPreferences.getString("url", "http://10.0.2.2:12345");
        binding.serverPort.setText(savedURL);


        binding.logoutButton.setOnClickListener(view -> {
            new Thread(() -> {
                List<User> chats = db.userDao().getChats();
                for (User chat : chats) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(chat.getChatID() + "_" + AppDatabase.getUsername());
                }
                db.clearAllTables();
            }).start();
            // Start MainActivity after logging out
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedTheme = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("theme", selectedTheme);
                editor.apply();

                if ("Dark".equals(selectedTheme)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.exitBtn.setOnClickListener(view -> {
            String url = binding.serverPort.getText().toString();
            if (url.isEmpty()) {
                finish();
            } else if (Utils.isValidURL(url)) {
                Toast.makeText(getApplicationContext(), "Invalid url", Toast.LENGTH_LONG / 2).show();
            } else {
                new Thread(() -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("url", binding.serverPort.getText().toString());
                    editor.apply();
                }).start();
                finish();
            }
        });
    }
}
