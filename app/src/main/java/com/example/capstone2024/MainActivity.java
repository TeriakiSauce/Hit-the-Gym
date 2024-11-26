package com.example.capstone2024;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements WorkoutProgramView {

    private WorkoutProgramPresenter presenter;
    private TextView programTextView;

    private Button startWorkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        programTextView = findViewById(R.id.programTextView);
        startWorkoutButton = findViewById(R.id.startWorkoutButton);

        // Initialize the Model
        WorkoutProgramGenerator model = null;
        try {
            InputStream exercisesInputStream = getAssets().open("exercises.json");
            model = new WorkoutProgramGenerator(exercisesInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load exercises data.");
        }

        if (model != null) {
            // Initialize the Presenter
            presenter = new WorkoutProgramPresenter(this, model);

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

            // Ask Presenter to generate and display the workout program
            presenter.generateAndDisplayWorkoutProgram(userInput);

            startWorkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the WorkoutActivity
                    Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
                    String workoutProgram = programTextView.getText().toString();
                    intent.putExtra("WORKOUT_PROGRAM", workoutProgram);
                    startActivity(intent);
                }
            });
        }
    }

    // Implementing WorkoutProgramView methods
    @Override
    public void displayWorkoutProgram(String program) {
        programTextView.setText(program);
    }

    @Override
    public void showError(String message) {
        programTextView.setText(message);
    }
}
