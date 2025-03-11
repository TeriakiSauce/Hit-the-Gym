package com.example.capstone2024.models;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

public class UserSetupDatabaseHelper {

    private UserSetupDao userDao;

    public UserSetupDatabaseHelper(Context context) {
        // Initialize DAO by getting it from the AppDatabase instance
        UserSetupDatabase db = UserSetupDatabaseClient.getInstance(context).getAppDatabase();
        userDao = db.userSetupDao();
    }

    public void insertUser(UserSetup user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDao.insert(user);
            }
        }).start(); // Use a background thread to insert data into the database
    }

    public void getAllUsers(final OnUsersLoadedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<UserSetup> users = userDao.getAllUsers();  // Fetch users in the background
                // Pass the result to the listener on the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onUsersLoaded(users);  // Notify listener with the fetched users
                    }
                });
            }
        }).start();
    }

    public interface OnUsersLoadedListener {
        void onUsersLoaded(List<UserSetup> users);
    }

}

