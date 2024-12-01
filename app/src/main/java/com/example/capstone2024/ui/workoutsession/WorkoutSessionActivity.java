package com.example.capstone2024.ui.workoutsession;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.ui.exercisesession.ExerciseSessionActivity;

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
            int sets = exerciseSession.getSets();
            int reps = exerciseSession.getReps(); // Assuming reps exist
            int progressPercentage = calculateProgress(exerciseSession); // Custom function for progress

            // Inflate the custom layout
            View exerciseCard = getLayoutInflater().inflate(R.layout.exercise_card, exercisesLayout, false);

            // Bind the data to the views
            TextView exerciseName = exerciseCard.findViewById(R.id.exerciseName);
            TextView exerciseDetails = exerciseCard.findViewById(R.id.exerciseDetails);
            ProgressBar progressBar = exerciseCard.findViewById(R.id.progressBar);
            TextView progressText = exerciseCard.findViewById(R.id.progressText);

            exerciseName.setText(name);
            exerciseDetails.setText("Sets: " + sets + " | Reps: " + reps);
            progressBar.setProgress(progressPercentage);
            progressText.setText(progressPercentage + "%");

            // Set the OnClickListener for the card
            exerciseCard.setOnClickListener(v -> {
                Intent intent = new Intent(WorkoutSessionActivity.this, ExerciseSessionActivity.class);
                intent.putExtra("EXERCISE", exercise);
                startActivity(intent);
            });

            // Add the card to the layout
            exercisesLayout.addView(exerciseCard);
        }
    }

    private int calculateProgress(ExerciseSession session) {
        // Placeholder for progress calculation logic
        int totalSets = session.getSets();
        int completedSets = session.getCompletedSets(); // Assuming you have a way to track completed sets
        return (int) ((completedSets / (float) totalSets) * 100);
    }
}