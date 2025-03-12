package com.example.capstone2024.ui.workoutsession;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.WorkoutSessionContract;
import com.example.capstone2024.database.ExerciseSessionWithExercise;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.presenters.WorkoutSessionPresenter;
import com.example.capstone2024.ui.exercisesearch.ExerciseSearchActivity;
import com.example.capstone2024.ui.exercisesession.ExerciseSessionActivity;

import java.util.List;

public class WorkoutSessionActivity extends AppCompatActivity implements WorkoutSessionContract.View {
    private String dayName;
    private LinearLayout exercisesLayout;
    private WorkoutSessionContract.Presenter presenter;
    private Button addExerciseButton;
    private ActivityResultLauncher<Intent> exerciseSearchLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);

        exercisesLayout = findViewById(R.id.exercisesLayout);
        addExerciseButton = findViewById(R.id.addExerciseButton);

        // Initialize the exerciseSearchLauncher
        exerciseSearchLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ExerciseSession selectedExercise =
                                    (ExerciseSession) data.getSerializableExtra("SELECTED_EXERCISE");
                            if (selectedExercise != null) {
                                presenter.addExerciseSession(selectedExercise);
                            }
                        }
                    }
                }
        );

        // Set up the OnClickListener for the "Add Exercise" button
        addExerciseButton.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutSessionActivity.this, ExerciseSearchActivity.class);
            exerciseSearchLauncher.launch(intent);
        });

        // Initialize the presenter
        presenter = new WorkoutSessionPresenter(this);

        // Retrieve data from Intent
        dayName = getIntent().getStringExtra("DAY_NAME");

        // Load workout session
        presenter.loadWorkoutSession(dayName);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get updated exercise session to reflect changes
        presenter.loadWorkoutSession(dayName);
    }

    @Override
    public void displayExercises(List<ExerciseSessionWithExercise> exerciseSessions) {
        // Calculate overall progress of the workout
        int overallTotalSets = 0;
        int overallCompletedSets = 0;
        for (ExerciseSessionWithExercise session : exerciseSessions) {
            overallTotalSets += session.getSets();
            overallCompletedSets += session.getCompletedSets();
        }
        int overallProgressPercentage = overallTotalSets == 0 ? 0 : Math.round((overallCompletedSets * 100f) / overallTotalSets);

        // Update the total progress bar
        ProgressBar overallProgressBar = findViewById(R.id.overallProgressBar);
        overallProgressBar.setProgress(overallProgressPercentage);

        exercisesLayout.removeAllViews(); // Clear any existing views

        for (ExerciseSessionWithExercise exerciseSession : exerciseSessions) {
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
            TextView exerciseCategory = exerciseCard.findViewById(R.id.exerciseCategory);
            TextView exercisePrimaryMuscles = exerciseCard.findViewById(R.id.exercisePrimaryMuscles);
            TextView progressText = exerciseCard.findViewById(R.id.progressText);

            // Set the text for the views
            exerciseName.setText(name);
            exerciseDetails.setText("Sets: " + sets + " | Reps: " + reps);
            exerciseCategory.setText("Category: " + capitalizeFirstLetter(exercise.getCategory()));
            exercisePrimaryMuscles.setText("Primary Muscle: " +  capitalizeFirstLetter(exercise.getPrimaryMuscles()));
            progressText.setText(progressPercentage + "%");

            // If 100% complete, set a green outline, otherwise use default
            exerciseCard.setActivated(progressPercentage == 100);

            // Bind the remove button
            Button removeButton = exerciseCard.findViewById(R.id.removeExerciseButton);
            removeButton.setOnClickListener(v -> {
                // Call the presenter method to remove the exercise session
                presenter.removeExerciseSession(exerciseSession.getId());
            });

            // Set the OnClickListener for the card
            exerciseCard.setOnClickListener(v -> {
                Intent intent = new Intent(WorkoutSessionActivity.this, ExerciseSessionActivity.class);
                intent.putExtra("EXERCISE_ID", exerciseSession.getId());
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

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private int calculateProgress(ExerciseSessionWithExercise session) {
        int totalSets = session.getSets();
        int completedSets = session.getCompletedSets();
        return (int) ((completedSets / (float) totalSets) * 100);
    }
}