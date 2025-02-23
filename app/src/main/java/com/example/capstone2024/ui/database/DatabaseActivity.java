package com.example.capstone2024.ui.database;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.capstone2024.R;
import com.example.capstone2024.models.UserSetup;
import com.example.capstone2024.models.UserSetupViewModel;

import java.util.List;

public class DatabaseActivity extends AppCompatActivity {
    private UserSetupViewModel userSetupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        userSetupViewModel = new ViewModelProvider(this).get(UserSetupViewModel.class);

        EditText nameInput = findViewById(R.id.nameInput);
        EditText ageInput = findViewById(R.id.ageInput);
        Button saveButton = findViewById(R.id.saveButton);
        TextView userList = findViewById(R.id.userList);

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String age = ageInput.getText().toString();

            if (!name.isEmpty() && !age.isEmpty()) {
                UserSetup newUser = new UserSetup();
                newUser.setName(name);
                newUser.setAge(age);
                userSetupViewModel.insertUser(newUser);

                nameInput.setText("");
                ageInput.setText("");
            }
        });

        // Observe and update the UI with LiveData
        userSetupViewModel.getAllUsers().observe(this, users -> {
            StringBuilder userData = new StringBuilder();
            for (UserSetup user : users) {
                userData.append("ID: ").append(user.getId())
                        .append(", Name: ").append(user.getName())
                        .append(", Age: ").append(user.getAge()).append("\n");
            }
            userList.setText(userData.toString());
        });
    }
}
