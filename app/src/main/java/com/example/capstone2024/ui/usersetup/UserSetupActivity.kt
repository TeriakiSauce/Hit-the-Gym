package com.example.capstone2024.ui.usersetup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.UserSetupContract;
import com.example.capstone2024.presenters.UserSetupPresenter;
import com.example.capstone2024.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class UserSetupActivity extends AppCompatActivity implements UserSetupContract.View {
    private UserSetupContract.Presenter presenter;

    // UI components
    private EditText name;
    private EditText age;
    private EditText height;
    private EditText currentWeight;
    private EditText targetWeight;
    private RadioGroup workoutLevelGroup;
    private List<CheckBox> targetBodyParts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        // Initialize views
        name = findViewById(R.id.answer1);
        age = findViewById(R.id.answer2);
        height = findViewById(R.id.answer3);
        currentWeight = findViewById(R.id.answer4);
        targetWeight = findViewById(R.id.answer5);
        workoutLevelGroup = findViewById(R.id.answer6);

        targetBodyParts = new ArrayList<>();
        targetBodyParts.add(findViewById(R.id.answer7Biceps));
        targetBodyParts.add(findViewById(R.id.answer7Triceps));
        targetBodyParts.add(findViewById(R.id.answer7Chest));
        targetBodyParts.add(findViewById(R.id.answer7Abs));
        targetBodyParts.add(findViewById(R.id.answer7Shoulders));
        targetBodyParts.add(findViewById(R.id.answer7Glutes));
        targetBodyParts.add(findViewById(R.id.answer7Legs));

        Button submitButton = findViewById(R.id.submitButton);

        // Initialize presenter
        presenter = new UserSetupPresenter(this);

        // Submit button listener
        submitButton.setOnClickListener(v -> {
            // Collect input data
            String nameStr = name.getText().toString();
            String ageStr = age.getText().toString();
            String heightStr = height.getText().toString();
            String currentWeightStr = currentWeight.getText().toString();
            String targetWeightStr = targetWeight.getText().toString();

            // Get selected workout level
            int workoutLevelId = workoutLevelGroup.getCheckedRadioButtonId();
            RadioButton selectedWorkoutLevel = findViewById(workoutLevelId);
            String workoutLevelStr = selectedWorkoutLevel != null ? selectedWorkoutLevel.getText().toString() : "";

            // Get selected body parts
            List<String> selectedBodyParts = new ArrayList<>();
            for (CheckBox checkBox : targetBodyParts) {
                if (checkBox.isChecked()) {
                    selectedBodyParts.add(checkBox.getText().toString());
                }
            }

            // Delegate validation and submission to the presenter
            presenter.submitSurvey(nameStr, ageStr, heightStr, currentWeightStr, targetWeightStr, workoutLevelStr, selectedBodyParts);
        });
    }

    @Override
    public void showValidationError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(UserSetupActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}