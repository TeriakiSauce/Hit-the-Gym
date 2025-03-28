package com.example.capstone2024.models;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class WorkoutCalendar extends GregorianCalendar {
    private Map<Date, WorkoutSession> workoutSchedule;

    public WorkoutCalendar() {
        super();
        this.workoutSchedule = new HashMap<>();
    }

    /**
     * Adds a workout plan to the specified date.
     */
    public void addWorkout(Date date, WorkoutSession workoutSession) {
        workoutSchedule.put(date, workoutSession);
    }

    /**
     * Removes a workout plan from the specified date.
     */
    public void removeWorkout(Date date) {
        workoutSchedule.remove(date);
    }

    /**
     * Gets the workout plan for a specific date.
     */
    public WorkoutSession getWorkoutForDate(Date date) {
        return workoutSchedule.get(date);
    }

    /**
     * Gets all workout plans for a specific month.
     */
    public Map<Date, WorkoutSession> getWorkoutPlanForMonth(int year, int month) {
        Map<Date, WorkoutSession> monthData = new HashMap<>();
        for (Map.Entry<Date, WorkoutSession> entry : workoutSchedule.entrySet()) {
            Date date = entry.getKey();
            if (date.getYear() == year - 1900 && date.getMonth() == month) {
                monthData.put(date, entry.getValue());
            }
        }
        return monthData;
    }

    /**
     * Checks if a workout plan is assigned to the specified date.
     */
    public boolean hasWorkout(Date date) {
        return workoutSchedule.containsKey(date);
    }
}