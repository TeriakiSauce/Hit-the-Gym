package com.example.capstone2024.models;

public class Set {
    private int reps;
    private int weight;
    private String setType; // "W" for warmup, "R" for regular

    public Set(int reps, int weight, String setType) {
        this.reps = reps;
        this.weight = weight;
        this.setType = setType;
    }

    public int getReps() {
        return reps;
    }

    public int getWeight() {
        return weight;
    }

    public String getSetType() {
        return setType;
    }
}