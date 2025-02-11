package com.example.capstone2024.models

import java.io.Serializable

class ExerciseSession(// Getters and setters
    @JvmField val exercise: Exercise, @JvmField val sets: Int, // in minutes
    val restTime: Int, @JvmField val reps: Int
) :
    Serializable {
    val completedSets: Int
        get() = 1
}
