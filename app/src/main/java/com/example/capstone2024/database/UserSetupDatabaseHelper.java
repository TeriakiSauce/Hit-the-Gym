package com.example.capstone2024.database;

import android.content.Context;

import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.models.ExerciseSession;
import com.example.capstone2024.models.UserSetup;
import com.example.capstone2024.models.WorkoutSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserSetupDatabaseHelper {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
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
        executorService.execute(() -> userDao.insert(user));
    }

    public List<UserSetup> getAllUsersSync() {
        return userDao.getAllUsers();
    }

    public List<UserSetup> fetchAllUsersSync() {
        Future<List<UserSetup>> future = executorService.submit(() -> getAllUsersSync());
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void insertExercise(Exercise exercise) {
        executorService.execute(() -> exerciseDao.insertExercise(exercise));
    }
    public List<Exercise> getAllExercisesSync() {
        return exerciseDao.getAllExercises();
    }
    public List<Exercise> fetchAllExercisesSync() {
        Future<List<Exercise>> future = executorService.submit(() -> getAllExercisesSync());
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
    public int insertWorkoutSession(WorkoutSession workoutSession) {
        Future<Long> future = executorService.submit(() -> workoutSessionDao.insertWorkoutSession(workoutSession));
        try {
            long generatedId = future.get();
            workoutSession.setId((int) generatedId);
            return (int) generatedId;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Map<String, WorkoutSessionWithExercises> getStoredWorkoutProgram() {
        Future<List<WorkoutSessionWithExercises>> future = executorService.submit(() ->
                workoutSessionDao.getWorkoutSessionsWithExercises()
        );
        try {
            List<WorkoutSessionWithExercises> sessions = future.get();
            Map<String, WorkoutSessionWithExercises> program = new LinkedHashMap<>();
            for (WorkoutSessionWithExercises ws : sessions) {
                // Assuming your WorkoutSession entity has a method getDayNumber()
                String key = "Workout " + ws.workoutSession.getDayNumber();
                program.put(key, ws);
            }
            return program;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new LinkedHashMap<>();
        }
    }

    public WorkoutSessionWithExercises getWorkoutSessionWithExercisesByDay(int dayNumber) {
        Future<WorkoutSessionWithExercises> future = executorService.submit(() ->
                workoutSessionDao.getWorkoutSessionWithExercisesByDay(dayNumber)
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertExerciseSession(ExerciseSession exerciseSession) {
        executorService.execute(() ->
                exerciseSessionDao.insertExerciseSession(exerciseSession)
        );
    }

    public void updateExerciseSession(ExerciseSession exerciseSession) {
        executorService.execute(() ->
                exerciseSessionDao.updateExerciseSession(exerciseSession)
        );
    }

    public ExerciseSessionWithExercise getExerciseSessionWithExerciseById(int id) {
        Future<ExerciseSessionWithExercise> future = executorService.submit(() ->
                exerciseSessionDao.getExerciseSessionWithExerciseById(id)
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteExerciseSession(int exerciseSessionId) {
        executorService.execute(() ->
                exerciseSessionDao.deleteExerciseSessionById(exerciseSessionId)
        );
    }

    public List<Exercise> searchExercises(String query) {
        Future<List<Exercise>> future = executorService.submit(() ->
                exerciseDao.searchExercises(query)
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

