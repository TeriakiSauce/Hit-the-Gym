package com.example.capstone2024.presenters;

import com.example.capstone2024.contracts.WorkoutCalendarContract;
import com.example.capstone2024.models.WorkoutCalendar;
import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;

import java.util.Date;
import java.util.Map;

public class WorkoutCalendarPresenter {
    private final WorkoutCalendarContract.View view;
    private final WorkoutCalendar workoutCalendarModel;

    public WorkoutCalendarPresenter(WorkoutCalendarContract.View view, WorkoutCalendar workoutCalendarModel) {
        this.view = view;
        this.workoutCalendarModel = workoutCalendarModel;
    }

    public void assignWorkoutToDay(Date date, WorkoutSession workoutSession) {
        workoutCalendarModel.addWorkout(date, workoutSession);
        view.updateCalendar();
    }

    public void removeWorkoutFromDay(Date date) {
        workoutCalendarModel.removeWorkout(date);
        view.updateCalendar();
    }

//    public void loadMonthData(int year, int month) {
//        Map<Date, WorkoutSession> monthData = workoutCalendarModel.getWorkoutPlanForMonth(year, month);
//        view.displayMonthData(monthData);
//    }
}
