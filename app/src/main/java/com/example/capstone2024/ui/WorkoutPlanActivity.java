package com.example.capstone2024.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.models.WorkoutSession;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class WorkoutPlanActivity extends AppCompatActivity {

    private Map<String, WorkoutSession> workoutProgram;
    private LinearLayout daysLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

        daysLayout = findViewById(R.id.daysLayout);

        // Retrieve the workoutProgram from the Intent
        workoutProgram = (HashMap<String, WorkoutSession>) getIntent().getSerializableExtra("WORKOUT_PROGRAM");

        if (workoutProgram != null) {
            displayWorkoutProgram();
        } else {
            // Handle the case when no workout program is received
            showError("No workout program received.");
        }
    }

    private void displayWorkoutProgram() {
        for (Map.Entry<String, WorkoutSession> entry : workoutProgram.entrySet()) {
            String day = entry.getKey();
            WorkoutSession workoutSession = entry.getValue();

            // Create a button for the day
            Button dayButton = new Button(this);
            dayButton.setText(day);
            dayButton.setTextSize(18);
            dayButton.setPadding(0, 50, 0, 50); // Make the button large
            dayButton.setAllCaps(false);

            // Set OnClickListener for the day button to start WorkoutSessionActivity
            dayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start WorkoutSessionActivity and pass the day's exercises
                    Intent intent = new Intent(WorkoutPlanActivity.this, WorkoutSessionActivity.class);
                    intent.putExtra("DAY_NAME", day);
                    intent.putExtra("WORKOUT_SESSION", workoutSession);
                    startActivity(intent);
                }
            });

            // Add the day button to the main layout
            daysLayout.addView(dayButton);
        }
    }

    private void showError(String message) {
        // Handle error display as needed
        // For simplicity, you can use a Toast or add a TextView to the layout
    }
}