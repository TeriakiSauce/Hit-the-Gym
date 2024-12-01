package com.example.capstone2024.contracts;

import com.example.capstone2024.models.Exercise;

import java.util.List;

public interface ExerciseSessionContract {
    interface View {
        void displayExerciseDetails(String name, int imageResource, String instructions);
        void setupSetsTable(int numberOfSets);
        void showError(String message);
    }

    interface Presenter {
        void loadExerciseSession(Exercise exercise);
    }
}