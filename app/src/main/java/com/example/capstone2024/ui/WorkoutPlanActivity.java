package com.example.capstone2024.ui;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;

import org.json.JSONException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutPlanActivity extends AppCompatActivity {

    private WorkoutPlan workoutPlan;
    private TextView programTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

        programTextView = findViewById(R.id.programTextView);

        // Initialize the WorkoutPlan
        try {
            InputStream exercisesInputStream = getAssets().open("exercises.json");
            workoutPlan = new WorkoutPlan(exercisesInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            programTextView.setText("Failed to load exercises data.");
            return;
        }

        // User input
        Map<String, Object> userInput = getUserInput(); // You can define this method or hardcode values

        // Generate workout program
        Map<String, WorkoutSession> workoutProgram = null;
        try {
            workoutProgram = workoutPlan.generateWorkoutProgram(userInput);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Display the workout program
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

    private Map<String, Object> getUserInput() {
        // You can get this data from user input fields
        // For now, we'll use hardcoded values
        Map<String, Object> userInput = new HashMap<>();
        userInput.put("age", 30);
        userInput.put("height", 175);
        userInput.put("weight", 70);
        userInput.put("target_weight", 65);
        userInput.put("workout_days", 5);
        userInput.put("level", "intermediate");
        userInput.put("equipment", "dumbbell");
        userInput.put("availability", 1.5);
        return userInput;
    }
}