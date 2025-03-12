package com.example.capstone2024.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;

import java.io.Serializable;

public class ExerciseSessionWithExercise implements Serializable {
    @Embedded
    public ExerciseSession exerciseSession;

    @Relation(
            parentColumn = "exercise_id",
            entityColumn = "id"
    )
    public Exercise exercise;

    public ExerciseSessionWithExercise(ExerciseSession exerciseSession, Exercise exercise) {
        this.exerciseSession = exerciseSession;
        this.exercise = exercise;
    }

    public ExerciseSession getExerciseSession() {
        return exerciseSession;
    }
    public Exercise getExercise() {
        return exercise;
    }
    public int getSets() {
        return exerciseSession.getSets();
    }
    public int getReps() {
        return exerciseSession.getReps();
    }

    public int getCompletedSets() {
        return exerciseSession.getCompletedSets();
    }
}