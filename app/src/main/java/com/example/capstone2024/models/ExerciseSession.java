package com.example.capstone2024.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "exercise_sessions",
        foreignKeys = @ForeignKey(entity = WorkoutSession.class,
                parentColumns = "id",
                childColumns = "workout_session_id",
                onDelete = ForeignKey.CASCADE))
public class ExerciseSession implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "workout_session_id")
    private int workoutSessionId;
    @ColumnInfo(name = "exercise_id")
    private int exerciseId;
    private int sets;
    private int restTime; // in minutes
    private int reps;

    private int completedSets;
    public ExerciseSession(int workoutSessionId, int exerciseId, int sets, int restTime, int reps) {
        this.workoutSessionId = workoutSessionId;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.restTime = restTime;
        this.reps = reps;
    }

    // Getters and setters

    public int getSets() {
        return sets;
    }

    public int getRestTime() {
        return restTime;
    }

    public int getCompletedSets() {
        return completedSets;
    }

    public int getReps() {
        return reps;
    }

    public int getExerciseId() {
        return exerciseId;
    }


    public int getId() {
        return id;
    }

    public int getWorkoutSessionId() {
        return workoutSessionId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompletedSets(int completedSets) {
        this.completedSets = completedSets;
    }

    public void setWorkoutSessionId(int workoutSessionId) {
        this.workoutSessionId = workoutSessionId;
    }
}
