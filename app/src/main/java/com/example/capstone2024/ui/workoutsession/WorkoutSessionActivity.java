package com.example.capstone2024.ui.workoutsession;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.WorkoutSessionContract;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.presenters.WorkoutSessionPresenter;
import com.example.capstone2024.ui.exercisesession.ExerciseSessionActivity;

import java.util.List;

public class WorkoutSessionActivity extends AppCompatActivity implements WorkoutSessionContract.View {
    private String dayName;
    private LinearLayout exercisesLayout;
    private WorkoutSessionContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);

        exercisesLayout = findViewById(R.id.exercisesLayout);

        // Initialize the presenter
        presenter = new WorkoutSessionPresenter(this);

        // Retrieve data from Intent
        dayName = getIntent().getStringExtra("DAY_NAME");
        Object workoutSessionData = getIntent().getSerializableExtra("WORKOUT_SESSION");

        // Load workout session
        presenter.loadWorkoutSession(dayName, workoutSessionData);
    }

    @Override
    public void displayExercises(List<ExerciseSession> exerciseSessions) {
        exercisesLayout.removeAllViews(); // Clear any existing views

        for (ExerciseSession exerciseSession : exerciseSessions) {
            Exercise exercise = exerciseSession.getExercise();
            String name = exercise.getName();
            int sets = exerciseSession.getSets();
            int reps = exerciseSession.getReps();
            int progressPercentage = calculateProgress(exerciseSession);

            // Inflate the custom layout
            View exerciseCard = getLayoutInflater().inflate(R.layout.exercise_card, exercisesLayout, false);

            // Bind data to views
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

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private int calculateProgress(ExerciseSession session) {
        int totalSets = session.getSets();
        int completedSets = session.getCompletedSets(); // Assuming this method exists
        return (int) ((completedSets / (float) totalSets) * 100);
    }
}