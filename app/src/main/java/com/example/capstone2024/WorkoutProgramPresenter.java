package com.example.capstone2024;

import org.json.JSONObject;
import java.util.List;
import java.util.Map;

public class WorkoutProgramPresenter {

    private WorkoutProgramView view;
    private WorkoutProgramGenerator model;

    public WorkoutProgramPresenter(WorkoutProgramView view, WorkoutProgramGenerator model) {
        this.view = view;
        this.model = model;
    }

    public void generateAndDisplayWorkoutProgram(Map<String, Object> userInput) {
        try {
            Map<String, List<JSONObject>> workoutProgram = model.generateWorkoutProgram(userInput);

            // Prepare to display the program string
            StringBuilder programDisplay = new StringBuilder();
            for (Map.Entry<String, List<JSONObject>> entry : workoutProgram.entrySet()) {
                String day = entry.getKey();
                List<JSONObject> exercises = entry.getValue();
                programDisplay.append("\n").append(day).append(":\n");
                for (JSONObject exercise : exercises) {
                    String name = exercise.optString("name");
                    String category = exercise.optString("category");
                    String primaryMuscles = exercise.optJSONArray("primaryMuscles").toString();
                    programDisplay.append("  - ").append(name).append(" (").append(category).append(") ").append(primaryMuscles).append("\n");
                }
            }

            view.displayWorkoutProgram(programDisplay.toString());

        } catch (Exception e) {
            view.showError("Failed to generate workout program.");
            e.printStackTrace();
        }
    }
}