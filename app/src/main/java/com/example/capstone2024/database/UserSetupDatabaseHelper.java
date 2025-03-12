package com.example.capstone2024.database;

import android.content.Context;

import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.UserSetup;
import com.example.capstone2024.models.WorkoutSession;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserSetupDatabaseHelper {

    private UserSetupDao userDao;
    private ExerciseDao exerciseDao;
    private WorkoutSessionDao workoutSessionDao;
    private ExerciseSessionDao exerciseSessionDao;
    private Context context;
    public UserSetupDatabaseHelper(Context context) {
        // Initialize DAO by getting it from the AppDatabase instance
        UserSetupDatabase db = UserSetupDatabaseClient.getInstance(context).getAppDatabase();
        userDao = db.userSetupDao();
        exerciseDao = db.exerciseDao();
        workoutSessionDao = db.workoutSessionDao();
        exerciseSessionDao = db.exerciseSessionDao();
        this.context = context;
    }

    public void insertUser(UserSetup user) {
        new Thread(() -> userDao.insert(user)).start(); // Use a background thread to insert data into the database
    }

    public List<UserSetup> getAllUsersSync() {
        return userDao.getAllUsers();
    }

    public List<UserSetup> fetchAllUsersSync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<UserSetup>> future = executor.submit(() -> {
            // Use the synchronous DAO method defined in your helper
            UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(context);
            return helper.getAllUsersSync();
        });

        try {
            // This will block until the result is available, but it's off the main thread.
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }
    public void insertExercise(Exercise exercise) {
        new Thread(() -> exerciseDao.insertExercise(exercise)).start();
    }
    public List<Exercise> getAllExercisesSync() {
        return exerciseDao.getAllExercises();
    }
    public List<Exercise> fetchAllExercisesSync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Exercise>> future = executor.submit(() -> {
            // Use the synchronous DAO method defined in your helper
            UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(context);
            return helper.getAllExercisesSync();
        });

        try {
            // This will block until the result is available, but it's off the main thread.
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }
    public int insertWorkoutSession(WorkoutSession workoutSession) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Long> future = executor.submit(() -> workoutSessionDao.insertWorkoutSession(workoutSession));
        try {
            // Wait for the insertion to complete and get ID
            long generatedId = future.get();
            workoutSession.setId((int) generatedId);
            return (int) generatedId;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        } finally {
            executor.shutdown();
        }
    }

    public Map<String, WorkoutSessionWithExercises> getStoredWorkoutProgram() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<WorkoutSessionWithExercises>> future = executor.submit(() ->
                workoutSessionDao.getWorkoutSessionsWithExercises()
        );
        try {
            List<WorkoutSessionWithExercises> sessions = future.get();
            // Convert the list into a map using a key like "Day X"
            Map<String, WorkoutSessionWithExercises> program = new LinkedHashMap<>();
            for (WorkoutSessionWithExercises ws : sessions) {
                // Assuming your WorkoutSession entity has a method getDayNumber() that returns the day number.
                String key = "Day " + ws.workoutSession.getDayNumber();
                program.put(key, ws);
            }
            return program;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new LinkedHashMap<>();
        } finally {
            executor.shutdown();
        }
    }

    public WorkoutSessionWithExercises getWorkoutSessionWithExercisesByDay(int dayNumber) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<WorkoutSessionWithExercises> future = executor.submit(() ->
                workoutSessionDao.getWorkoutSessionWithExercisesByDay(dayNumber)
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }

    public void insertExerciseSession(ExerciseSession exerciseSession) {
        new Thread(() -> exerciseSessionDao.insertExerciseSession(exerciseSession)).start();
    }
}

