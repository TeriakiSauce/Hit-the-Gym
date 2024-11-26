package com.example.capstone2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WorkoutActivity extends AppCompatActivity {

    private TextView workoutDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workoutDetailsTextView = findViewById(R.id.workoutDetailsTextView);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String workoutProgram = intent.getStringExtra("WORKOUT_PROGRAM");

        // Display the workout program
        workoutDetailsTextView.setText(workoutProgram);
    }
}