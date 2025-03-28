package com.example.capstone2024.contracts;

import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;

import java.util.Date;
import java.util.Map;

public interface WorkoutCalendarContract {
    interface View {

        /**
         * Displays an error message to the user.
         *
         * @param message The error message to display.
         */
        void showError(String message);

        void updateCalendar();
    }

    interface Presenter {
        /**
         * Assigns a workout to a specific day in the calendar.
         *
         * @param date        The date to assign the workout.
         * @param workoutPlan The workout plan to assign.
         */
        void assignWorkoutToDay(Date date, WorkoutPlan workoutPlan);

        /**
         * Removes a workout from a specific day in the calendar.
         *
         * @param date The date to remove the workout.
         */
        void removeWorkoutFromDay(Date date);

        /**
         * Loads the workout data for a specific month.
         *
         * @param year  The year of the month to load.
         * @param month The month to load (0-based index).
         */
        void loadMonthData(int year, int month);
    }
}