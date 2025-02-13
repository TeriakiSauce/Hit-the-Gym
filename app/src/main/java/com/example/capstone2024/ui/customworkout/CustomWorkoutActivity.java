package com.example.capstone2024.ui.customworkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.CustomWorkoutContract;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.presenters.CustomWorkoutPresenter;
import com.example.capstone2024.ui.exercisesearch.ExerciseSearchActivity;

import java.util.List;

public class CustomWorkoutActivity extends AppCompatActivity implements CustomWorkoutContract.View {
    private LinearLayout exercisesLayout;
    private Button addExerciseButton, saveWorkoutButton;
    private CustomWorkoutContract.Presenter presenter;

    private static final int REQUEST_EXERCISE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout);

        exercisesLayout = findViewById(R.id.exercisesLayout);
        addExerciseButton = findViewById(R.id.addExerciseButton);
        saveWorkoutButton = findViewById(R.id.saveWorkoutButton);

        presenter = new CustomWorkoutPresenter(this);
        presenter.loadWorkout();

        // Listener for "Add Exercise" button
        addExerciseButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseSearchActivity.class);
            startActivityForResult(intent, REQUEST_EXERCISE);
        });
        // Listener for "Save Workout" button (Goes Back for Now)
        saveWorkoutButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXERCISE && resultCode == RESULT_OK) {
            ExerciseSession selectedExercise = (ExerciseSession) data.getSerializableExtra("SELECTED_EXERCISE");
            if (selectedExercise != null) {
                presenter.addExercise(selectedExercise);
            }
        }
    }

    @Override
    public void displayExercises(List<ExerciseSession> exercises) {
        exercisesLayout.removeAllViews();
        for (ExerciseSession exerciseSession : exercises) {
            View exerciseCard = getLayoutInflater().inflate(R.layout.exercise_card, exercisesLayout, false);

            TextView exerciseName = exerciseCard.findViewById(R.id.exerciseName);
            exerciseName.setText(exerciseSession.getExercise().getName());

            exercisesLayout.addView(exerciseCard);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
