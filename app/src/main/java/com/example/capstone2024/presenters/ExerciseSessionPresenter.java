package com.example.capstone2024.presenters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.ExerciseSessionContract;
import com.example.capstone2024.database.ExerciseSessionWithExercise;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.Set;

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
        ExerciseSession exerciseSession = session.getExerciseSession();

        if (exercise != null && exerciseSession != null) {
            // Generate the list of Set details from the ExerciseSession
            List<Set> setDetails = exerciseSession.getSetDetails();

            // Optionally, load other details (e.g., image, instructions)
            String name = exercise.getName();
            Drawable firstImage = loadImageFromAssets(name.replace(" ", "_"), 0);
            String instructionsText = buildInstructionsText(exercise);

            // Update the view with exercise details and the set details
            view.displayExerciseDetails(name, firstImage, instructionsText);
            view.setupSetsTable(setDetails);
        } else {
            view.showError("Failed to load exercise session details.");
        }
    }

    private String buildInstructionsText(Exercise exercise) {
        if (exercise == null) {
            return "No instructions available.";
        }
        String equipment = exercise.getEquipment();
        String category = exercise.getCategory();
        String primaryMuscles = exercise.getPrimaryMuscles();
        String secondaryMuscles = exercise.getSecondaryMuscles().toString();
        String mechanic = exercise.getMechanic();
        String level = exercise.getLevel();

        List<String> instructions = exercise.getInstructions();
        // Build header with exercise details
        StringBuilder finalInstructionsBuilder = new StringBuilder();

        if (instructions == null || instructions.isEmpty()) {
            finalInstructionsBuilder.append("No instructions available.");
        } else {
            for (int i = 0; i < instructions.size(); i++) {
                finalInstructionsBuilder.append((i + 1))
                        .append(". ")
                        .append(instructions.get(i))
                        .append("\n\n");
            }
        }
        finalInstructionsBuilder.append("Equipment: ").append(capitalize(equipment)).append("\n");
        finalInstructionsBuilder.append("Category: ").append(capitalize(category)).append("\n");
        finalInstructionsBuilder.append("Primary Muscles: ").append(capitalize(primaryMuscles)).append("\n");
        finalInstructionsBuilder.append("Secondary Muscles: ").append(capitalize(secondaryMuscles)).append("\n");
        finalInstructionsBuilder.append("Mechanic: ").append(capitalize(mechanic)).append("\n");
        finalInstructionsBuilder.append("Level: ").append(capitalize(level)).append("\n\n");
        return finalInstructionsBuilder.toString();
    }

    // Helper method to capitalize first letter
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
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