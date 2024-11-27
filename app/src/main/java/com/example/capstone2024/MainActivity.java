package com.example.capstone2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    //Home Screen Buttons
    private ImageButton homeButton;
    private ImageButton progressButton;
    private ImageButton heartButton;
    private ImageButton surveyButton;
    private ImageButton chartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
                startActivity(intent);
            }
        });

        chartButton = (ImageButton) findViewById(R.id.button_chart);

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start SurveyActivity
                Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
                startActivity(intent);
            }
        });

        heartButton = (ImageButton) findViewById(R.id.button_heart);

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ExerciseActivity
                Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
                startActivity(intent);
            }
        });

        surveyButton = (ImageButton) findViewById(R.id.button_survey);

        surveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ExerciseActivity
                Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                startActivity(intent);
            }
        });

    }
}
