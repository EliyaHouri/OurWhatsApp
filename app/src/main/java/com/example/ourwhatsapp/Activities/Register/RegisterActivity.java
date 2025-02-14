package com.example.ourwhatsapp.Activities.Register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.MutableLiveData;

import com.example.ourwhatsapp.MainActivity;
import com.example.ourwhatsapp.Repositories.UserRepository;
import com.example.ourwhatsapp.Utils;
import com.example.ourwhatsapp.databinding.ActivityRegisterBinding;

import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserRepository userRepository;

    private String base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bitmap bm=((BitmapDrawable)binding.photoImageView.getDrawable()).getBitmap();
        base64 = Utils.imagePathToBase64(bm);

        ActivityResultLauncher<Intent> imagePickerActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        try {
                            assert result.getData() != null;
                            final Uri imageUri = result.getData().getData();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            binding.photoImageView.setImageBitmap(selectedImage);

                            base64 = Utils.imagePathToBase64(selectedImage);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Something went wrong, try again!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No image was picked!", Toast.LENGTH_LONG).show();
                    }
                }
        );

        userRepository = new UserRepository(getApplicationContext());

        binding.navToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
        });

        binding.photoImageView.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            imagePickerActivityResult.launch(galleryIntent);
        });

        binding.loginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isValidUsername(s.toString())) {
                    binding.loginUsername.setTextColor(Color.RED);
                } else {
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        // Night mode is enabled
                        binding.loginUsername.setTextColor(Color.WHITE);
                    } else {
                        // Night mode is disabled
                        binding.loginUsername.setTextColor(Color.BLACK);
                    }
                }
            }
        });

        binding.loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isValidPassword(s.toString())) {
                    binding.loginPassword.setTextColor(Color.RED);
                } else {
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        // Night mode is enabled
                        binding.loginPassword.setTextColor(Color.WHITE);
                    } else {
                        // Night mode is disabled
                        binding.loginPassword.setTextColor(Color.BLACK);
                    }
                }
            }
        });

        binding.displayName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isValidDisplayName(s.toString())) {
                    binding.displayName.setTextColor(Color.RED);
                } else {
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        // Night mode is enabled
                        binding.displayName.setTextColor(Color.WHITE);
                    } else {
                        // Night mode is disabled
                        binding.displayName.setTextColor(Color.BLACK);
                    }
                }
            }
        });

        binding.confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String password = binding.loginPassword.getText().toString();
                if (!(s.toString().equals(password))) {
                    binding.confirmPassword.setTextColor(Color.RED);
                } else {
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        // Night mode is enabled
                        binding.confirmPassword.setTextColor(Color.WHITE);
                    } else {
                        // Night mode is disabled
                        binding.confirmPassword.setTextColor(Color.BLACK);
                    }
                }
            }
        });


        binding.showListButton.setOnClickListener(view -> {
            String username = binding.loginUsername.getText().toString();
            String password = binding.loginPassword.getText().toString();
            String displayName = binding.displayName.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();


            // Validate the input fields
            if (Utils.isValidUsername(username)) {
                Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_LONG / 2).show();
                Toast.makeText(getApplicationContext(), "Username must be at least 6 chars, only letters and digits", Toast.LENGTH_LONG).show();
            } else if (Utils.isValidPassword(password)) {
                Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG / 2).show();
                Toast.makeText(getApplicationContext(), "at least 8 chars, 1+ uppercase & lowercase letter, digits and special char", Toast.LENGTH_LONG).show();
            } else if (Utils.isValidDisplayName(displayName)) {
                Toast.makeText(getApplicationContext(), "Invalid display name", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "must be at least 6 chars, only letters, digits and spaces", Toast.LENGTH_LONG).show();
            } else if (!(password.equals(confirmPassword))) {
                Toast.makeText(getApplicationContext(), "Your passwords do not match", Toast.LENGTH_LONG).show();
            } else {
                MutableLiveData<Integer> res = userRepository.tryRegister(username, password, displayName, base64);
                res.observe(this, code -> {
                    if (code == 200) {
                        finish();
                    } else if (code == 409) {
                        Toast.makeText(getApplicationContext(), "Username is already exists, try another one!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong, try again!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
