package com.example.capstone2024.presenters;

import com.example.capstone2024.contracts.WorkoutSessionContract;
import com.example.capstone2024.models.WorkoutSession;

public class WorkoutSessionPresenter implements WorkoutSessionContract.Presenter {
    private final WorkoutSessionContract.View view;

    public WorkoutSessionPresenter(WorkoutSessionContract.View view) {
        this.view = view;
    }

    @Override
    public void loadWorkoutSession(String dayName, Object workoutSessionData) {
        if (workoutSessionData instanceof WorkoutSession) {
            WorkoutSession workoutSession = (WorkoutSession) workoutSessionData;
            view.displayExercises(workoutSession.getExerciseSessions());
        } else {
            view.showError("Failed to load workout session for " + dayName);
        }
    }
}