package com.example.capstone2024.contracts;

import com.example.capstone2024.database.ExerciseSessionWithExercise;
import com.example.capstone2024.models.ExerciseSession;

import java.util.List;

public interface WorkoutSessionContract {
    interface View {
        void displayExercises(List<ExerciseSessionWithExercise> exerciseSessions);
        void showError(String message);
        void showMessage(String message);
    }

    interface Presenter {
        void loadWorkoutSession(String dayName);
        void addExerciseSession(ExerciseSession exerciseSession);
        void removeExerciseSession(int exerciseSessionId);
        void createEmptyWorkoutSession(String dayName);
    }
}