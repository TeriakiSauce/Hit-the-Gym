package com.example.capstone2024.contracts;

import com.example.capstone2024.models.ExerciseSession;
import java.util.List;

public interface CustomWorkoutContract {
    interface View {
        void displayExercises(List<ExerciseSession> exercises);
        void showError(String message);
    }

    interface Presenter {
        void loadWorkout();
        void addExercise(ExerciseSession exercise);
    }
}