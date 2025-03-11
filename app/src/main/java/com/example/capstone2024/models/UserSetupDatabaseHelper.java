package com.example.capstone2024.models;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserSetupDatabaseHelper {

    private UserSetupDao userDao;
    private Context context;
    public UserSetupDatabaseHelper(Context context) {
        // Initialize DAO by getting it from the AppDatabase instance
        UserSetupDatabase db = UserSetupDatabaseClient.getInstance(context).getAppDatabase();
        userDao = db.userSetupDao();
        this.context = context;
    }

    public void insertUser(UserSetup user) {
        new Thread(() -> userDao.insert(user)).start(); // Use a background thread to insert data into the database
    }

    public List<UserSetup> getAllUsersSync() {
        return userDao.getAllUsers();
    }

    public List<UserSetup> fetchAllUsersSync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<UserSetup>> future = executor.submit(() -> {
            // Use the synchronous DAO method defined in your helper
            UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(context);
            return helper.getAllUsersSync();
        });

        try {
            // This will block until the result is available, but it's off the main thread.
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }
}

