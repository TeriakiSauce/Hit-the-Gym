package com.example.capstone2024.ui.workoutplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.WorkoutPlanContract;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.presenters.WorkoutPlanPresenter;
import com.example.capstone2024.ui.customworkout.CustomWorkoutActivity;
import com.example.capstone2024.ui.workoutsession.WorkoutSessionActivity;

import java.util.Map;

public class WorkoutPlanActivity extends AppCompatActivity implements WorkoutPlanContract.View {
    private LinearLayout daysLayout;
    private WorkoutPlanContract.Presenter presenter;
    private Button createCustomWorkoutButton;
    private UserSetupDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

        daysLayout = findViewById(R.id.daysLayout);

        createCustomWorkoutButton = findViewById(R.id.createCustomWorkoutButton);

        // Initialize the presenter
        presenter = new WorkoutPlanPresenter(this);

        // Load the workout program
        presenter.loadWorkoutProgram();

        // Set click listener for the "Create Custom Workout" button
        createCustomWorkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutPlanActivity.this, CustomWorkoutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void displayWorkoutProgram(Map<String, WorkoutSessionWithExercises> workoutProgram) {
        daysLayout.removeAllViews(); // Clear existing views
        for (Map.Entry<String, WorkoutSessionWithExercises> entry : workoutProgram.entrySet()) {
            String day = entry.getKey();
            WorkoutSessionWithExercises workoutSession = entry.getValue();

            // Inflate the custom card layout
            View dayCard = getLayoutInflater().inflate(R.layout.day_card, daysLayout, false);

            // Bind data to the views
            TextView dayNameText = dayCard.findViewById(R.id.dayName);
            dayNameText.setText(day);

            // Set OnClickListener for the card
            dayCard.setOnClickListener(v -> {
                Intent intent = new Intent(WorkoutPlanActivity.this, WorkoutSessionActivity.class);
                intent.putExtra("DAY_NAME", day);
                startActivity(intent);
            });

            // Add the card to the layout
            daysLayout.addView(dayCard);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}