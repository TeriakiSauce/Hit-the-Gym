package com.example.capstone2024.models;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    // Convert List<String> to String (for storage in the database)
    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(list); // Convert the List to JSON String
    }

    // Convert String (JSON) back to List<String> (when retrieving from the database)
    @TypeConverter
    public static List<String> toList(String value) {
        if (value == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType); // Convert the JSON String back to List
    }
}
