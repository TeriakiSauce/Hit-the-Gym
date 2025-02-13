package com.example.capstone2024.presenters;

import com.example.capstone2024.contracts.CustomWorkoutContract;
import com.example.capstone2024.models.CustomWorkout;
import com.example.capstone2024.models.ExerciseSession;

public class CustomWorkoutPresenter implements CustomWorkoutContract.Presenter {
    private final CustomWorkoutContract.View view;
    private final CustomWorkout customWorkout;

    public CustomWorkoutPresenter(CustomWorkoutContract.View view) {
        this.view = view;
        this.customWorkout = new CustomWorkout();
    }

    @Override
    public void loadWorkout() {
        view.displayExercises(customWorkout.getExercises());
    }

    @Override
    public void addExercise(ExerciseSession exercise) {
        customWorkout.addExercise(exercise);
        view.displayExercises(customWorkout.getExercises());
    }
}
