package com.example.capstone2024.ui.exercisesearch;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExerciseSearchActivity extends AppCompatActivity {
    private EditText searchInput;
    private LinearLayout exercisesListLayout;
    private ImageView clearSearchButton;
    private UserSetupDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_search);

        searchInput = findViewById(R.id.searchInput);
        clearSearchButton = findViewById(R.id.clearSearchButton);
        exercisesListLayout = findViewById(R.id.exercisesListLayout);

        // Initialize database helper
        helper = new UserSetupDatabaseHelper(getApplicationContext());

        // Load initial list of exercises (returns all exercises)
        performSearch("");

        // Add text watcher for search input
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                if (query.isEmpty()) {
                    clearSearchButton.setVisibility(View.GONE);
                } else {
                    clearSearchButton.setVisibility(View.VISIBLE);
                }
                performSearch(query);
            }
            @Override
            public void afterTextChanged(Editable s) {
}
        });

        // Clears text and resets search results.
        clearSearchButton.setOnClickListener(v -> {
            searchInput.setText("");
            performSearch("");
            clearSearchButton.setVisibility(View.GONE);
        });
    }

    // Search for exercises based on the query asynchronously
    private void performSearch(String query) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            // This method should query the database using the DAO's searchExercises method.
            List<Exercise> searchResults = helper.searchExercises(query);
            mainHandler.post(() -> displayExercises(searchResults));
        });
        executor.shutdown();
    }

    // Display the search results as cards
    private void displayExercises(List<Exercise> exercises) {
        exercisesListLayout.removeAllViews();
        if (exercises == null || exercises.isEmpty()) {
            Toast.makeText(this, "No exercises found.", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Exercise exercise : exercises) {
            View exerciseCard = getLayoutInflater().inflate(R.layout.exercise_search_card, exercisesListLayout, false);
            TextView exerciseName = exerciseCard.findViewById(R.id.exerciseName);
            Button addButton = exerciseCard.findViewById(R.id.addExerciseButton);
            exerciseName.setText(exercise.getName());
            addButton.setOnClickListener(v -> {
                // Create an ExerciseSession with default values.
                // The workout_session_id here is set to 1 as a placeholder.
                ExerciseSession selectedExercise = new ExerciseSession(1, exercise.getId(), 4, 1, 10);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SELECTED_EXERCISE", selectedExercise);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            });
            exercisesListLayout.addView(exerciseCard);
        }
    }
}