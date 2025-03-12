package com.example.capstone2024.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;


import com.example.capstone2024.models.WorkoutSession;

import java.util.List;

@Dao
public interface WorkoutSessionDao {
    @Insert
    long insertWorkoutSession(WorkoutSession workoutSession);

    @Query("SELECT * FROM workout_sessions")
    List<WorkoutSession> getWorkoutSessions();

    @Query("SELECT * FROM workout_sessions WHERE completed = 0")
    List<WorkoutSession> getIncompleteWorkoutSessions();

    @Transaction
    @Query("SELECT * FROM workout_sessions")
    List<WorkoutSessionWithExercises> getWorkoutSessionsWithExercises();

    @Transaction
    @Query("SELECT * FROM workout_sessions WHERE day_number = :dayNumber")
    WorkoutSessionWithExercises getWorkoutSessionWithExercisesByDay(int dayNumber);
}