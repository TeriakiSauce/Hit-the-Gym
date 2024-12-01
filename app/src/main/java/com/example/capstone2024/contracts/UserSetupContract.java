package com.example.capstone2024.contracts;

import java.util.List;

public interface UserSetupContract {
    interface View {
        void showValidationError(String message);
        void navigateToHome();
    }

    interface Presenter {
        void submitSurvey(
                String name,
                String age,
                String height,
                String currentWeight,
                String targetWeight,
                String workoutLevel,
                List<String> targetBodyParts
        );
    }
}