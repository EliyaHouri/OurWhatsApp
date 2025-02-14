package com.example.ourwhatsapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ourwhatsapp.Database.DatabaseDAOs.UserDao;
import com.example.ourwhatsapp.Database.DatabaseDAOs.MessagesDao;
import com.example.ourwhatsapp.Database.DatabaseDAOs.SettingsDao;

import com.example.ourwhatsapp.Database.Entities.User;
import com.example.ourwhatsapp.Database.Entities.Messages;
import com.example.ourwhatsapp.Database.Entities.Settings;

@Database(entities = {User.class, Messages.class, Settings.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract MessagesDao messageDao();
    public abstract SettingsDao settingsDao();

    private static String token;
    private static String username;

    public static String getToken() {
        return token;
    }

    public static String getUsername() {
        return username;
    }

    public static void setToken(String token) {
        AppDatabase.token = "Bearer " + token;
    }

    public static void setUsername(String username) {
        AppDatabase.username = username;
    }

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "OurWhatsappDB")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
