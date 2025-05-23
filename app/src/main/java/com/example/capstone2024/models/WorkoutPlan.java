package com.example.capstone2024.models;
import android.content.Context;
import android.widget.Toast;

import com.example.capstone2024.database.ExerciseSessionWithExercise;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.database.WorkoutSessionWithExercises;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.*;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkoutPlan {

    private JSONArray exercisesJsonArray;
    private List<Exercise> exercisesList; // List of Exercise objects
    private Map<String, WorkoutSessionWithExercises> workoutProgram; // Map of day to WorkoutSession
    private UserSetupDatabaseHelper databaseHelper;
    private Context context;

    private Map<String, Object> userInput;

    public WorkoutPlan(InputStream exercisesInputStream, Context context, Map<String, Object> userInput) throws JSONException {
        this.exercisesJsonArray = loadExercises(exercisesInputStream);
        this.exercisesList = parseExercises(exercisesJsonArray);
        this.context = context;
        this.userInput = userInput;
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
        UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(context);
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
            helper.insertExercise(exercise);
        }
        List<Exercise> exercisesList = helper.fetchAllExercisesSync();
        Collections.shuffle(exercisesList);
        return exercisesList;
    }

    public Map<String, WorkoutSessionWithExercises> generateWorkoutProgram() {

        int workoutDays = 5;

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
        this.workoutProgram = createWeeklyProgram(filteredExercises, targetExercises, workoutDays);

        return this.workoutProgram;
    }

    /**
     * Create a weekly program based on target exercises and workout days.
     */
    private Map<String, WorkoutSessionWithExercises> createWeeklyProgram(List<Exercise> exercises, Map<String, Integer> targetExercises, int workoutDays) {
        Map<String, WorkoutSessionWithExercises> program = new LinkedHashMap<>();
        for (int i = 0; i < workoutDays; i++) {
            WorkoutSession tempSession = new WorkoutSession(i + 1);
            program.put("Workout " + (i + 1), new WorkoutSessionWithExercises(tempSession));
        }

        for (int day = 0; day < workoutDays; day++) {
            WorkoutSessionWithExercises session = program.get("Workout " + (day + 1));
            List<Exercise> cardioExercises = new ArrayList<>();
            // Collect all cardio exercises
            for (Exercise ex : exercises) {
                if ("cardio".equalsIgnoreCase(ex.getCategory())) {
                    cardioExercises.add(ex);
                }
            }
            // Choose a random cardio exercise and add it to the beginning of the session
            if (!cardioExercises.isEmpty()) {
                Collections.shuffle(cardioExercises);
                Exercise warmup = cardioExercises.get(0);
                System.out.println(warmup);
                ExerciseSession warmupSession = new ExerciseSession(session.getId(), warmup);
                ExerciseSessionWithExercise warmupExercise = new ExerciseSessionWithExercise(warmupSession, warmup);
                session.getExerciseSessions().add(0, warmupExercise);
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
                String day = "Workout " + ((dayIndex % workoutDays) + 1);
                WorkoutSessionWithExercises workoutSession = program.get(day);
                // Create main exercise session (default 4 sets)
                ExerciseSession exerciseSession = new ExerciseSession(workoutSession.getId(), exercise);
                ExerciseSessionWithExercise exerciseExercise = new ExerciseSessionWithExercise(exerciseSession, exercise);
                workoutSession.getExerciseSessions().add(exerciseExercise);

                // Add a stretch for the muscle group used in compound exercises
                if ("compound".equalsIgnoreCase(exercise.getMechanic())) {
                    if (!muscleStretches.isEmpty()) {
                        Exercise stretch = muscleStretches.get(0);
                        ExerciseSession stretchSession = new ExerciseSession(workoutSession.getId(),stretch);
                        ExerciseSessionWithExercise stretchExercise = new ExerciseSessionWithExercise(stretchSession, stretch);
                        workoutSession.getExerciseSessions().add(stretchExercise);

                        // For seniors, add an extra stretch
                        int age = Integer.parseInt(userInput.getOrDefault("age", "30").toString());
                        if (age > 65 && muscleStretches.size() > 1) {
                            Exercise extraStretch = muscleStretches.get(1);
                            ExerciseSession extraStretchSession = new ExerciseSession(workoutSession.getId(), extraStretch);
                            ExerciseSessionWithExercise extraStretchExercise = new ExerciseSessionWithExercise(extraStretchSession, extraStretch);
                            workoutSession.getExerciseSessions().add(extraStretchExercise);
                        }
                    }
                }
                dayIndex++;
            }
        }

        // Ensure one compound exercise per session
        for (int day = 0; day < workoutDays; day++) {
            WorkoutSessionWithExercises session = program.get("Workout " + (day + 1));
            boolean hasCompound = false;
            for (ExerciseSessionWithExercise es : session.getExerciseSessions()) {
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
                    ExerciseSession compoundSession = new ExerciseSession(session.getId(), compound);
                    ExerciseSessionWithExercise compoundExercise = new ExerciseSessionWithExercise(compoundSession, compound);
                    session.getExerciseSessions().add(compoundExercise);
                }
            }
            UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(context);
            int workoutSessionId = helper.insertWorkoutSession(session.workoutSession);
            for (ExerciseSessionWithExercise es : session.getExerciseSessions()) {
                System.out.println(es + " " + workoutSessionId);
                es.getExerciseSession().setWorkoutSessionId(workoutSessionId);
                helper.insertExerciseSession(es.getExerciseSession());
            }
        }
        // Ensure each workout session has at least 5 exercises
        for (int day = 0; day < workoutDays; day++) {
            WorkoutSessionWithExercises session = program.get("Workout " + (day + 1));
            while (session.getExerciseSessions().size() < 5) {
                // Create a list of additional strength exercises not already in the session
                List<Exercise> availableExercises = new ArrayList<>(exercises);
                availableExercises.removeIf(ex -> session.getExerciseSessions().stream()
                        .anyMatch(es -> es.getExercise().getId() == ex.getId()));
                if (availableExercises.isEmpty()) {
                    break; // No extra exercises available
                }
                // Shuffle and add one extra exercise
                Collections.shuffle(availableExercises);
                Exercise additional = availableExercises.get(0);
                ExerciseSession additionalSession = new ExerciseSession(session.getId(), additional);
                ExerciseSessionWithExercise additionalExercise = new ExerciseSessionWithExercise(additionalSession, additional);
                session.getExerciseSessions().add(additionalExercise);
            }
        }
        System.out.println(program);
        return program;
    }

    /**
     * Filters based on user's level, age, available equipment and muscle groups.
     * @param userInput USer input data
     * @return list of compatible exercises
     */
    private List<Exercise> filterExercisesByParameters(Map<String, Object> userInput) {
        // Get User Input Data
        String level = ((String) userInput.getOrDefault("level", "beginner")).toLowerCase();
        System.out.println(level);
        String equipment = ((String) userInput.getOrDefault("equipment", "bodyweight")).toLowerCase();
        System.out.println(equipment);
        int age = Integer.parseInt(userInput.getOrDefault("age", "30").toString());
        System.out.println(age);
        int currentWeight = Integer.parseInt(userInput.getOrDefault("current_weight", "0").toString());
        System.out.println(currentWeight);
        int targetWeight = Integer.parseInt(userInput.getOrDefault("target_weight", "0").toString());
        System.out.println(targetWeight);
        // Determine goal: lose weight if target is less than current weight, else gain weight.
        String weightGoal = (targetWeight < currentWeight) ? "lose" : "gain";
        // Retrieve targeted muscle groups if provided (assumed to be lowercase strings)
        ArrayList<String> targetMuscleGroups = (ArrayList<String>) userInput.getOrDefault("targetBodyParts", new ArrayList<String>());
        // Cast all muscle groups to lower case
        targetMuscleGroups = targetMuscleGroups.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Exercise> filteredExercises = new ArrayList<>();

        for (Exercise exercise : exercisesList) {
            // Level filtering: Only allow exercises at or below the user's level.
            String exerciseLevel = exercise.getLevel();
            if ("advanced".equals(exerciseLevel) && !"advanced".equals(level))
                continue;
            if ("intermediate".equals(exerciseLevel) && "beginner".equals(level))
                continue;

            // Age filtering:
            if (age < 12 && !exercise.getEquipment().equalsIgnoreCase("body only"))
                continue;
            if (age < 16 && exercise.getEquipment().equalsIgnoreCase("barbell"))
                continue;

            // Equipment filtering:
            if ("bodyweight".equals(equipment)) {
                if (!exercise.getEquipment().equalsIgnoreCase("body only"))
                    continue;
            } else if ("only dumbbells".equals(equipment)) {
                if (!(exercise.getEquipment().equalsIgnoreCase("body only") || exercise.getEquipment().equalsIgnoreCase("dumbbell")))
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
