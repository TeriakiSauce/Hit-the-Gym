package com.example.capstone2024.models;

import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {
    private final String name;
    private final String level;
    private final String equipment;
    private final String category;
    private final List<String> primaryMuscles;

    private final List<String> instructions;

    public Exercise(String name, String level, String equipment, String category, List<String> primaryMuscles, List<String> instructions) {
        this.name = name;
        this.level = level;
        this.equipment = equipment;
        this.category = category;
        this.primaryMuscles = primaryMuscles;
        this.instructions = instructions;
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

    public List<String> getInstructions() {
        return instructions;
    }
}