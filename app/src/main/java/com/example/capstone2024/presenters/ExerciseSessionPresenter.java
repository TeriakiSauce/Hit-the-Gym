package com.example.capstone2024.presenters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.ExerciseSessionContract;
import com.example.capstone2024.database.ExerciseSessionWithExercise;
import com.example.capstone2024.models.Exercise;

import java.io.IOException;
import java.util.List;

public class ExerciseSessionPresenter implements ExerciseSessionContract.Presenter {
    private final ExerciseSessionContract.View view;

    public ExerciseSessionPresenter(ExerciseSessionContract.View view) {
        this.view = view;
    }

    @Override
    public void loadExerciseSession(ExerciseSessionWithExercise session) {
        Exercise exercise = session.getExercise();

        if (exercise != null) {
            String name = exercise.getName(); // Exercise name corresponds to folder name
            String imageName = name.replace(" ", "_"); // Corresponds to image directory name
            List<String> instructions = exercise.getInstructions();
            String instructionsText = buildInstructionsText(instructions);

            // Load the first image from assets
            Drawable firstImage = loadImageFromAssets(imageName, 0);

            if (firstImage != null) {
                view.displayExerciseDetails(name, firstImage, instructionsText);
                view.setupSetsTable(4); // Assuming 4 sets, adjust as needed
            } else {
                view.showError("Failed to load exercise image.");
            }
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

    private Drawable loadImageFromAssets(String exerciseName, int imageIndex) {
        try {
            AssetManager assetManager = ((Context)view).getAssets(); // Ensure your view is a Context or modify accordingly
            String imagePath = "images/" + exerciseName + "/" + imageIndex + ".jpg";
            return Drawable.createFromStream(assetManager.open(imagePath), null);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if the image is not found or an error occurs
        }
    }
}