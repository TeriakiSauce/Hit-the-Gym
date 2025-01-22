package com.example.capstone2024.models;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {UserSetup.class}, version = 1)
@TypeConverters(Converters.class)  // Register the TypeConverter
public abstract class UserSetupDatabase extends RoomDatabase{
    public abstract UserSetupDao userSetupDao();
}