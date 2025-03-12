package com.example.capstone2024.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.WorkoutSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSessionWithExercises implements Serializable {
    @Embedded
    public WorkoutSession workoutSession;

    @Relation(
            parentColumn = "id",
            entityColumn = "workout_session_id",
            entity = ExerciseSession.class
    )
    public List<ExerciseSessionWithExercise> exerciseSessions;
    public WorkoutSessionWithExercises(WorkoutSession workoutSession) {
        this.workoutSession = workoutSession;
        this.exerciseSessions = new ArrayList<>();
    }

    public List<ExerciseSessionWithExercise> getExerciseSessions() {
        return exerciseSessions;
    }
    public int getId() {
        return workoutSession.getId();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Workout Session ID: ").append(workoutSession.getId()).append(" ");
        return sb.toString();
    }
}