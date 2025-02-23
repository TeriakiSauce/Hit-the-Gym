package com.example.capstone2024.models;

import java.util.List;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserSetupRepository {
    private UserSetupDao userSetupDao;
    private LiveData<List<UserSetup>> allUsers;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserSetupRepository(Application application) {
        UserSetupDatabase db = UserSetupDatabase.getDatabase(application);
        userSetupDao = db.userSetupDao();
        allUsers = userSetupDao.getAllUsers();
    }

    public void insert(UserSetup userSetup) {
        executorService.execute(() -> userSetupDao.insert(userSetup));
    }

    public LiveData<List<UserSetup>> getAllUsers() {
        return allUsers;
    }
}
