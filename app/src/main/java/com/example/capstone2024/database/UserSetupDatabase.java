package com.example.capstone2024.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.capstone2024.models.Converters;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.UserSetup;
import com.example.capstone2024.models.WorkoutSession;

@Database(entities = {UserSetup.class, Exercise.class, WorkoutSession.class, ExerciseSession.class}, version = 4, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class UserSetupDatabase extends RoomDatabase{
    public abstract UserSetupDao userSetupDao();
    public abstract ExerciseDao exerciseDao();
    public abstract WorkoutSessionDao workoutSessionDao();
    public abstract ExerciseSessionDao exerciseSessionDao();
}