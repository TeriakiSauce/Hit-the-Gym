package com.example.capstone2024.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.ui.ProgressActivity;
import com.example.capstone2024.ui.survey.SurveyActivity;
import com.example.capstone2024.ui.workoutplan.WorkoutPlanActivity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {

    //Home Screen Buttons
    private WorkoutPlan workoutPlan;
    private Map<String, WorkoutSession> workoutProgram;
    private ImageButton homeButton;
    private ImageButton progressButton;
    private ImageButton heartButton;
    private ImageButton surveyButton;
    private ImageButton chartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            InputStream exercisesInputStream = getAssets().open("exercises.json");
            workoutPlan = new WorkoutPlan(exercisesInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return; // Exit if data loading fails
        }

        // User input
        Map<String, Object> userInput = new HashMap<>();
        userInput.put("age", 30);
        userInput.put("height", 175);
        userInput.put("weight", 70);
        userInput.put("target_weight", 65);
        userInput.put("workout_days", 5);
        userInput.put("level", "intermediate");
        userInput.put("equipment", "dumbbell");
        userInput.put("availability", 1.5);

        // Generate the workout program
        workoutProgram = workoutPlan.generateWorkoutProgram(userInput);
        homeButton = (ImageButton) findViewById(R.id.button_home);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        progressButton = (ImageButton) findViewById(R.id.button_progress);

        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start SurveyActivity
                Intent intent = new Intent(HomeActivity.this, ProgressActivity.class);
                startActivity(intent);
            }
        });

        chartButton = (ImageButton) findViewById(R.id.button_chart);

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ExerciseActivity
                Intent intent = new Intent(HomeActivity.this, WorkoutPlanActivity.class);
                intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
                startActivity(intent);
            }
        });

        heartButton = (ImageButton) findViewById(R.id.button_heart);

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ExerciseActivity
                Intent intent = new Intent(HomeActivity.this, WorkoutPlanActivity.class);
                intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
                startActivity(intent);
            }
        });

        surveyButton = (ImageButton) findViewById(R.id.button_survey);

        surveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ExerciseActivity
                Intent intent = new Intent(HomeActivity.this, SurveyActivity.class);
                startActivity(intent);
            }
        });

    }
}