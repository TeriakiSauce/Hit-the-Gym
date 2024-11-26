package com.example.capstone2024.models;

import java.io.Serializable;

public class ExerciseSession implements Serializable {
    private Exercise exercise;
    private int sets;
    private int restTime; // in minutes

    public ExerciseSession(Exercise exercise, int sets, int restTime) {
        this.exercise = exercise;
        this.sets = sets;
        this.restTime = restTime;
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
}
