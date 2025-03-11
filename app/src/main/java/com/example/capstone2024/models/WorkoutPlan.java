package com.example.capstone2024.models;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.*;
import java.util.Set;

public class WorkoutPlan {

    private JSONArray exercisesJsonArray;
    private List<Exercise> exercisesList; // List of Exercise objects
    private Map<String, WorkoutSession> workoutProgram; // Map of day to WorkoutSession
    private UserSetupDatabaseHelper databaseHelper;

    public WorkoutPlan(InputStream exercisesInputStream) throws JSONException {
        this.exercisesJsonArray = loadExercises(exercisesInputStream);
        this.exercisesList = parseExercises(exercisesJsonArray);
    }

    // Load exercises from JSON
    private JSONArray loadExercises(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream)) {
            String jsonStr = scanner.useDelimiter("\\A").next();
            return new JSONArray(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // Parse JSONArray into List<Exercise>
    private List<Exercise> parseExercises(JSONArray jsonArray) throws JSONException {
        List<Exercise> exercises = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject exerciseJson = jsonArray.getJSONObject(i);
            String name = exerciseJson.optString("name");
            String level = exerciseJson.optString("level", "beginner");
            String equipment = exerciseJson.optString("equipment", "bodyweight");
            String category = exerciseJson.optString("category", "");
            JSONArray primaryMusclesArray = exerciseJson.optJSONArray("primaryMuscles");
            List<String> primaryMuscles = new ArrayList<>();
            if (primaryMusclesArray != null) {
                for (int j = 0; j < primaryMusclesArray.length(); j++) {
                    primaryMuscles.add(primaryMusclesArray.getString(j));
                }
            }
            JSONArray instructionsArray = exerciseJson.optJSONArray("instructions");
            List<String> instructions = new ArrayList<>();
            if (instructionsArray != null) {
                for (int j = 0; j < instructionsArray.length(); j++) {
                    instructions.add(instructionsArray.getString(j));
                }
            }
            // Create Exercise object
            Exercise exercise = new Exercise(name, level, equipment, category, primaryMuscles, instructions);
            exercises.add(exercise);
        }
        return exercises;
    }

    public Map<String, WorkoutSession> generateWorkoutProgram(Map<String, Object> userInput) {
        int workoutDays = (int) userInput.getOrDefault("workout_days", 3);
        double availability = (double) userInput.getOrDefault("availability", 1.0);

        Map<String, Set<String>> muscleGroupsMap = classifyMuscleGroups();
        Set<String> largeMuscles = muscleGroupsMap.get("large");
        Set<String> mediumMuscles = muscleGroupsMap.get("medium");
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

        // Filter exercises based on parameters
        List<Exercise> filteredExercises = filterExercisesByParameters(userInput);

        // Create weekly program
        this.workoutProgram = createWeeklyProgram(filteredExercises, targetExercises, workoutDays);

        return this.workoutProgram;
    }


    private Map<String, WorkoutSession> createWeeklyProgram(List<Exercise> exercises, Map<String, Integer> targetExercises, int workoutDays) {
        Map<String, WorkoutSession> program = new LinkedHashMap<>();
        for (int i = 0; i < workoutDays; i++) {
            program.put("Day " + (i + 1), new WorkoutSession(new ArrayList<>()));
        }

        int dayIndex = 0;

        for (Map.Entry<String, Integer> entry : targetExercises.entrySet()) {
            String muscleGroup = entry.getKey();
            int targetCount = entry.getValue();

            List<Exercise> muscleExercises = filterExercisesByMuscle(exercises, Collections.singleton(muscleGroup));
            List<Exercise> muscleStretches = findStretches(exercises, Collections.singleton(muscleGroup));
            Collections.shuffle(muscleExercises);
            Collections.shuffle(muscleStretches);

            List<Exercise> selectedExercises = muscleExercises.subList(0, Math.min(targetCount, muscleExercises.size()));

            for (Exercise exercise : selectedExercises) {
                String day = "Day " + ((dayIndex % workoutDays) + 1);
                WorkoutSession workoutSession = program.get(day);

                // Create ExerciseSession with default sets and rest time
                ExerciseSession exerciseSession = new ExerciseSession(exercise, 4, 1, 10); // 4 sets, 1 minute rest
                workoutSession.getExerciseSessions().add(exerciseSession);

                // Optionally add a corresponding stretch
                if (!muscleStretches.isEmpty()) {
                    Exercise stretchExercise = muscleStretches.get(0);
                    ExerciseSession stretchSession = new ExerciseSession(stretchExercise, 1, 1, 10);
                    workoutSession.getExerciseSessions().add(stretchSession);
                }
                dayIndex++;
            }
        }

        return program;
    }

    // Update filter methods to work with Exercise objects
    private List<Exercise> filterExercisesByParameters(Map<String, Object> userInput) {
        String level = (String) userInput.getOrDefault("level", "beginner");
        String equipment = (String) userInput.getOrDefault("equipment", "bodyweight");
        int age = (int) userInput.getOrDefault("age", 30);
        double cardioRatio = ((int) userInput.get("target_weight") < (int) userInput.get("weight")) ? 0.3 : 0.2;
        Set<String> blacklist = (age > 50) ? new HashSet<>(Collections.singletonList("deadlift")) : new HashSet<>();

        List<Exercise> filteredExercises = new ArrayList<>();

        for (Exercise exercise : exercisesList) {
            // Level filtering
            String exerciseLevel = exercise.getLevel();
            if ("advanced".equals(exerciseLevel) && !"advanced".equals(level)) continue;
            if ("intermediate".equals(exerciseLevel) && "beginner".equals(level)) continue;

            // Equipment filtering
            String exerciseEquipment = exercise.getEquipment();
            if (!exerciseEquipment.equalsIgnoreCase(equipment)) continue;

            // Blacklist filtering
            String exerciseName = exercise.getName().toLowerCase();
            if (blacklist.contains(exerciseName)) continue;

            // Cardio vs. strength filtering
            String category = exercise.getCategory();
            double randomValue = Math.random();
            if ("cardio".equalsIgnoreCase(category) && randomValue > cardioRatio) continue;
            if ("strength".equalsIgnoreCase(category) && randomValue < cardioRatio) continue;

            filteredExercises.add(exercise);
        }

        return filteredExercises;
    }

    private List<Exercise> filterExercisesByMuscle(List<Exercise> exercises, Set<String> muscleGroups) {
        List<Exercise> filteredExercises = new ArrayList<>();
        for (Exercise exercise : exercises) {
            List<String> primaryMuscles = exercise.getPrimaryMuscles();
            Set<String> muscleIntersection = new HashSet<>(primaryMuscles);
            muscleIntersection.retainAll(muscleGroups);
            if (!muscleIntersection.isEmpty()) {
                filteredExercises.add(exercise);
            }
        }
        return filteredExercises;
    }

    private List<Exercise> findStretches(List<Exercise> exercises, Set<String> muscleGroups) {
        List<Exercise> stretches = new ArrayList<>();
        for (Exercise exercise : exercises) {
            List<String> primaryMuscles = exercise.getPrimaryMuscles();
            Set<String> muscleIntersection = new HashSet<>(primaryMuscles);
            muscleIntersection.retainAll(muscleGroups);
            if (!muscleIntersection.isEmpty() && "stretch".equalsIgnoreCase(exercise.getCategory())) {
                stretches.add(exercise);
            }
        }
        return stretches;
    }

    private Map<String, Set<String>> classifyMuscleGroups() {
        Set<String> largeMuscles = new HashSet<>(Arrays.asList("chest", "back", "quadriceps", "hamstrings", "glutes", "shoulders"));
        Set<String> mediumMuscles = new HashSet<>(Arrays.asList("biceps", "triceps", "abdominals"));
        Set<String> smallMuscles = new HashSet<>(Arrays.asList("calves", "forearms", "neck", "adductors", "abductors"));

        Map<String, Set<String>> muscleGroups = new HashMap<>();
        muscleGroups.put("large", largeMuscles);
        muscleGroups.put("medium", mediumMuscles);
        muscleGroups.put("small", smallMuscles);

        return muscleGroups;
    }

    public List<Exercise> getExercisesList() {
        return exercisesList;
    }
}
