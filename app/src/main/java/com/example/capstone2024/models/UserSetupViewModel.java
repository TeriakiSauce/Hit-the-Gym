package com.example.capstone2024.models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class UserSetupViewModel extends AndroidViewModel {
    private UserSetupRepository repository;
    private LiveData<List<UserSetup>> allUsers;

    public UserSetupViewModel(Application application) {
        super(application);
        repository = new UserSetupRepository(application);
        allUsers = repository.getAllUsers();
    }

    public void insertUser(UserSetup user) {
        repository.insert(user);
    }

    public LiveData<List<UserSetup>> getAllUsers() {
        return allUsers;
    }
}
