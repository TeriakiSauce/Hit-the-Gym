package com.example.capstone2024.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "exercises", indices = {@Index(value = "name")})
@TypeConverters(Converters.class)
public class Exercise implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private final String name;
    @ColumnInfo(name = "level")
    private final String level;
    @ColumnInfo(name = "equipment")
    private final String equipment;
    @ColumnInfo(name = "category")
    private final String category;
    @ColumnInfo(name = "primary_muscles")
    private final String primaryMuscles;
    @ColumnInfo(name = "secondary_muscles")
    private final List<String> secondaryMuscles;
    @ColumnInfo(name = "instructions")
    private final List<String> instructions;
    @ColumnInfo(name = "mechanic")
    private final String mechanic;

    public Exercise(String name, String level, String equipment, String category, String primaryMuscles, List<String> secondaryMuscles, List<String> instructions, String mechanic) {
        this.name = name;
        this.level = level;
        this.equipment = equipment;
        this.category = category;
        this.primaryMuscles = primaryMuscles;
        this.secondaryMuscles = secondaryMuscles;
        this.instructions = instructions;
        this.mechanic = mechanic;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getCategory() {
        return category;
    }

    public String getPrimaryMuscles() {
        return primaryMuscles;
    }

    public List<String> getSecondaryMuscles() { return secondaryMuscles; }

    public List<String> getInstructions() {
        return instructions;
    }
    public String getMechanic() {
        return mechanic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}