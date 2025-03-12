package com.example.capstone2024.presenters;

import android.content.Context;

import com.example.capstone2024.contracts.HomeContract;
import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.models.UserSetup;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View view;
    private final Context context;
    private Map<String, WorkoutSessionWithExercises> workoutProgram;


    public HomePresenter(HomeContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void initializeWorkoutPlan() {
        try {
            InputStream exercisesInputStream = context.getAssets().open("exercises.json");

            Map<String, Object> userInput = fetchUserInput();

            WorkoutPlan workoutPlan = new WorkoutPlan(exercisesInputStream, this.context, userInput);

            workoutProgram = workoutPlan.generateWorkoutProgram();
            view.displayWorkoutProgram(workoutProgram);
        } catch (Exception e) {
            e.printStackTrace();
            view.showError("Failed to load workout plan.");
        }
    }
    private Map<String, Object> fetchUserInput() {
        Map<String, Object> userInput = new HashMap<>();
        // Initialize the database helper (which uses our modified client)
        UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(context);
        // Get the list of users synchronously
        List<UserSetup> users = helper.fetchAllUsersSync();

        // For testing purposes, use a dummy user
        if (users.isEmpty()) {
            userInput.put("age", 30);
            userInput.put("current_weight", 180);
            userInput.put("target_weight", 170);
            userInput.put("level", "Advanced");
            userInput.put("equipment", "Full Gym");
            userInput.put("targetBodyParts", new ArrayList<>(Arrays.asList("Chest", "Back", "Hamstrings", "Biceps", "Triceps")));
        } else {
            userInput.put("age", users.get(0).getAge());
            userInput.put("current_weight", users.get(0).getCurrentWeight());
            userInput.put("target_weight", users.get(0).getTargetWeight());
            userInput.put("level", users.get(0).getWorkoutLevel());
            userInput.put("equipment", users.get(0).getEquipment());
            userInput.put("targetBodyParts", users.get(0).getTargetBodyParts());
        }

        return userInput;
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

    public void handleWorkoutCalendarNavigation() {
        if (workoutProgram != null) {
            view.navigateToWorkoutCalendar(workoutProgram);
        } else {
            view.showError("Workout plan is not initialized.");
        }
    }
}