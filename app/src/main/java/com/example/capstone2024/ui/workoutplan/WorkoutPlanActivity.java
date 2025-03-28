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
import com.example.capstone2024.ui.workoutsession.WorkoutSessionActivity;

import java.util.HashSet;
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
        createCustomWorkout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get updated exercise session to reflect changes
        presenter.loadWorkoutProgram();
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
                intent.putExtra("WORKOUT_NAME", day);
                startActivity(intent);
            });

            // Add the card to the layout
            daysLayout.addView(dayCard);
        }
    }

    private void createCustomWorkout() {
        createCustomWorkoutButton.setOnClickListener(v -> {
            // Initialize helper
            if (helper == null) {
                helper = new UserSetupDatabaseHelper(WorkoutPlanActivity.this);
            }

            // Retrieve the workout program
            Map<String, WorkoutSessionWithExercises> program = helper.getStoredWorkoutProgram();

            // Get set of used day numbers
            HashSet<Integer> usedDays = new HashSet<>();
            for (String key : program.keySet()) {
                try {
                    int dayNum = Integer.parseInt(key.replace("Workout ", "").trim());
                    usedDays.add(dayNum);
                } catch (NumberFormatException ignored) {
                }
            }

            // Find the smallest integer not used
            int nextDay = 1;
            while (usedDays.contains(nextDay)) {
                nextDay++;
            }

            // Create a new workout session with the computed day number
            WorkoutSession newSession = new WorkoutSession(nextDay);
            helper.insertWorkoutSession(newSession);

            // Navigate directly to the WorkoutSessionActivity with the new day name
            Intent intent = new Intent(WorkoutPlanActivity.this, WorkoutSessionActivity.class);
            intent.putExtra("WORKOUT_NAME", "Workout " + nextDay);
            startActivity(intent);
        });
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}