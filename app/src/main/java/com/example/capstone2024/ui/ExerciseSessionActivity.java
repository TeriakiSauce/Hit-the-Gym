package com.example.capstone2024.ui;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.capstone2024.R;
import com.example.capstone2024.models.Exercise;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ExerciseSessionActivity extends AppCompatActivity {

    private Exercise exercise;

    private TextView exerciseNameTextView;
    private ImageView exerciseImageView;
    private TextView exerciseInstructionsTextView;
    private TableLayout setsTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_session);

        // Initialize UI components
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView);
        exerciseImageView = findViewById(R.id.exerciseImageView);
        exerciseInstructionsTextView = findViewById(R.id.exerciseInstructionsTextView);
        setsTableLayout = findViewById(R.id.setsTableLayout);

        // Retrieve the Exercise object from the Intent
        exercise = (Exercise) getIntent().getSerializableExtra("EXERCISE");

        if (exercise != null) {
            displayExerciseDetails();
            setupSetsTable();
        } else {
            // Handle the case when no exercise data is received
            finish();
        }
    }

    private void displayExerciseDetails() {
        // Display exercise name
        exerciseNameTextView.setText(exercise.getName());

        // Set the local placeholder image
        exerciseImageView.setImageResource(R.drawable.placeholder_image);

        // Display exercise instructions
        displayInstructions();
    }

    private void displayInstructions() {
        List<String> instructions = exercise.getInstructions();
        if (instructions != null && !instructions.isEmpty()) {
            StringBuilder instructionsBuilder = new StringBuilder();
            for (int i = 0; i < instructions.size(); i++) {
                instructionsBuilder.append((i + 1) + ". " + instructions.get(i) + "\n\n");
            }
            exerciseInstructionsTextView.setText(instructionsBuilder.toString());
        } else {
            exerciseInstructionsTextView.setText("No instructions available.");
        }
    }

    private void setupSetsTable() {
        // Add table headers
        TableRow headerRow = new TableRow(this);

        TextView setTypeHeader = new TextView(this);
        setTypeHeader.setText("Set Type");
        setTypeHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(setTypeHeader);

        TextView repsHeader = new TextView(this);
        repsHeader.setText("Reps");
        repsHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(repsHeader);

        TextView weightHeader = new TextView(this);
        weightHeader.setText("Weight");
        weightHeader.setPadding(8, 8, 8, 8);
        headerRow.addView(weightHeader);

        setsTableLayout.addView(headerRow);

        // Add rows for each set (e.g., 4 sets)
        int numberOfSets = 4; // Adjust as needed
        for (int i = 0; i < numberOfSets; i++) {
            TableRow setRow = new TableRow(this);

            // Set Type input
            EditText setTypeInput = new EditText(this);
            setTypeInput.setHint("Set " + (i + 1));
            setTypeInput.setPadding(8, 8, 8, 8);
            setRow.addView(setTypeInput);

            // Reps input
            EditText repsInput = new EditText(this);
            repsInput.setHint("Reps");
            repsInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            repsInput.setPadding(8, 8, 8, 8);
            setRow.addView(repsInput);

            // Weight input
            EditText weightInput = new EditText(this);
            weightInput.setHint("Weight");
            weightInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            weightInput.setPadding(8, 8, 8, 8);
            setRow.addView(weightInput);

            setsTableLayout.addView(setRow);
        }
    }
}