package com.example.capstone2024.contracts;

import com.example.capstone2024.models.ExerciseSession;

import java.util.List;

public interface WorkoutSessionContract {
    interface View {
        void displayExercises(List<ExerciseSession> exerciseSessions);
        void showError(String message);
    }

    interface Presenter {
        void loadWorkoutSession(String dayName, Object workoutSessionData);
    }
}