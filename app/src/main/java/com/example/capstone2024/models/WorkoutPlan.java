package com.example.capstone2024.models;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.*;
import java.util.Set;

public class WorkoutPlan {
    // Attributes
    private Map<String, List<ExerciseSession>> workouts; // Map<DayOfWeek, List<ExerciseSession>>
    private Calendar calendar;

    private JSONArray exercises;

    // Constructor
    public WorkoutPlan(InputStream exercisesInputStream) {
        this.workouts = new HashMap<>();
        this.calendar = new Calendar();
        this.exercises = loadExercises(exercisesInputStream);
    }

    // Getters and Setters
    public Map<String, List<ExerciseSession>> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(Map<String, List<ExerciseSession>> workouts) {
        this.workouts = workouts;
    }

    public GregorianCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * Fetches exercises for a specific day of the week.
     *
     * @param dayOfWeek The day of the week to fetch exercises for (e.g., "Monday").
     * @return A list of ExerciseSession objects for the specified day.
     */
    public List<ExerciseSession> getExercisesForDay(String dayOfWeek) {
        return workouts.getOrDefault(dayOfWeek, new ArrayList<>());
    }

    /**
     * Adds a workout plan to the calendar.
     *
     * @param date The date to add the workout plan to.
     * @param workoutPlan The workout plan to add.
     */
    public void addWorkoutToCalendar(Date date, WorkoutPlan workoutPlan) {
        calendar.addWorkout(date, workoutPlan);
    }

    /**
     * Displays the workout calendar.
     */
    public void displayCalendar() {
        calendar.display();
    }

    // Load exercises from JSON InputStream
    private JSONArray loadExercises(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream)) {
            String jsonStr = scanner.useDelimiter("\\A").next();
            return new JSONArray(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // Classify muscle groups
    private Map<String, java.util.Set<String>> classifyMuscleGroups() {
        java.util.Set<String> largeMuscles = new HashSet<>(Arrays.asList("chest", "back", "quadriceps", "hamstrings", "glutes", "shoulders"));
        java.util.Set<String> mediumMuscles = new HashSet<>(Arrays.asList("biceps", "triceps", "abdominals"));
        java.util.Set<String> smallMuscles = new HashSet<>(Arrays.asList("calves", "forearms", "neck", "adductors", "abductors"));

        Map<String, java.util.Set<String>> muscleGroups = new HashMap<>();
        muscleGroups.put("large", largeMuscles);
        muscleGroups.put("medium", mediumMuscles);
        muscleGroups.put("small", smallMuscles);

        return muscleGroups;
    }

    // Filter exercises based on user parameters
    private List<JSONObject> filterExercisesByParameters(Map<String, Object> userInput) throws JSONException {
        String level = (String) userInput.getOrDefault("level", "beginner");
        String equipment = (String) userInput.getOrDefault("equipment", "bodyweight");
        int age = (int) userInput.getOrDefault("age", 30);
        double cardioRatio = ((int) userInput.get("target_weight") < (int) userInput.get("weight")) ? 0.3 : 0.2;
        java.util.Set<String> blacklist = (age > 50) ? new HashSet<>(Collections.singletonList("deadlift")) : new HashSet<>();

        List<JSONObject> filteredExercises = new ArrayList<>();

        for (int i = 0; i < exercises.length(); i++) {
            JSONObject exercise = exercises.getJSONObject(i);

            // Level filtering
            String exerciseLevel = exercise.optString("level", "beginner");
            if ("advanced".equals(exerciseLevel) && !"advanced".equals(level)) continue;
            if ("intermediate".equals(exerciseLevel) && "beginner".equals(level)) continue;

            // Equipment filtering
            String exerciseEquipment = exercise.optString("equipment", "bodyweight");
            if (!exerciseEquipment.equalsIgnoreCase(equipment)) continue;

            // Blacklist filtering
            String exerciseName = exercise.optString("name", "").toLowerCase();
            if (blacklist.contains(exerciseName)) continue;

            // Cardio vs. strength filtering
            String category = exercise.optString("category", "");
            double randomValue = Math.random();
            if ("cardio".equalsIgnoreCase(category) && randomValue > cardioRatio) continue;
            if ("strength".equalsIgnoreCase(category) && randomValue < cardioRatio) continue;

            filteredExercises.add(exercise);
        }

        return filteredExercises;
    }

    // Filter exercises for a specific muscle group
    private List<JSONObject> filterExercisesByMuscle(List<JSONObject> exercises, java.util.Set<String> muscleGroups) throws JSONException {
        List<JSONObject> filteredExercises = new ArrayList<>();
        for (JSONObject exercise : exercises) {
            JSONArray primaryMusclesArray = exercise.optJSONArray("primaryMuscles");
            if (primaryMusclesArray != null) {
                java.util.Set<String> primaryMuscles = new HashSet<>();
                for (int i = 0; i < primaryMusclesArray.length(); i++) {
                    primaryMuscles.add(primaryMusclesArray.getString(i));
                }
                primaryMuscles.retainAll(muscleGroups);
                if (!primaryMuscles.isEmpty()) {
                    filteredExercises.add(exercise);
                }
            }
        }
        return filteredExercises;
    }

    // Find stretches for specific muscle groups
    private List<JSONObject> findStretches(List<JSONObject> exercises, java.util.Set<String> muscleGroups) throws JSONException {
        List<JSONObject> stretches = new ArrayList<>();
        for (JSONObject exercise : exercises) {
            JSONArray primaryMusclesArray = exercise.optJSONArray("primaryMuscles");
            if (primaryMusclesArray != null) {
                java.util.Set<String> primaryMuscles = new HashSet<>();
                for (int i = 0; i < primaryMusclesArray.length(); i++) {
                    primaryMuscles.add(primaryMusclesArray.getString(i));
                }
                primaryMuscles.retainAll(muscleGroups);
                if (!primaryMuscles.isEmpty() && "stretch".equalsIgnoreCase(exercise.optString("category", ""))) {
                    stretches.add(exercise);
                }
            }
        }
        return stretches;
    }

    // Distribute exercises and stretches per week
    private Map<String, List<JSONObject>> createWeeklyProgram(List<JSONObject> exercises, Map<String, Integer> targetExercises, int workoutDays) throws JSONException {
        Map<String, List<JSONObject>> program = new LinkedHashMap<>();
        for (int i = 0; i < workoutDays; i++) {
            program.put("Day " + (i + 1), new ArrayList<>());
        }

        int dayIndex = 0;

        for (Map.Entry<String, Integer> entry : targetExercises.entrySet()) {
            String muscleGroup = entry.getKey();
            int targetCount = entry.getValue();

            List<JSONObject> muscleExercises = filterExercisesByMuscle(exercises, Collections.singleton(muscleGroup));
            List<JSONObject> muscleStretches = findStretches(exercises, Collections.singleton(muscleGroup));
            Collections.shuffle(muscleExercises);
            Collections.shuffle(muscleStretches);

            List<JSONObject> selectedExercises = muscleExercises.subList(0, Math.min(targetCount, muscleExercises.size()));

            for (JSONObject exercise : selectedExercises) {
                String day = "Day " + ((dayIndex % workoutDays) + 1);
                program.get(day).add(exercise);

                // Add a corresponding stretch
                if (!muscleStretches.isEmpty()) {
                    program.get(day).add(muscleStretches.get(0));
                }
                dayIndex++;
            }
        }

        return program;
    }

    // Generate workout program
    public Map<String, List<JSONObject>> generateWorkoutProgram(Map<String, Object> userInput) throws JSONException {
        int workoutDays = (int) userInput.getOrDefault("workout_days", 3);
        double availability = (double) userInput.getOrDefault("availability", 1.0);

        Map<String, java.util.Set<String>> muscleGroupsMap = classifyMuscleGroups();
        java.util.Set<String> largeMuscles = muscleGroupsMap.get("large");
        java.util.Set<String> mediumMuscles = muscleGroupsMap.get("medium");
        Set<String> smallMuscles = muscleGroupsMap.get("small");

        // Adjust workout targets based on availability
        int baseTarget = Math.max(1, (int) availability);
        Map<String, Integer> targetExercises = new HashMap<>();
        for (String muscle : largeMuscles) {
            targetExercises.put(muscle, 4 * baseTarget);
        }
        for (String muscle : mediumMuscles) {
            targetExercises.put(muscle, 3 * baseTarget);
        }
        for (String muscle : smallMuscles) {
            targetExercises.put(muscle, 2 * baseTarget);
        }

        // Filter exercises based on user parameters
        List<JSONObject> filteredExercises = filterExercisesByParameters(userInput);

        // Create weekly program
        return createWeeklyProgram(filteredExercises, targetExercises, workoutDays);
    }
}
