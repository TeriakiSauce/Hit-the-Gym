package com.example.capstone2024.presenters

import android.content.Context
import android.graphics.drawable.Drawable
import com.example.capstone2024.contracts.ExerciseSessionContract
import com.example.capstone2024.models.Exercise
import java.io.IOException

class ExerciseSessionPresenter(private val view: ExerciseSessionContract.View) :
    ExerciseSessionContract.Presenter {
    override fun loadExerciseSession(exercise: Exercise?) {
        if (exercise != null) {
            val name = exercise.name // Exercise name corresponds to folder name
            val imageName = name.replace(" ", "_") // Corresponds to image directory name
            val instructions = exercise.instructions
            val instructionsText = buildInstructionsText(instructions)

            // Load the first image from assets
            val firstImage = loadImageFromAssets(imageName, 0)

            if (firstImage != null) {
                view.displayExerciseDetails(name, firstImage, instructionsText)
                view.setupSetsTable(4) // Assuming 4 sets, adjust as needed
            } else {
                view.showError("Failed to load exercise image.")
            }
        } else {
            view.showError("Failed to load exercise details.")
        }
    }

    private fun buildInstructionsText(instructions: List<String>?): String {
        if (instructions == null || instructions.isEmpty()) {
            return "No instructions available."
        }
        val instructionsBuilder = StringBuilder()
        for (i in instructions.indices) {
            instructionsBuilder.append((i + 1)).append(". ").append(instructions[i]).append("\n\n")
        }
        return instructionsBuilder.toString()
    }

    private fun loadImageFromAssets(exerciseName: String, imageIndex: Int): Drawable? {
        try {
            val assetManager =
                (view as Context).assets // Ensure your view is a Context or modify accordingly
            val imagePath = "images/$exerciseName/$imageIndex.jpg"
            return Drawable.createFromStream(assetManager.open(imagePath), null)
        } catch (e: IOException) {
            e.printStackTrace()
            return null // Return null if the image is not found or an error occurs
        }
    }
}