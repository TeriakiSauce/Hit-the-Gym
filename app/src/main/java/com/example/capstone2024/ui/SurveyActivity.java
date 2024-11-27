package com.example.capstone2024.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;

public class SurveyActivity extends AppCompatActivity {

    private String nameStr;
    private String ageStr;
    private String heightStr;
    private String currentWeightStr;
    private String targetWeightStr;
    private String workoutLvlStr;
    private ArrayList<String> targetBodyPartsStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // Initialize views
        EditText name = findViewById(R.id.answer1);
        EditText age = findViewById(R.id.answer2);
        EditText height = findViewById(R.id.answer3);
        EditText currentWeight = findViewById(R.id.answer4);
        EditText targetWeight = findViewById(R.id.answer5);
        RadioGroup workoutLvl = findViewById(R.id.answer6);

        ArrayList<CheckBox> targetBodyParts = new ArrayList<>();
        targetBodyParts.add(findViewById(R.id.answer7Biceps));
        targetBodyParts.add(findViewById(R.id.answer7Triceps));
        targetBodyParts.add(findViewById(R.id.answer7Chest));
        targetBodyParts.add(findViewById(R.id.answer7Abs));
        targetBodyParts.add(findViewById(R.id.answer7Shoulders));
        targetBodyParts.add(findViewById(R.id.answer7Glutes));
        targetBodyParts.add(findViewById(R.id.answer7Legs));

        Button submitButton = findViewById(R.id.submitButton);

        // Submit button listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get values of text answers
                nameStr = name.getText().toString();
                ageStr = age.getText().toString();
                heightStr = height.getText().toString();
                currentWeightStr = currentWeight.getText().toString();
                targetWeightStr = targetWeight.getText().toString();

                // Get value of workout level answer
                int workoutLvlId = workoutLvl.getCheckedRadioButtonId();
                RadioButton workoutLvlButton = findViewById(workoutLvlId);
                workoutLvlStr = "";
                if (workoutLvlButton != null) {
                    workoutLvlStr = workoutLvlButton.getText().toString();
                }

                // Get values of targeted body part answers
                targetBodyPartsStr = new ArrayList<>();
                for (CheckBox answer : targetBodyParts) {
                    targetBodyPartsStr.add(answer.getText().toString());
                }
                //Log.d("targetBodyPartsStr", "Target Body Parts List: " + targetBodyPartsStr.toString());

                // Prevent submitting if questions haven't been answered
                if (nameStr.isEmpty()) {
                    Toast.makeText(SurveyActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (ageStr.isEmpty()) {
                    Toast.makeText(SurveyActivity.this, "Please enter your age", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (heightStr.isEmpty()) {
                    Toast.makeText(SurveyActivity.this, "Please enter your height", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (currentWeightStr.isEmpty()) {
                    Toast.makeText(SurveyActivity.this, "Please enter your current weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (targetWeightStr.isEmpty()) {
                    Toast.makeText(SurveyActivity.this, "Please enter your target weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (workoutLvlStr.isEmpty()) {
                    Toast.makeText(SurveyActivity.this, "Please enter your selected workout level", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent = new Intent(SurveyActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}