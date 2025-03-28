package com.example.capstone2024.ui.usersetup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.UserSetupContract;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.models.UserSetup;
import com.example.capstone2024.presenters.UserSetupPresenter;
import com.example.capstone2024.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class UserStatusActivity extends AppCompatActivity implements UserSetupContract.View {
    private UserSetupContract.Presenter presenter;
    private UserSetup user;
    private UserSetupDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_status);

        // Initialize user
        databaseHelper = new UserSetupDatabaseHelper(this);
        user = databaseHelper.fetchAllUsersSync().get(0);

        // Display current User Status
        TextView nameText = findViewById(R.id.statusFullName);
        nameText.setText("Full Name: " + user.getName());

        TextView ageText = findViewById(R.id.statusAge);
        ageText.setText("Age: " + user.getAge() + " years old");

        TextView weightText = findViewById(R.id.statusWeight);
        weightText.setText("Current weight: " + user.getCurrentWeight() + " lb");

        TextView targetWeightText = findViewById(R.id.statusTargetWeight);
        targetWeightText.setText("Target weight: " + user.getTargetWeight() + " lb");

        TextView workoutLevelText = findViewById(R.id.statusWorkoutLevel);
        workoutLevelText.setText("Experience level: " + user.getWorkoutLevel());

        TextView bodyPartsText = findViewById(R.id.statusBodyParts);
        StringBuilder bodyPartsString = new StringBuilder("Target Body Parts: \n\n");
        for (String bodypart: user.getTargetBodyParts()){
            bodyPartsString.append(bodypart + "\n");
        }
        bodyPartsString.deleteCharAt(bodyPartsString.length() - 1);
        bodyPartsText.setText(bodyPartsString);

        TextView equipmentText = findViewById(R.id.statusEquipment);
        equipmentText.setText("Equipment available: " + user.getEquipment());

    }

    @Override
    public void showValidationError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(UserStatusActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}