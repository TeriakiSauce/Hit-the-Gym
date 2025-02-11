package com.example.capstone2024.models

import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import java.util.Collections
import java.util.Locale
import java.util.Scanner
import kotlin.collections.Set
import kotlin.math.max
import kotlin.math.min

class WorkoutPlan(exercisesInputStream: InputStream) {
    private val exercisesJsonArray: JSONArray
    private val exercisesList: List<Exercise> // List of Exercise objects
    private var workoutProgram: Map<String, WorkoutSession>? = null // Map of day to WorkoutSession

    init {
        this.exercisesJsonArray = loadExercises(exercisesInputStream)
        this.exercisesList = parseExercises(exercisesJsonArray)
    }

    // Load exercises from JSON
    private fun loadExercises(inputStream: InputStream): JSONArray {
        try {
            Scanner(inputStream).use { scanner ->
                val jsonStr = scanner.useDelimiter("\\A").next()
                return JSONArray(jsonStr)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return JSONArray()
        }
    }

    // Parse JSONArray into List<Exercise>
    @Throws(JSONException::class)
    private fun parseExercises(jsonArray: JSONArray): List<Exercise> {
        val exercises: MutableList<Exercise> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            val exerciseJson = jsonArray.getJSONObject(i)
            val name = exerciseJson.optString("name")
            val level = exerciseJson.optString("level", "beginner")
            val equipment = exerciseJson.optString("equipment", "bodyweight")
            val category = exerciseJson.optString("category", "")
            val primaryMusclesArray = exerciseJson.optJSONArray("primaryMuscles")
            val primaryMuscles: MutableList<String> = ArrayList()
            if (primaryMusclesArray != null) {
                for (j in 0 until primaryMusclesArray.length()) {
                    primaryMuscles.add(primaryMusclesArray.getString(j))
                }
            }
            val instructionsArray = exerciseJson.optJSONArray("instructions")
            val instructions: MutableList<String> = ArrayList()
            if (instructionsArray != null) {
                for (j in 0 until instructionsArray.length()) {
                    instructions.add(instructionsArray.getString(j))
                }
            }
            // Create Exercise object
            val exercise = Exercise(name, level, equipment, category, primaryMuscles, instructions)
            exercises.add(exercise)
        }
        return exercises
    }

    fun generateWorkoutProgram(userInput: Map<String?, Any>): Map<String, WorkoutSession> {
        val workoutDays = userInput.getOrDefault("workout_days", 3) as Int
        val availability = userInput.getOrDefault("availability", 1.0) as Double

        val muscleGroupsMap = classifyMuscleGroups()
        val largeMuscles = muscleGroupsMap["large"]
        val mediumMuscles = muscleGroupsMap["medium"]
        val smallMuscles = muscleGroupsMap["small"]

        // Adjust workout targets based on availability
        val baseTarget = max(1.0, availability.toInt().toDouble()).toInt()
        val targetExercises: MutableMap<String, Int> = HashMap()
        for (muscle in largeMuscles!!) {
            targetExercises[muscle] = 4 * baseTarget
        }
        for (muscle in mediumMuscles!!) {
            targetExercises[muscle] = 3 * baseTarget
        }
        for (muscle in smallMuscles!!) {
            targetExercises[muscle] = 2 * baseTarget
        }

        // Filter exercises based on parameters
        val filteredExercises = filterExercisesByParameters(userInput)

        // Create weekly program
        this.workoutProgram = createWeeklyProgram(filteredExercises, targetExercises, workoutDays)

        return workoutProgram!!
    }


    private fun createWeeklyProgram(
        exercises: List<Exercise>,
        targetExercises: Map<String, Int>,
        workoutDays: Int
    ): Map<String, WorkoutSession> {
        val program: MutableMap<String, WorkoutSession> = LinkedHashMap()
        for (i in 0 until workoutDays) {
            program["Day " + (i + 1)] = WorkoutSession(ArrayList())
        }

        var dayIndex = 0

        for ((muscleGroup, targetCount) in targetExercises) {
            val muscleExercises = filterExercisesByMuscle(exercises, setOf(muscleGroup))
            val muscleStretches = findStretches(exercises, setOf(muscleGroup))
            Collections.shuffle(muscleExercises)
            Collections.shuffle(muscleStretches)

            val selectedExercises = muscleExercises.subList(
                0,
                min(targetCount.toDouble(), muscleExercises.size.toDouble()).toInt()
            )

            for (exercise in selectedExercises) {
                val day = "Day " + ((dayIndex % workoutDays) + 1)
                val workoutSession = program[day]

                // Create ExerciseSession with default sets and rest time
                val exerciseSession = ExerciseSession(exercise, 4, 1, 10) // 4 sets, 1 minute rest
                workoutSession!!.exerciseSessions.add(exerciseSession)

                // Optionally add a corresponding stretch
                if (!muscleStretches.isEmpty()) {
                    val stretchExercise = muscleStretches[0]
                    val stretchSession = ExerciseSession(stretchExercise!!, 1, 1, 10)
                    workoutSession.exerciseSessions.add(stretchSession)
                }
                dayIndex++
            }
        }

        return program
    }

    // Update filter methods to work with Exercise objects
    private fun filterExercisesByParameters(userInput: Map<String?, Any>): List<Exercise> {
        val level = userInput.getOrDefault("level", "beginner") as String
        val equipment = userInput.getOrDefault("equipment", "bodyweight") as String
        val age = userInput.getOrDefault("age", 30) as Int
        val cardioRatio: Double = if ((userInput["target_weight"] as? Int ?: 0) < (userInput["weight"] as? Int ?: 0)) 0.3 else 0.2
        val blacklist: Set<String> = if ((age > 50)) HashSet(listOf("deadlift")) else HashSet()

        val filteredExercises: MutableList<Exercise> = ArrayList()

        for (exercise in exercisesList) {
            // Level filtering
            val exerciseLevel = exercise.level
            if ("advanced" == exerciseLevel && "advanced" != level) continue
            if ("intermediate" == exerciseLevel && "beginner" == level) continue

            // Equipment filtering
            val exerciseEquipment = exercise.equipment
            if (!exerciseEquipment.equals(equipment, ignoreCase = true)) continue

            // Blacklist filtering
            val exerciseName = exercise.name.lowercase(Locale.getDefault())
            if (blacklist.contains(exerciseName)) continue

            // Cardio vs. strength filtering
            val category = exercise.category
            val randomValue = Math.random()
            if ("cardio".equals(category, ignoreCase = true) && randomValue > cardioRatio) continue
            if ("strength".equals(
                    category,
                    ignoreCase = true
                ) && randomValue < cardioRatio
            ) continue

            filteredExercises.add(exercise)
        }

        return filteredExercises
    }

    private fun filterExercisesByMuscle(
        exercises: List<Exercise>,
        muscleGroups: Set<String>
    ): List<Exercise> {
        val filteredExercises: MutableList<Exercise> = ArrayList()
        for (exercise in exercises) {
            val primaryMuscles = exercise.primaryMuscles
            val muscleIntersection: MutableSet<String> = HashSet(primaryMuscles)
            muscleIntersection.retainAll(muscleGroups)
            if (!muscleIntersection.isEmpty()) {
                filteredExercises.add(exercise)
            }
        }
        return filteredExercises
    }

    private fun findStretches(
        exercises: List<Exercise>,
        muscleGroups: Set<String>
    ): List<Exercise?> {
        val stretches: MutableList<Exercise?> = ArrayList()
        for (exercise in exercises) {
            val primaryMuscles = exercise.primaryMuscles
            val muscleIntersection: MutableSet<String> = HashSet(primaryMuscles)
            muscleIntersection.retainAll(muscleGroups)
            if (!muscleIntersection.isEmpty() && "stretch".equals(
                    exercise.category,
                    ignoreCase = true
                )
            ) {
                stretches.add(exercise)
            }
        }
        return stretches
    }

    private fun classifyMuscleGroups(): Map<String, Set<String>> {
        val largeMuscles: Set<String> = HashSet(
            mutableListOf(
                "chest",
                "back",
                "quadriceps",
                "hamstrings",
                "glutes",
                "shoulders"
            )
        )
        val mediumMuscles: Set<String> = HashSet(mutableListOf("biceps", "triceps", "abdominals"))
        val smallMuscles: Set<String> =
            HashSet(mutableListOf("calves", "forearms", "neck", "adductors", "abductors"))

        val muscleGroups: MutableMap<String, Set<String>> = HashMap()
        muscleGroups["large"] = largeMuscles
        muscleGroups["medium"] = mediumMuscles
        muscleGroups["small"] = smallMuscles

        return muscleGroups
    }
}
