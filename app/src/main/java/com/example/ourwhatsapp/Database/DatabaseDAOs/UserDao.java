package com.example.ourwhatsapp.Database.DatabaseDAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ourwhatsapp.Database.Entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    User get(int id);

    @Query("SELECT * FROM user WHERE chatID != 0 ORDER BY lastMassageSendingTime DESC")
    List<User> getChats();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);

    @Query("DELETE FROM user WHERE chatID = :chatID")
    void deleteByChatID(String chatID);

}
