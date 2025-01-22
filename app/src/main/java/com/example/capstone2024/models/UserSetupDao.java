package com.example.capstone2024.models;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;
import java.util.List;

@Dao
public interface UserSetupDao {
    @Insert
    void insert(UserSetup user);

    @Update
    void update(UserSetup user);

    @Delete
    void delete(UserSetup user);

    @Query("SELECT * FROM user_setup")
    List<UserSetup> getAllUsers();

    @Query("SELECT * FROM user_setup WHERE id = :userId")
    UserSetup getUserById(int userId);

    @Query("DELETE FROM user_setup WHERE id = :userId")
    void deleteUserById(int userId);
}