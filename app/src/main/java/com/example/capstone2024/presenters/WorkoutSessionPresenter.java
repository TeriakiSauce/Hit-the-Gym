package com.example.capstone2024.presenters;

import android.content.Context;

import com.example.capstone2024.contracts.WorkoutSessionContract;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.models.WorkoutSession;

public class WorkoutSessionPresenter implements WorkoutSessionContract.Presenter {
    private final WorkoutSessionContract.View view;
    private final UserSetupDatabaseHelper helper;

    public WorkoutSessionPresenter(WorkoutSessionContract.View view) {
        this.view = view;
        this.helper = new UserSetupDatabaseHelper((Context) view);
    }

    @Override
    public void loadWorkoutSession(String dayName) {
        int dayNumber = Integer.parseInt(dayName.replace("Day ", "").trim());
        // Query the database for the workout session with this day number
        WorkoutSessionWithExercises session = helper.getWorkoutSessionWithExercisesByDay(dayNumber);
        if (session != null) {
            view.displayExercises(session.getExerciseSessions());
        } else {
            view.showError("Workout session not found for " + dayName);
        }
    }
}