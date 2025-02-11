package com.example.capstone2024.models;


import java.io.Serializable;
import java.util.List;

public class WorkoutSession implements Serializable {
    private List<ExerciseSession> exerciseSessions;

    public WorkoutSession(List<ExerciseSession> exerciseSessions) {
        this.exerciseSessions = exerciseSessions;
    }

    // Getters and setters

    public List<ExerciseSession> getExerciseSessions() {
        return exerciseSessions;
    }
}