package com.example.capstone2024.models;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class UserSetupRepository {
    private UserSetupDao userSetupDao;
    private LiveData<List<UserSetup>> allUsers;

    // Create an ExecutorService for background execution
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserSetupRepository(Application application) {
        UserSetupDatabase db = UserSetupDatabase.getDatabase(application);
        userSetupDao = db.userSetupDao();
        allUsers = userSetupDao.getAllUsers();
    }

    // Insert operation in the background
    public void insert(UserSetup userSetup) {
        executorService.execute(() -> userSetupDao.insert(userSetup));
    }

    // Update operation in the background
    public void update(UserSetup userSetup) {
        executorService.execute(() -> userSetupDao.update(userSetup));
    }

    // Delete operation in the background
    public void delete(UserSetup userSetup) {
        executorService.execute(() -> userSetupDao.delete(userSetup));
    }

    // Fetch all users (LiveData runs on a background thread automatically)
    public LiveData<List<UserSetup>> getAllUsers() {
        return allUsers;
    }
}
