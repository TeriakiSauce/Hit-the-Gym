package com.example.capstone2024.presenters

import android.content.Intent
import com.example.capstone2024.contracts.WorkoutPlanContract
import com.example.capstone2024.models.WorkoutSession

class WorkoutPlanPresenter(private val view: WorkoutPlanContract.View) :
    WorkoutPlanContract.Presenter {
    override fun loadWorkoutProgram(intentData: Any) {
        if (intentData is Intent) {
            val workoutProgram: Map<String, WorkoutSession>? =
                intentData.getSerializableExtra("WORKOUT_PROGRAM") as HashMap<String, WorkoutSession>?

            if (workoutProgram != null) {
                view.displayWorkoutProgram(workoutProgram)
            } else {
                view.showError("No workout program received.")
            }
        } else {
            view.showError("Invalid data received.")
        }
    }
}