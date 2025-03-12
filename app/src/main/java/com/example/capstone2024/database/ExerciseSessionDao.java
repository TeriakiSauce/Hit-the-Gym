package com.example.capstone2024.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.capstone2024.models.ExerciseSession;

import java.util.List;

@Dao
public interface ExerciseSessionDao {
    @Insert
    void insertExerciseSession(ExerciseSession exerciseSession);

    @Update
    void updateExerciseSession(ExerciseSession exerciseSession);

    @Delete
    void deleteExerciseSession(ExerciseSession exerciseSession);

    @Query("SELECT * FROM exercise_sessions WHERE workout_session_id = :workoutSessionId")
    List<ExerciseSession> getExerciseSessionsForWorkout(int workoutSessionId);

    @Query("DELETE FROM exercise_sessions WHERE id = :id")
    void deleteExerciseSessionById(int id);



    @Transaction
    @Query("SELECT * FROM exercise_sessions WHERE id = :id LIMIT 1")
    ExerciseSessionWithExercise getExerciseSessionWithExerciseById(int id);

}