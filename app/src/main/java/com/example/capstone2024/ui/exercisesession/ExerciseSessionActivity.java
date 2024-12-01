package com.example.capstone2024.ui.exercisesession;


import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.ExerciseSessionContract;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.presenters.ExerciseSessionPresenter;

public class ExerciseSessionActivity extends AppCompatActivity implements ExerciseSessionContract.View {
    private TextView exerciseNameTextView;
    private ImageView exerciseImageView;
    private TextView exerciseInstructionsTextView;
    private TableLayout setsTableLayout;

    private ExerciseSessionContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_session);

        // Initialize UI components
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView);
        exerciseImageView = findViewById(R.id.exerciseImageView);
        exerciseInstructionsTextView = findViewById(R.id.exerciseInstructionsTextView);
        setsTableLayout = findViewById(R.id.setsTableLayout);

        // Initialize presenter
        presenter = new ExerciseSessionPresenter(this);

        // Load exercise data from Intent
        Exercise exercise = (Exercise) getIntent().getSerializableExtra("EXERCISE");

        presenter.loadExerciseSession(exercise);
    }

    @Override
    public void displayExerciseDetails(String name, int imageResource, String instructions) {
        exerciseNameTextView.setText(name);
        exerciseImageView.setImageResource(imageResource);
        exerciseInstructionsTextView.setText(instructions);
    }

    @Override
    public void setupSetsTable(int numberOfSets) {
        setsTableLayout.removeAllViews(); // Clear any existing rows

        // Add table headers
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createHeaderTextView("Set Type"));
        headerRow.addView(createHeaderTextView("Reps"));
        headerRow.addView(createHeaderTextView("Weight"));
        setsTableLayout.addView(headerRow);

        // Add rows for sets
        for (int i = 0; i < numberOfSets; i++) {
            TableRow setRow = new TableRow(this);

            EditText setTypeInput = createEditText("Set " + (i + 1));
            EditText repsInput = createEditText("Reps");
            EditText weightInput = createEditText("Weight");

            setRow.addView(setTypeInput);
            setRow.addView(repsInput);
            setRow.addView(weightInput);

            setsTableLayout.addView(setRow);
        }
    }

    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }

    private EditText createEditText(String hint) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setPadding(8, 8, 8, 8);
        return editText;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}