package com.example.capstone2024.contracts;

import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.models.WorkoutSession;

import java.util.Map;

public interface WorkoutPlanContract {
    interface View {
        void displayWorkoutProgram(Map<String, WorkoutSessionWithExercises> workoutProgram);
        void showError(String message);
    }

    interface Presenter {
        void loadWorkoutProgram();
    }
}