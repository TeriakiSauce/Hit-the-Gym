package com.example.capstone2024.models;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {UserSetup.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)  // Register the TypeConverter
public abstract class UserSetupDatabase extends RoomDatabase{
    public abstract UserSetupDao userSetupDao();
}