package com.example.capstone2024.database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.capstone2024.models.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert
    void insertExercise(Exercise exercise);

    @Insert
    void insertAll(List<Exercise> exercises);

    @Query("SELECT * FROM exercises")
    List<Exercise> getAllExercises();

    // Search by exercise name
    @Query("SELECT * FROM exercises WHERE lower(name) LIKE '%' || lower(:query) || '%'")
    List<Exercise> searchExercises(String query);
}