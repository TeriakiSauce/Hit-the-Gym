package com.example.capstone2024.contracts;

import com.example.capstone2024.models.WorkoutSession;

import java.util.Map;

public interface HomeContract {
    interface View {
        void displayWorkoutProgram(Map<String, WorkoutSession> workoutProgram);
        void navigateToSurvey();
        void navigateToProgress();
        void navigateToWorkoutPlan(Map<String, WorkoutSession> workoutProgram);
        void navigateToWorkoutCalendar(Map<String, WorkoutSession> workoutProgram);
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