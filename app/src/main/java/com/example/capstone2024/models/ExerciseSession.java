package com.example.capstone2024.models;

import java.io.Serializable;

public class ExerciseSession implements Serializable {
    private Exercise exercise;
    private int sets;
    private int restTime; // in minutes
    private int reps;
    public ExerciseSession(Exercise exercise, int sets, int restTime, int reps) {
        this.exercise = exercise;
        this.sets = sets;
        this.restTime = restTime;
        this.reps = reps;
    }

    // Getters and setters

    public Exercise getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }

    public int getRestTime() {
        return restTime;
    }

    public int getCompletedSets() {
        return 1;
    }

    public int getReps() {
        return reps;
    }
}
