package com.example.capstone2024.models

import java.util.Date
import java.util.GregorianCalendar

class WorkoutCalendar : GregorianCalendar() {
    private val workoutSchedule: MutableMap<Date, WorkoutPlan> =
        HashMap()

    /**
     * Adds a workout plan to the specified date.
     *
     * @param date        The date to assign the workout plan.
     * @param workoutPlan The workout plan to assign.
     */
    fun addWorkout(date: Date, workoutPlan: WorkoutPlan) {
        workoutSchedule[date] = workoutPlan
    }

    /**
     * Removes a workout plan from the specified date.
     *
     * @param date The date to remove the workout plan.
     */
    fun removeWorkout(date: Date) {
        workoutSchedule.remove(date)
    }

    /**
     * Gets the workout plan for a specific date.
     *
     * @param date The date to retrieve the workout plan for.
     * @return The workout plan for the date, or null if none exists.
     */
    fun getWorkoutForDate(date: Date): WorkoutPlan? {
        return workoutSchedule[date]
    }

    /**
     * Gets all workout plans for a specific month.
     *
     * @param year  The year of the month to retrieve.
     * @param month The month to retrieve (0-based index).
     * @return A map of dates to workout plans for the specified month.
     */
    fun getWorkoutPlanForMonth(year: Int, month: Int): Map<Date, WorkoutPlan> {
        val monthData: MutableMap<Date, WorkoutPlan> = HashMap()
        for ((date, value) in workoutSchedule) {
            if (date.year == year - 1900 && date.month == month) {
                monthData[date] = value
            }
        }
        return monthData
    }

    /**
     * Checks if a workout plan is assigned to the specified date.
     *
     * @param date The date to check.
     * @return True if a workout is assigned, false otherwise.
     */
    fun hasWorkout(date: Date): Boolean {
        return workoutSchedule.containsKey(date)
    }
}