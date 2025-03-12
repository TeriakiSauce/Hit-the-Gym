package com.example.capstone2024.presenters;

import android.content.Context;
import android.content.Intent;

import com.example.capstone2024.contracts.WorkoutSessionContract;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.ui.workoutsession.WorkoutSessionActivity;

public class WorkoutSessionPresenter implements WorkoutSessionContract.Presenter {
    private final WorkoutSessionContract.View view;
    private final UserSetupDatabaseHelper helper;
    private int currentDayNumber;

    public WorkoutSessionPresenter(WorkoutSessionContract.View view) {
        this.view = view;
        this.helper = new UserSetupDatabaseHelper((Context) view);
    }

    @Override
    public void loadWorkoutSession(String dayName) {
        currentDayNumber = Integer.parseInt(dayName.replace("Day ", "").trim());
        // Query the database for the workout session with this day number
        WorkoutSessionWithExercises session = helper.getWorkoutSessionWithExercisesByDay(currentDayNumber);
        if (session != null) {
            view.displayExercises(session.getExerciseSessions());
        } else {
            view.showError("Workout session not found for " + dayName);
        }
    }

    @Override
    public void addExerciseSession(ExerciseSession newSession) {
        WorkoutSessionWithExercises session = helper.getWorkoutSessionWithExercisesByDay(currentDayNumber);
        if (session != null) {
            newSession.setWorkoutSessionId(session.getId());
            helper.insertExerciseSession(newSession);
            view.showMessage("Exercise added.");
            // Reload session after insertion to update UI
            loadWorkoutSession("Day " + currentDayNumber);
        } else {
            view.showError("Failed to add exercise session.");
        }
    }
    @Override
    public void removeExerciseSession(int exerciseSessionId) {
        // Remove the exercise session from the database.
        helper.deleteExerciseSession(exerciseSessionId);
        view.showMessage("Exercise removed.");
        loadWorkoutSession("Day " + currentDayNumber);
    }

    @Override
    public void createEmptyWorkoutSession(String dayName) {
        // Create a new WorkoutSession with no exercise sessions.
        int dayNumber;
        try {
            dayNumber = Integer.parseInt(dayName.replace("Day ", "").trim());
        } catch (NumberFormatException e) {
            view.showError("Invalid day name format.");
            return;
        }
        WorkoutSession emptySession = new WorkoutSession(dayNumber);
        int newId = helper.insertWorkoutSession(emptySession);
        if (newId != 0) {
            view.showMessage("New empty workout session created.");
//            // Navigate to the WorkoutSessionActivity for editing.
//            Intent intent = new Intent((Context) view, WorkoutSessionActivity.class);
//            intent.putExtra("DAY_NAME", "Day " + dayNumber);
//            ((Context) view).startActivity(intent);
        } else {
            view.showError("Error creating new workout session.");
        }
    }
}