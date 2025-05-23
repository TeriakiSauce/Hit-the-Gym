package com.example.capstone2024.contracts;

import android.graphics.drawable.Drawable;

import com.example.capstone2024.database.ExerciseSessionWithExercise;
import com.example.capstone2024.models.Set;

import java.util.List;

public interface ExerciseSessionContract {
    interface View {
        void displayExerciseDetails(String name, Drawable imageResource, String instructions);
        void setupSetsTable(List<Set> numberOfSets);
        void showError(String message);
    }

    interface Presenter {
        void loadExerciseSession(ExerciseSessionWithExercise session);
    }
}