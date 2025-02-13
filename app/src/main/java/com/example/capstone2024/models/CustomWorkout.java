package com.example.capstone2024.models;

import java.util.ArrayList;
import java.util.List;

public class CustomWorkout {
    private List<ExerciseSession> exercises;

    public CustomWorkout() {
        exercises = new ArrayList<>();
    }

    public void addExercise(ExerciseSession exercise) {
        exercises.add(exercise);
    }

    public List<ExerciseSession> getExercises() {
        return exercises;
    }
}