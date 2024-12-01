package com.example.capstone2024.presenters;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.ExerciseSessionContract;
import com.example.capstone2024.models.Exercise;

import java.util.List;

public class ExerciseSessionPresenter implements ExerciseSessionContract.Presenter {
    private final ExerciseSessionContract.View view;

    public ExerciseSessionPresenter(ExerciseSessionContract.View view) {
        this.view = view;
    }

    @Override
    public void loadExerciseSession(Exercise exercise) {
        if (exercise != null) {
            String name = exercise.getName();
            int placeholderImage = R.drawable.placeholder_image;
            List<String> instructions = exercise.getInstructions();
            String instructionsText = buildInstructionsText(instructions);

            view.displayExerciseDetails(name, placeholderImage, instructionsText);
            view.setupSetsTable(4); // Assuming 4 sets, adjust if needed
        } else {
            view.showError("Failed to load exercise details.");
        }
    }

    private String buildInstructionsText(List<String> instructions) {
        if (instructions == null || instructions.isEmpty()) {
            return "No instructions available.";
        }
        StringBuilder instructionsBuilder = new StringBuilder();
        for (int i = 0; i < instructions.size(); i++) {
            instructionsBuilder.append((i + 1)).append(". ").append(instructions.get(i)).append("\n\n");
        }
        return instructionsBuilder.toString();
    }
}