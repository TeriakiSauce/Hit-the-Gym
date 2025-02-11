package com.example.capstone2024.models

import java.io.Serializable


class WorkoutSession(// Getters and setters
    @JvmField val exerciseSessions: MutableList<ExerciseSession>
) : Serializable 