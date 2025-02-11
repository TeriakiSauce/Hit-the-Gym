package com.example.capstone2024.presenters;

import android.content.Intent;

import com.example.capstone2024.contracts.WorkoutPlanContract;
import com.example.capstone2024.models.WorkoutSession;

import java.util.HashMap;
import java.util.Map;

public class WorkoutPlanPresenter implements WorkoutPlanContract.Presenter {
    private WorkoutPlanContract.View view;

    public WorkoutPlanPresenter(WorkoutPlanContract.View view) {
        this.view = view;
    }

    @Override
    public void loadWorkoutProgram(Object intentData) {
        if (intentData instanceof Intent) {
            Intent intent = (Intent) intentData;
            Map<String, WorkoutSession> workoutProgram =
                    (HashMap<String, WorkoutSession>) intent.getSerializableExtra("WORKOUT_PROGRAM");

            if (workoutProgram != null) {
                view.displayWorkoutProgram(workoutProgram);
            } else {
                view.showError("No workout program received.");
            }
        } else {
            view.showError("Invalid data received.");
        }
    }
}