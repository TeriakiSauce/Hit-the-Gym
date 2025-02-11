package com.example.capstone2024.presenters

import com.example.capstone2024.contracts.UserSetupContract

class UserSetupPresenter(private val view: UserSetupContract.View) : UserSetupContract.Presenter {
    override fun submitSurvey(
        name: String,
        age: String,
        height: String,
        currentWeight: String,
        targetWeight: String,
        workoutLevel: String,
        targetBodyParts: List<String>
    ) {
        // Validate inputs
        if (name.isEmpty()) {
            view.showValidationError("Please enter your name")
            return
        }
        if (age.isEmpty()) {
            view.showValidationError("Please enter your age")
            return
        }
        if (height.isEmpty()) {
            view.showValidationError("Please enter your height")
            return
        }
        if (currentWeight.isEmpty()) {
            view.showValidationError("Please enter your current weight")
            return
        }
        if (targetWeight.isEmpty()) {
            view.showValidationError("Please enter your target weight")
            return
        }
        if (workoutLevel.isEmpty()) {
            view.showValidationError("Please select your workout level")
            return
        }

        // All inputs are valid, navigate to the Home screen
        view.navigateToHome()
    }
}