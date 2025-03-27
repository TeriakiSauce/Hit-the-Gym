package com.example.capstone2024.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @ColumnInfo(name = "completed_sets")
    private int completedSets;
    private int sets;
    private int restTime; // in minutes
    private int reps;
    private int warmupWeight;
    private int fullWeight;

    public ExerciseSession(int id, int workoutSessionId, int exerciseId, int completedSets, int sets, int restTime, int reps) {
        this.id = id;
        this.workoutSessionId = workoutSessionId;
        this.exerciseId = exerciseId;
        this.completedSets = completedSets;
        this.sets = sets;
        this.restTime = restTime;
        this.reps = reps;
    }

    // New constructor: sets, reps, and rest time are determined automatically based on the exercise's category
    public ExerciseSession(int workoutSessionId, Exercise exercise) {
        this.workoutSessionId = workoutSessionId;
        this.exerciseId = exercise.getId();
        this.completedSets = 0;

        String category = exercise.getCategory().toLowerCase();
        String equipment = exercise.getEquipment().toLowerCase();
        if (category.equals("cardio")) {
            // For cardio warmups, give one set of one rep
            this.sets = 1;
            this.reps = 1;
            this.restTime = 5; // 5 minutes rest
        } else if (category.equals("stretching")) {
            // Stretching exercises are performed as a single set
            this.sets = 1;
            this.reps = 10;
            this.restTime = 1; // 1 minute rest
        } else if (category.equals("plyometrics")) {
            // Plyometrics - fewer sets and quick recovery
            this.sets = 3;
            this.reps = 10;
            this.restTime = 1;
        } else if (category.equals("powerlifting")) {
            // Powerlifting – heavy, low rep work.
            this.sets = 5;
            this.reps = 5;
            this.restTime = 4;
        } else if (category.equals("strongman")) {
            // Strongman – very heavy, low-rep movements
            this.sets = 3;
            this.reps = 3;
            this.restTime = 4;
        } else if (category.equals("strength")) {
            // Strength exercises, adjust further based on equipment
            if (equipment.contains("barbell")) {
                this.warmupWeight = 45;
                this.fullWeight = 65;
                this.sets = 4;
                this.reps = 8;
                this.restTime = 3;
            } else if (equipment.contains("dumbbell") || equipment.contains("kettlebell")) {
                this.warmupWeight = 10;
                this.fullWeight = 20;
                this.sets = 4;
                this.reps = 10;
                this.restTime = 2;
            } else if (equipment.contains("machine")) {
                this.warmupWeight = 20;
                this.fullWeight = 30;
                this.sets = 4;
                this.reps = 10;
                this.restTime = 2;
            } else if (equipment.contains("cable")) {
                this.warmupWeight = 10;
                this.fullWeight = 15;
                this.sets = 4;
                this.reps = 12;
                this.restTime = 2;
            } else if (equipment.contains("bands")) {
                // For bands there is no fixed weight
                this.sets = 3;
                this.reps = 15;
                this.restTime = 1;
            } else if (equipment.contains("body") || equipment.contains("foam") || equipment.contains("ball")) {
                // Body-only has no weight
                this.sets = 4;
                this.reps = 10;
                this.restTime = 2;
            } else {
                // Default for strength if equipment isn't recognized
                this.warmupWeight = 0;
                this.fullWeight = 0;
                this.sets = 4;
                this.reps = 10;
                this.restTime = 2;
            }
        } else {
            // Default values for unrecognized categories
            this.warmupWeight = 0;
            this.fullWeight = 0;
            this.sets = 4;
            this.reps = 10;
            this.restTime = 2;
        }
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

    public int getWarmupWeight() {
        return warmupWeight;
    }

    public void setWarmupWeight(int warmupWeight) {
        this.warmupWeight = warmupWeight;
    }

    public int getFullWeight() {
        return fullWeight;
    }

    public void setFullWeight(int fullWeight) {
        this.fullWeight = fullWeight;
    }

    @Ignore // Generates set values for each set
    public List<Set> getSetDetails() {
        List<Set> setDetails = new ArrayList<>();
        for (int i = 1; i <= sets; i++) {
            int weight = (i == 1) ? warmupWeight : fullWeight;
            String setType = (i == 1) ? "W" : "R";
            setDetails.add(new Set(reps, weight, setType));
        }
        return setDetails;
    }
}