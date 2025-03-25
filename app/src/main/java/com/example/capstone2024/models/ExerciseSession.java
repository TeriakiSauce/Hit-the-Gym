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
            this.sets = 1;
            this.reps = 1;
            this.restTime = 5; // 5 minute rest for cardio
        } else if (category.equals("stretching")) {
            this.sets = 1;
            this.reps = 10;
            this.restTime = 1; // 1 minute rest for stretching
        } else if (category.equals("strength")) {
            if (equipment.contains("barbell")) {
                this.warmupWeight = 45;
                this.fullWeight = 65;
                this.reps = 8;
                this.sets = 4;
                this.restTime = 3; // 3 minutes rest for barbell exercises
            } else if (equipment.contains("dumbbell") || equipment.contains("kettlebell")) {
                this.warmupWeight = 10;
                this.fullWeight = 20;
                this.reps = 10;
                this.sets = 4;
                this.restTime = 2; // 2 minutes rest for dumbbell exercises
            } else if (equipment.contains("machine")) {
                this.warmupWeight = 20;
                this.fullWeight = 30;
                this.reps = 10;
                this.sets = 4;
                this.restTime = 2; // 2 minutes rest for machine exercises
            } else if (equipment.contains("bodyweight")) {
                this.reps = 10;
                this.sets = 4;
                this.restTime = 2; // 2 minutes rest for bodyweight exercises
            } else if (equipment.contains("cable")) {
                this.warmupWeight = 10;
                this.fullWeight = 15;
                this.reps = 12;
                this.sets = 4;
                this.restTime = 2; // 2 minutes rest for cable exercises
            }
        } else {
            this.sets = 4;
            this.reps = 10;
            this.restTime = 2; // default rest time
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