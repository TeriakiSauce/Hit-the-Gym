package com.example.capstone2024.presenters

import com.example.capstone2024.contracts.WorkoutSessionContract
import com.example.capstone2024.models.WorkoutSession

class WorkoutSessionPresenter(private val view: WorkoutSessionContract.View) :
    WorkoutSessionContract.Presenter {
    override fun loadWorkoutSession(dayName: String, workoutSessionData: Any) {
        if (workoutSessionData is WorkoutSession) {
            view.displayExercises(workoutSessionData.exerciseSessions)
        } else {
            view.showError("Failed to load workout session for $dayName")
        }
    }
}