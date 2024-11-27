package com.example.capstone2024.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.WorkoutSession;

import java.util.List;

public class WorkoutSessionActivity extends AppCompatActivity {

    private String dayName;
    private WorkoutSession workoutSession;
    private LinearLayout exercisesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);

        exercisesLayout = findViewById(R.id.exercisesLayout);

        // Retrieve the data passed from WorkoutPlanActivity
        dayName = getIntent().getStringExtra("DAY_NAME");
        workoutSession = (WorkoutSession) getIntent().getSerializableExtra("WORKOUT_SESSION");

        if (workoutSession != null) {
            displayExercises();
        } else {
            // Handle the case when no workout session is received
            // You can show an error message or finish the activity
        }
    }

    private void displayExercises() {
        List<ExerciseSession> exerciseSessions = workoutSession.getExerciseSessions();

        for (ExerciseSession exerciseSession : exerciseSessions) {
            Exercise exercise = exerciseSession.getExercise();
            String name = exercise.getName();
            String category = exercise.getCategory();
            String primaryMuscles = exercise.getPrimaryMuscles().toString();
            int sets = exerciseSession.getSets();
            int restTime = exerciseSession.getRestTime();

            // Create a button for each exercise
            Button exerciseButton = new Button(this);
            exerciseButton.setText(name + "\n" + category + " | Sets: " + sets + ", Rest: " + restTime + " min");
            exerciseButton.setTextSize(16);
            exerciseButton.setPadding(0, 30, 0, 30); // Adjust padding as needed
            exerciseButton.setAllCaps(false);

            // Set OnClickListener for the exercise button
            exerciseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start ExerciseSessionActivity and pass the Exercise object
                    Intent intent = new Intent(WorkoutSessionActivity.this, ExerciseSessionActivity.class);
                    intent.putExtra("EXERCISE", exercise);
                    startActivity(intent);
                }
            });

            // Add the exercise button to the layout
            exercisesLayout.addView(exerciseButton);
        }
    }
}