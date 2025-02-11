package com.example.capstone2024.models

import java.io.Serializable

class Exercise(// Getters and setters
    @JvmField val name: String,
    @JvmField val level: String,
    @JvmField val equipment: String,
    @JvmField val category: String,
    @JvmField val primaryMuscles: List<String>,
    @JvmField val instructions: List<String>
) :
    Serializable 