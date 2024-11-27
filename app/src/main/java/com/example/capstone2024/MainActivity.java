package com.example.capstone2024;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.ui.WorkoutPlanActivity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private WorkoutPlan workoutPlan;
    private TextView programTextView;
    private Button startWorkoutButton;

    private Map<String, WorkoutSession> workoutProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        programTextView = findViewById(R.id.programTextView);
        startWorkoutButton = findViewById(R.id.startWorkoutButton);

        // Initialize the WorkoutPlan
        try {
            InputStream exercisesInputStream = getAssets().open("exercises.json");
            workoutPlan = new WorkoutPlan(exercisesInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load exercises data.");
            return; // Exit if data loading fails
        }

        // User input
        Map<String, Object> userInput = new HashMap<>();
        userInput.put("age", 30);
        userInput.put("height", 175);
        userInput.put("weight", 70);
        userInput.put("target_weight", 65);
        userInput.put("workout_days", 5);
        userInput.put("level", "intermediate");
        userInput.put("equipment", "dumbbell");
        userInput.put("availability", 1.5);

        // Generate the workout program
        workoutProgram = workoutPlan.generateWorkoutProgram(userInput);

        // Display the workout program
        displayWorkoutProgram();

        // Set OnClickListener for the button
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start the WorkoutActivity
                Intent intent = new Intent(MainActivity.this, WorkoutPlanActivity.class);
                // Pass the workoutProgram map
                intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
                startActivity(intent);
            }
        });
    }

    private void displayWorkoutProgram() {
        StringBuilder programDisplay = new StringBuilder();
        for (Map.Entry<String, WorkoutSession> entry : workoutProgram.entrySet()) {
            String day = entry.getKey();
            WorkoutSession workoutSession = entry.getValue();
            List<ExerciseSession> exerciseSessions = workoutSession.getExerciseSessions();

            programDisplay.append("\n").append(day).append(":\n");
            for (ExerciseSession exerciseSession : exerciseSessions) {
                Exercise exercise = exerciseSession.getExercise();
                String name = exercise.getName();
                String category = exercise.getCategory();
                List<String> primaryMuscles = exercise.getPrimaryMuscles();

                programDisplay.append("  - ").append(name)
                        .append(" (").append(category).append(") ")
                        .append(primaryMuscles.toString())
                        .append(" | Sets: ").append(exerciseSession.getSets())
                        .append(", Rest: ").append(exerciseSession.getRestTime()).append(" min\n");
            }
        }

        programTextView.setText(programDisplay.toString());
    }

    private void showError(String message) {
        programTextView.setText(message);
    }
}
