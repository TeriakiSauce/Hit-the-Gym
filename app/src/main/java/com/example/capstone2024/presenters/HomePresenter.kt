package com.example.capstone2024.presenters

import android.content.Context
import com.example.capstone2024.contracts.HomeContract
import com.example.capstone2024.models.WorkoutPlan
import com.example.capstone2024.models.WorkoutSession

class HomePresenter(private val view: HomeContract.View, private val context: Context) :
    HomeContract.Presenter {
    private var workoutProgram: Map<String, WorkoutSession>? = null

    override fun initializeWorkoutPlan() {
        try {
            val exercisesInputStream = context.assets.open("exercises.json")
            val workoutPlan = WorkoutPlan(exercisesInputStream)

            // Simulate user input
            val userInput: MutableMap<String?, Any> = HashMap()
            userInput["age"] = 30
            userInput["height"] = 175
            userInput["weight"] = 70
            userInput["target_weight"] = 65
            userInput["workout_days"] = 5
            userInput["level"] = "intermediate"
            userInput["equipment"] = "dumbbell"
            userInput["availability"] = 1.5

            workoutProgram = workoutPlan.generateWorkoutProgram(userInput)
            view.displayWorkoutProgram(workoutProgram)
        } catch (e: Exception) {
            e.printStackTrace()
            view.showError("Failed to load workout plan.")
        }
    }

    override fun handleSurveyNavigation() {
        view.navigateToSurvey()
    }

    override fun handleProgressNavigation() {
        view.navigateToProgress()
    }

    override fun handleWorkoutPlanNavigation() {
        if (workoutProgram != null) {
            view.navigateToWorkoutPlan(workoutProgram)
        } else {
            view.showError("Workout plan is not initialized.")
        }
    }

    override fun handleWorkoutCalendarNavigation() {
        if (workoutProgram != null) {
            view.navigateToWorkoutCalendar(workoutProgram)
        } else {
            view.showError("Workout plan is not initialized.")
        }
    }
}