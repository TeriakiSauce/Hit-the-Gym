package com.example.capstone2024.models;

import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {
    private String name;
    private String level;
    private String equipment;
    private String category;
    private List<String> primaryMuscles;

    public Exercise(String name, String level, String equipment, String category, List<String> primaryMuscles) {
        this.name = name;
        this.level = level;
        this.equipment = equipment;
        this.category = category;
        this.primaryMuscles = primaryMuscles;
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

    public List<String> getPrimaryMuscles() {
        return primaryMuscles;
    }
}