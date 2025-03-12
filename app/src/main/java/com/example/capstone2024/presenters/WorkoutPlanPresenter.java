package com.example.capstone2024.presenters;

import android.content.Context;
import android.content.Intent;

import com.example.capstone2024.contracts.WorkoutPlanContract;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.models.WorkoutSession;

import java.util.HashMap;
import java.util.Map;

public class WorkoutPlanPresenter implements WorkoutPlanContract.Presenter {
    private WorkoutPlanContract.View view;
    private UserSetupDatabaseHelper helper;

    public WorkoutPlanPresenter(WorkoutPlanContract.View view) {
        this.view = view;
        helper = new UserSetupDatabaseHelper((Context) view);
    }

    @Override
    public void loadWorkoutProgram() {
        Map<String, WorkoutSessionWithExercises> workoutProgram  = helper.getStoredWorkoutProgram();

        if (workoutProgram != null) {
            view.displayWorkoutProgram(workoutProgram);
        } else {
            view.showError("No workout program received.");
        }
    }
}