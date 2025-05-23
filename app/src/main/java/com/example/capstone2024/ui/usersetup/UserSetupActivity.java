package com.example.capstone2024.ui.usersetup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.UserSetupContract;
import com.example.capstone2024.models.UserSetup;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.presenters.UserSetupPresenter;
import com.example.capstone2024.ui.home.HomeActivity;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

public class UserSetupActivity extends AppCompatActivity implements UserSetupContract.View {
    private UserSetupContract.Presenter presenter;
    private UserSetup userSetup;
    private UserSetupDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        // Initialize model
        userSetup = new UserSetup();

        // Initialize database
        databaseHelper = new UserSetupDatabaseHelper(this);

        // Initialize age spinner dropdown question
        ageSpinnerSetup();

        // Initialize other questions
        EditText name = findViewById(R.id.answer1);
        EditText currentWeight = findViewById(R.id.answer3);
        EditText targetWeight = findViewById(R.id.answer4);
        RadioGroup workoutLevelGroup = findViewById(R.id.answer5);
        RadioGroup equipmentGroup = findViewById(R.id.answer7);

        List<CheckBox> targetBodyParts = new ArrayList<>();
        targetBodyParts.add(findViewById(R.id.answer6Biceps));
        targetBodyParts.add(findViewById(R.id.answer6Triceps));
        targetBodyParts.add(findViewById(R.id.answer6Forearms));
        targetBodyParts.add(findViewById(R.id.answer6Chest));
        targetBodyParts.add(findViewById(R.id.answer6Abs));
        targetBodyParts.add(findViewById(R.id.answer6Shoulders));
        targetBodyParts.add(findViewById(R.id.answer6Back));
        targetBodyParts.add(findViewById(R.id.answer6Glutes));
        targetBodyParts.add(findViewById(R.id.answer6Quads));
        targetBodyParts.add(findViewById(R.id.answer6Hamstrings));
        targetBodyParts.add(findViewById(R.id.answer6Calves));
        targetBodyParts.add(findViewById(R.id.answer6Hips));

        // Set all body parts to true by default
        for (CheckBox checkBox:targetBodyParts){
            checkBox.setChecked(true);
        }

        Button submitButton = findViewById(R.id.submitButton);

        // Initialize presenter
        presenter = new UserSetupPresenter(this);

        // Submit button listener
        submitButton.setOnClickListener(v -> {
            // Collect input data
            userSetup.setName(name.getText().toString());
            userSetup.setCurrentWeight(currentWeight.getText().toString());
            userSetup.setTargetWeight(targetWeight.getText().toString());

            // Get selected workout level
            int workoutLevelId = workoutLevelGroup.getCheckedRadioButtonId();
            RadioButton selectedWorkoutLevel = findViewById(workoutLevelId);
            userSetup.setWorkoutLevel(selectedWorkoutLevel != null ? selectedWorkoutLevel.getText().toString() : "");

            // Get selected body parts
            List<String> selectedBodyParts = new ArrayList<>();
            for (CheckBox checkBox : targetBodyParts) {
                if (checkBox.isChecked()) {
                    selectedBodyParts.add(checkBox.getText().toString());
                }
            }
            userSetup.setTargetBodyParts(selectedBodyParts);

            // Get selected equipment access
            int equipmentId = equipmentGroup.getCheckedRadioButtonId();
            RadioButton selectedEquipment = findViewById(equipmentId);
            userSetup.setEquipment(selectedEquipment != null ? selectedEquipment.getText().toString() : "");

            // Update Database
            databaseHelper.insertUser(userSetup);

            // Delegate validation and submission to the presenter
            presenter.submitSurvey(userSetup.getName(), userSetup.getAge(),
                    userSetup.getCurrentWeight(), userSetup.getTargetWeight(),
                    userSetup.getWorkoutLevel(), userSetup.getTargetBodyParts(), userSetup.getEquipment());
        });
    }

    public void ageSpinnerSetup() {
        Spinner spinnerAnswer = findViewById(R.id.answer2);
        int sizeOfNum = 100;

        // Create an array of numbers
        String[] numbers = new String[sizeOfNum+1];
        for (int i = 0; i <= sizeOfNum; i++) {
            numbers[i] = String.valueOf(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, numbers) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the default TextView for the selected item
                TextView view = (TextView) super.getView(position, convertView, parent);
                // Change the text color for the selected item
                view.setTextColor(Color.GRAY);  // Set your preferred color here
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        spinnerAnswer.setAdapter(adapter);

        // Handle item selection
        spinnerAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedNumber = parentView.getItemAtPosition(position).toString();
                userSetup.setAge(selectedNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                userSetup.setAge("0");
            }
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