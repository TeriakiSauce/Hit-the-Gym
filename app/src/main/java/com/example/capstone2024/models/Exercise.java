package com.example.capstone2024.models;

import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {
    private final String name;
    private final String level;
    private final String equipment;
    private final String category;
    private final String primaryMuscles;
    private final List<String> secondaryMuscles;

    private final List<String> instructions;
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

}