package com.example.capstone2024.presenters;

import com.example.capstone2024.contracts.UserSetupContract;

import java.util.List;
import java.util.Objects;

public class UserSetupPresenter implements UserSetupContract.Presenter {
    private final UserSetupContract.View view;

    public UserSetupPresenter(UserSetupContract.View view) {
        this.view = view;
    }

    @Override
    public void submitSurvey(
            String name,
            String age,
            String currentWeight,
            String targetWeight,
            String workoutLevel,
            List<String> targetBodyParts,
            String equipment
    ) {
        // Validate inputs
        if (name.isEmpty()) {
            view.showValidationError("Please enter your name");
            return;
        }
        if (age.equals("0")) {
            view.showValidationError("Please enter your age");
            return;
        }
        if (currentWeight.equals("0")) {
            view.showValidationError("Please enter your current weight");
            return;
        }
        if (targetWeight.equals("0")) {
            view.showValidationError("Please enter your target weight");
            return;
        }
        if (workoutLevel.isEmpty()) {
            view.showValidationError("Please select your workout level");
            return;
        }
        if (equipment.isEmpty()) {
            view.showValidationError("Please enter your equipment access");
            return;
        }

        // All inputs are valid, navigate to the Home screen
        view.navigateToHome();
    }
}