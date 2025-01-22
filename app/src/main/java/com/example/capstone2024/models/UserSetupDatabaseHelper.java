package com.example.capstone2024.models;

import android.content.Context;
import androidx.room.Room;

import java.util.List;

public class UserSetupDatabaseHelper {

    private UserSetupDatabase userSetupDatabase;

    public UserSetupDatabaseHelper(Context context) {
        userSetupDatabase = Room.databaseBuilder(context, UserSetupDatabase.class, "user setup-database")
                .allowMainThreadQueries()  // Allowing main thread queries (use with caution)
                .build();
    }

    // Insert a user
    public void insertUser(UserSetup user) {
        userSetupDatabase.userSetupDao().insert(user);
    }

    // Get all users
    public List<UserSetup> getAllUsers() {
        return userSetupDatabase.userSetupDao().getAllUsers();
    }

    // Get a user by ID
    public UserSetup getUserById(int userId) {
        return userSetupDatabase.userSetupDao().getUserById(userId);
    }

    // Delete a user by ID
    public void deleteUserById(int userId) {
        userSetupDatabase.userSetupDao().deleteUserById(userId);
    }
}