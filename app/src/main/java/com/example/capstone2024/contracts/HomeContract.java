package com.example.capstone2024.contracts;

import com.example.capstone2024.database.WorkoutSessionWithExercises;

import java.util.Map;

public interface HomeContract {
    interface View {
        void displayWorkoutProgram(Map<String, WorkoutSessionWithExercises> workoutProgram);
        void navigateToSurvey();
        void navigateToProgress();
        void navigateToWorkoutPlan(Map<String, WorkoutSessionWithExercises> workoutProgram);
        void navigateToWorkoutCalendar(Map<String, WorkoutSessionWithExercises> workoutProgram);
        void showError(String message);

    }

    interface Presenter {
        void initializeWorkoutPlan();
        void handleSurveyNavigation();
        void handleProgressNavigation();
        void handleWorkoutPlanNavigation();
        void handleWorkoutCalendarNavigation();
    }
}