package com.example.capstone2024.presenters;

import android.content.Context;

import com.example.capstone2024.contracts.HomeContract;
import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View view;
    private final Context context;
    private Map<String, WorkoutSession> workoutProgram;

    public HomePresenter(HomeContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void initializeWorkoutPlan() {
        try {
            InputStream exercisesInputStream = context.getAssets().open("exercises.json");
            WorkoutPlan workoutPlan = new WorkoutPlan(exercisesInputStream);

            // Simulate user input
            Map<String, Object> userInput = new HashMap<>();
            userInput.put("age", 30);
            userInput.put("height", 175);
            userInput.put("weight", 70);
            userInput.put("target_weight", 65);
            userInput.put("workout_days", 5);
            userInput.put("level", "intermediate");
            userInput.put("equipment", "dumbbell");
            userInput.put("availability", 1.5);

            workoutProgram = workoutPlan.generateWorkoutProgram(userInput);
            view.displayWorkoutProgram(workoutProgram);
        } catch (Exception e) {
            e.printStackTrace();
            view.showError("Failed to load workout plan.");
        }
    }

    @Override
    public void handleSurveyNavigation() {
        view.navigateToSurvey();
    }

    @Override
    public void handleProgressNavigation() {
        view.navigateToProgress();
    }

    @Override
    public void handleWorkoutPlanNavigation() {
        if (workoutProgram != null) {
            view.navigateToWorkoutPlan(workoutProgram);
        } else {
            view.showError("Workout plan is not initialized.");
        }
    }
}