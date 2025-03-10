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
            String primaryMuscles = exerciseJson.optJSONArray("primaryMuscles").getString(0);
            JSONArray secondaryMusclesArray = exerciseJson.optJSONArray("secondaryMuscles");
            List<String> secondaryMuscles = new ArrayList<>();
            if (secondaryMusclesArray != null) {
                for (int j = 0; j < secondaryMusclesArray.length(); j++) {
                    secondaryMuscles.add(secondaryMusclesArray.getString(j));
                }
            }
            JSONArray instructionsArray = exerciseJson.optJSONArray("instructions");
            List<String> instructions = new ArrayList<>();
            if (instructionsArray != null) {
                for (int j = 0; j < instructionsArray.length(); j++) {
                    instructions.add(instructionsArray.getString(j));
                }
            }
            String mechanic = exerciseJson.optString("mechanic", "");

            // Create Exercise object
            Exercise exercise = new Exercise(name, level, equipment, category, primaryMuscles, secondaryMuscles, instructions, mechanic);
            exercises.add(exercise);
        }
        return exercises;
    }

    public Map<String, WorkoutSession> generateWorkoutProgram(Map<String, Object> userInput) {
        int workoutDays = (int) userInput.getOrDefault("workout_days", 5);

        Map<String, Set<String>> muscleGroupsMap = classifyMuscleGroups();
        Set<String> largeMuscles = muscleGroupsMap.get("large");
        Set<String> mediumMuscles = muscleGroupsMap.get("medium");
        Set<String> smallMuscles = muscleGroupsMap.get("small");

        Map<String, Integer> targetExercises = new HashMap<>();

        for (String muscle : largeMuscles) {
            targetExercises.put(muscle, 2);
        }
        for (String muscle : mediumMuscles) {
            targetExercises.put(muscle, 2);
        }
        for (String muscle : smallMuscles) {
            targetExercises.put(muscle, 1);
        }

        // Filter exercises based on parameters
        List<Exercise> filteredExercises = filterExercisesByParameters(userInput);

        // Create weekly program
        this.workoutProgram = createWeeklyProgram(filteredExercises, targetExercises, workoutDays, userInput);

        return this.workoutProgram;
    }

    /**
     * Create a weekly program based on target exercises and workout days.
     */
    private Map<String, WorkoutSession> createWeeklyProgram(List<Exercise> exercises, Map<String, Integer> targetExercises, int workoutDays, Map<String, Object> userInput) {
        Map<String, WorkoutSession> program = new LinkedHashMap<>();
        for (int i = 0; i < workoutDays; i++) {
            program.put("Day " + (i + 1), new WorkoutSession(new ArrayList<>()));
        }

        // Add one cardio exercise at the beginning of each workout day as warmup
        for (int day = 0; day < workoutDays; day++) {
            WorkoutSession session = program.get("Day " + (day + 1));
            List<Exercise> cardioExercises = new ArrayList<>();
            for (Exercise ex : exercises) {
                if ("cardio".equalsIgnoreCase(ex.getCategory())) {
                    cardioExercises.add(ex);
                }
            }
            if (!cardioExercises.isEmpty()) {
                Collections.shuffle(cardioExercises);
                Exercise warmup = cardioExercises.get(0);
                ExerciseSession warmupSession = new ExerciseSession(warmup, 1, 1, 10);
                session.getExerciseSessions().add(0, warmupSession);
            }
        }

        // Distribute muscle-specific exercises and add stretches.
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
                // Create main exercise session (default 4 sets)
                ExerciseSession exerciseSession = new ExerciseSession(exercise, 4, 1, 10);
                workoutSession.getExerciseSessions().add(exerciseSession);

                // Add a stretch for the muscle group used in compound exercises
                if ("compound".equalsIgnoreCase(exercise.getMechanic())) {
                    if (!muscleStretches.isEmpty()) {
                        Exercise stretchExercise = muscleStretches.get(0);
                        ExerciseSession stretchSession = new ExerciseSession(stretchExercise, 1, 1, 10);
                        workoutSession.getExerciseSessions().add(stretchSession);

                        // For seniors, add an extra stretch
                        int age = (int) userInput.getOrDefault("age", 30);
                        if (age > 65 && muscleStretches.size() > 1) {
                            Exercise extraStretch = muscleStretches.get(1);
                            ExerciseSession extraStretchSession = new ExerciseSession(extraStretch, 1, 1, 10);
                            workoutSession.getExerciseSessions().add(extraStretchSession);
                        }
                    }
                }
                dayIndex++;
            }
        }

        // Ensure one compound exercise per session
        for (int day = 0; day < workoutDays; day++) {
            WorkoutSession session = program.get("Day " + (day + 1));
            boolean hasCompound = false;
            for (ExerciseSession es : session.getExerciseSessions()) {
                if ("compound".equalsIgnoreCase(es.getExercise().getCategory())) {
                    hasCompound = true;
                    break;
                }
            }
            if (!hasCompound) {
                List<Exercise> compoundExercises = new ArrayList<>();
                for (Exercise ex : exercises) {
                    if ("compound".equalsIgnoreCase(ex.getCategory())) {
                        compoundExercises.add(ex);
                    }
                }
                if (!compoundExercises.isEmpty()) {
                    Collections.shuffle(compoundExercises);
                    Exercise compound = compoundExercises.get(0);
                    ExerciseSession compoundSession = new ExerciseSession(compound, 3, 1, 10);
                    session.getExerciseSessions().add(compoundSession);
                }
            }
        }

        return program;
    }

    /**
     * Filters based on user's level, age, available equipment and muscle groups.
     * @param userInput USer input data
     * @return list of compatible exercises
     */
    private List<Exercise> filterExercisesByParameters(Map<String, Object> userInput) {
        String level = ((String) userInput.getOrDefault("level", "beginner")).toLowerCase();
        String equipment = ((String) userInput.getOrDefault("equipment", "bodyweight")).toLowerCase();
        int age = (int) userInput.getOrDefault("age", 30);
        int currentWeight = (int) userInput.getOrDefault("weight", 0);
        int targetWeight = (int) userInput.getOrDefault("target_weight", 0);
        // Determine goal: lose weight if target is less than current weight, else gain weight.
        String weightGoal = (targetWeight < currentWeight) ? "lose" : "gain";
        // Retrieve targeted muscle groups if provided (assumed to be lowercase strings)
        Set<String> targetMuscleGroups = (Set<String>) userInput.getOrDefault("target_muscle_groups", new HashSet<String>());

        List<Exercise> filteredExercises = new ArrayList<>();

        for (Exercise exercise : exercisesList) {
            // Level filtering: Only allow exercises at or below the user's level.
            String exerciseLevel = exercise.getLevel();
            if ("advanced".equals(exerciseLevel) && !"advanced".equals(level))
                continue;
            if ("intermediate".equals(exerciseLevel) && "beginner".equals(level))
                continue;

            // Age filtering:
            if (age < 12 && !exercise.getEquipment().equalsIgnoreCase("bodyweight"))
                continue;
            if (age < 16 && exercise.getEquipment().equalsIgnoreCase("barbell"))
                continue;

            // Equipment filtering:
            if ("bodyweight".equals(equipment)) {
                if (!exercise.getEquipment().equalsIgnoreCase("bodyweight"))
                    continue;
            } else if ("dumbbells".equals(equipment)) {
                if (!(exercise.getEquipment().equalsIgnoreCase("bodyweight") || exercise.getEquipment().equalsIgnoreCase("dumbbells")))
                    continue;
            }

            // Target muscle group filtering:
            if (!targetMuscleGroups.isEmpty()) {
                String primaryMuscles = exercise.getPrimaryMuscles();
                boolean targetsSelected = false;
                if (targetMuscleGroups.contains(primaryMuscles))
                {
                    break;
                }
                if (!targetsSelected)
                    continue;
            }
            filteredExercises.add(exercise);
        }
        return filteredExercises;
    }

    private List<Exercise> filterExercisesByMuscle(List<Exercise> exercises, Set<String> muscleGroups) {
        List<Exercise> filteredExercises = new ArrayList<>();
        for (Exercise exercise : exercises) {
            String primaryMuscle = exercise.getPrimaryMuscles();
            if (primaryMuscle != null && muscleGroups.contains(primaryMuscle.toLowerCase())) {
                filteredExercises.add(exercise);
            }
        }
        return filteredExercises;
    }

    /**
     * Finds stretching exercises for specified muscle groups.
     */
    private List<Exercise> findStretches(List<Exercise> exercises, Set<String> muscleGroups) {
        List<Exercise> stretches = new ArrayList<>();
        for (Exercise exercise : exercises) {
            String primaryMuscle = exercise.getPrimaryMuscles();
            if (primaryMuscle != null
                    && muscleGroups.contains(primaryMuscle.toLowerCase())
                    && "stretching".equalsIgnoreCase(exercise.getCategory())) {
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
