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
    private static UserSetupDatabase INSTANCE;

    public static UserSetupDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserSetupDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserSetupDatabase.class, "userSetup_database")
                            .allowMainThreadQueries()  // TEMPORARY: Remove in production!
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}