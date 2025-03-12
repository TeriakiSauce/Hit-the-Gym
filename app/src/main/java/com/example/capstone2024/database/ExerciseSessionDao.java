package com.example.capstone2024.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.capstone2024.models.ExerciseSession;

import java.util.List;

@Dao
public interface ExerciseSessionDao {
    @Insert
    void insertExerciseSession(ExerciseSession exerciseSession);

    @Update
    void updateExerciseSession(ExerciseSession exerciseSession);

    @Query("SELECT * FROM exercise_sessions WHERE workout_session_id = :workoutSessionId")
    List<ExerciseSession> getExerciseSessionsForWorkout(int workoutSessionId);
}