package com.example.capstone2024.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.window.SplashScreen;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.HomeContract;
import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.presenters.HomePresenter;
import com.example.capstone2024.ui.ProgressActivity;
import com.example.capstone2024.ui.WorkoutCalendarActivity;
import com.example.capstone2024.ui.usersetup.UserSetupActivity;
import com.example.capstone2024.ui.workoutplan.WorkoutPlanActivity;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private ImageButton homeButton;
    private ImageButton progressButton;
    private ImageButton heartButton;
    private ImageButton surveyButton;
    private ImageButton chartButton;

    private HomeContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = getSplashScreen();
        setContentView(R.layout.activity_home);

        // Initialize UI
        homeButton = findViewById(R.id.button_home);
        progressButton = findViewById(R.id.button_progress);
        chartButton = findViewById(R.id.button_chart);
        heartButton = findViewById(R.id.button_heart);
        surveyButton = findViewById(R.id.button_survey);

        // Initialize Presenter
        presenter = new HomePresenter(this, this);

        // Set up listeners
        homeButton.setOnClickListener(v -> {});
        progressButton.setOnClickListener(v -> presenter.handleProgressNavigation());
        chartButton.setOnClickListener(v -> presenter.handleWorkoutPlanNavigation());
        heartButton.setOnClickListener(v -> presenter.handleWorkoutCalendarNavigation());
        surveyButton.setOnClickListener(v -> presenter.handleSurveyNavigation());

        // Initialize Workout Plan
        presenter.initializeWorkoutPlan();
    }

    @Override
    public void displayWorkoutProgram(Map<String, WorkoutSessionWithExercises> workoutProgram) {
        // Placeholder: Logic for updating UI with workout program if needed
    }

    @Override
    public void navigateToSurvey() {
        Intent intent = new Intent(HomeActivity.this, UserSetupActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToProgress() {
        Intent intent = new Intent(HomeActivity.this, ProgressActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToWorkoutPlan(Map<String, WorkoutSessionWithExercises> workoutProgram) {
        Intent intent = new Intent(HomeActivity.this, WorkoutPlanActivity.class);
        intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
        startActivity(intent);
    }
    @Override
    public void navigateToWorkoutCalendar(Map<String, WorkoutSessionWithExercises> workoutProgram) {
        Intent intent = new Intent(HomeActivity.this, WorkoutCalendarActivity.class);
        intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        // Example: Show a Toast
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}