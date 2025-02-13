package com.example.capstone2024.ui.exercisesearch;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.WorkoutPlan;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ExerciseSearchActivity extends AppCompatActivity {
    private EditText searchInput;
    private LinearLayout exercisesListLayout;
    private ImageView clearSearchButton;
    private List<Exercise> allExercises; // Stores all exercises
    private List<Exercise> filteredExercises; // Stores filtered exercises

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_search);

        searchInput = findViewById(R.id.searchInput);
        clearSearchButton = findViewById(R.id.clearSearchButton);
        exercisesListLayout = findViewById(R.id.exercisesListLayout);

        // Fetch all exercises
        try {
            allExercises = fetchExercisesFromDatabase();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        filteredExercises = new ArrayList<>(allExercises); // Initialize with all exercises

        // Populate initial list
        displayExercises(filteredExercises);

        // Add search functionality
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterExercises(s.toString());
                if (s.length() > 0) {
                    clearSearchButton.setVisibility(View.VISIBLE); // Show button
                } else {
                    clearSearchButton.setVisibility(View.GONE); // Hide button
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Clear button functionality
        clearSearchButton.setOnClickListener(v -> {
            searchInput.setText(""); // Clear text
            filterExercises(""); // Reset exercise list
            clearSearchButton.setVisibility(View.GONE); // Hide button
        });
    }

    private void displayExercises(List<Exercise> exercises) {
        exercisesListLayout.removeAllViews(); // Clear previous views

        for (Exercise exercise : exercises) {
            View exerciseCard = getLayoutInflater().inflate(R.layout.exercise_search_card, exercisesListLayout, false);

            TextView exerciseName = exerciseCard.findViewById(R.id.exerciseName);
            Button addButton = exerciseCard.findViewById(R.id.addExerciseButton);

            exerciseName.setText(exercise.getName());

            addButton.setOnClickListener(v -> {
                ExerciseSession selectedExercise = new ExerciseSession(exercise, 4, 1, 10);
                Intent intent = new Intent();
                intent.putExtra("SELECTED_EXERCISE", selectedExercise);
                setResult(RESULT_OK, intent);
                finish();
            });

            exercisesListLayout.addView(exerciseCard);
        }
    }

    private void filterExercises(String query) {
        filteredExercises.clear();

        for (Exercise exercise : allExercises) {
            if (exercise.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredExercises.add(exercise);
            }
        }

        displayExercises(filteredExercises);
    }


    private List<Exercise> fetchExercisesFromDatabase() throws JSONException, IOException {
        // WIth no database yet, we will need to create a list of exercises by creating a temporary workout plan
        Context context = getApplicationContext();
        InputStream exercisesInputStream = context.getAssets().open("exercises.json");
        WorkoutPlan workoutPlan = new WorkoutPlan(exercisesInputStream);
        return workoutPlan.getExercisesList();
    }
}