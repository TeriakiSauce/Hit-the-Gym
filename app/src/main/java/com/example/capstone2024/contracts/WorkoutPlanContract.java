package com.example.capstone2024.contracts;

import com.example.capstone2024.models.WorkoutSession;

import java.util.Map;

public interface WorkoutPlanContract {
    interface View {
        void displayWorkoutProgram(Map<String, WorkoutSession> workoutProgram);
        void showError(String message);
    }

    interface Presenter {
        void loadWorkoutProgram(Object intentData);
    }
}