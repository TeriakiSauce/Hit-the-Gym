package com.example.capstone2024.presenters

import com.example.capstone2024.contracts.WorkoutCalendarContract
import com.example.capstone2024.models.WorkoutCalendar
import com.example.capstone2024.models.WorkoutPlan
import java.util.Date

class WorkoutCalendarPresenter(
    private val view: WorkoutCalendarContract.View,
    private val workoutCalendarModel: WorkoutCalendar
) {
    fun assignWorkoutToDay(date: Date, workoutPlan: WorkoutPlan) {
        workoutCalendarModel.addWorkout(date, workoutPlan)
        view.updateCalendar()
    }

    fun removeWorkoutFromDay(date: Date) {
        workoutCalendarModel.removeWorkout(date)
        view.updateCalendar()
    }

    fun loadMonthData(year: Int, month: Int) {
        val monthData = workoutCalendarModel.getWorkoutPlanForMonth(year, month)
        view.displayMonthData(monthData)
    }
}
