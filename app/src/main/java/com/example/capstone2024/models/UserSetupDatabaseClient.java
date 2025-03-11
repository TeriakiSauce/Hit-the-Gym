package com.example.capstone2024.models;

import android.content.Context;

import androidx.room.Room;

public class UserSetupDatabaseClient {
    private static UserSetupDatabaseClient instance;
    private UserSetupDatabase appDatabase;

    // Private constructor to prevent instantiation
    private UserSetupDatabaseClient(Context context) {
        appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        UserSetupDatabase.class, "MyDatabase")
                .fallbackToDestructiveMigration()
                .build();
    }
    public static synchronized UserSetupDatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new UserSetupDatabaseClient(context);
        }
        return instance;
    }
    public UserSetupDatabase getAppDatabase() {
        return appDatabase;
    }
}

