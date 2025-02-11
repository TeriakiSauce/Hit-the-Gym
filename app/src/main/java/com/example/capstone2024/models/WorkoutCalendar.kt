package com.example.capstone2024.models;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class WorkoutCalendar extends GregorianCalendar {
    private Map<Date, WorkoutPlan> workoutSchedule;

    public WorkoutCalendar() {
        super();
        this.workoutSchedule = new HashMap<>();
    }

    /**
     * Adds a workout plan to the specified date.
     *
     * @param date        The date to assign the workout plan.
     * @param workoutPlan The workout plan to assign.
     */
    public void addWorkout(Date date, WorkoutPlan workoutPlan) {
        workoutSchedule.put(date, workoutPlan);
    }

    /**
     * Removes a workout plan from the specified date.
     *
     * @param date The date to remove the workout plan.
     */
    public void removeWorkout(Date date) {
        workoutSchedule.remove(date);
    }

    /**
     * Gets the workout plan for a specific date.
     *
     * @param date The date to retrieve the workout plan for.
     * @return The workout plan for the date, or null if none exists.
     */
    public WorkoutPlan getWorkoutForDate(Date date) {
        return workoutSchedule.get(date);
    }

    /**
     * Gets all workout plans for a specific month.
     *
     * @param year  The year of the month to retrieve.
     * @param month The month to retrieve (0-based index).
     * @return A map of dates to workout plans for the specified month.
     */
    public Map<Date, WorkoutPlan> getWorkoutPlanForMonth(int year, int month) {
        Map<Date, WorkoutPlan> monthData = new HashMap<>();
        for (Map.Entry<Date, WorkoutPlan> entry : workoutSchedule.entrySet()) {
            Date date = entry.getKey();
            if (date.getYear() == year - 1900 && date.getMonth() == month) {
                monthData.put(date, entry.getValue());
            }
        }
        return monthData;
    }

    /**
     * Checks if a workout plan is assigned to the specified date.
     *
     * @param date The date to check.
     * @return True if a workout is assigned, false otherwise.
     */
    public boolean hasWorkout(Date date) {
        return workoutSchedule.containsKey(date);
    }
}