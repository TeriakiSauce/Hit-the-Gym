package com.example.capstone2024.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone2024.R
import com.example.capstone2024.contracts.HomeContract
import com.example.capstone2024.models.WorkoutSession
import com.example.capstone2024.presenters.HomePresenter
import com.example.capstone2024.ui.ProgressActivity
import com.example.capstone2024.ui.WorkoutCalendarActivity
import com.example.capstone2024.ui.usersetup.UserSetupActivity
import com.example.capstone2024.ui.workoutplan.WorkoutPlanActivity

class HomeActivity : AppCompatActivity(), HomeContract.View {
    private lateinit var homeButton: ImageButton
    private lateinit var progressButton: ImageButton
    private lateinit var heartButton: ImageButton
    private lateinit var surveyButton: ImageButton
    private lateinit var chartButton: ImageButton

    private lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Must be called first

        // Initialize UI after setting the content view
        homeButton = findViewById(R.id.button_home)
        progressButton = findViewById(R.id.button_progress)
        // Note: Check your resource IDs. In your original code, there was a mistake:
        // heartButton should probably be linked to R.id.button_heart instead of R.id.button_chart.
        heartButton = findViewById(R.id.button_heart)
        surveyButton = findViewById(R.id.button_survey)
        chartButton = findViewById(R.id.button_chart)

        // Initialize Presenter
        presenter = HomePresenter(this, this)

        // Set up listeners
        homeButton.setOnClickListener { /* Your code here */ }
        progressButton.setOnClickListener { presenter.handleProgressNavigation() }
        chartButton.setOnClickListener { presenter.handleWorkoutPlanNavigation() }
        heartButton.setOnClickListener { presenter.handleWorkoutCalendarNavigation() }
        surveyButton.setOnClickListener { presenter.handleSurveyNavigation() }

        // Initialize Workout Plan
        presenter.initializeWorkoutPlan()
    }

    override fun displayWorkoutProgram(workoutProgram: Map<String, WorkoutSession>) {
        // Placeholder: Logic for updating UI with workout program if needed
    }

    override fun navigateToSurvey() {
        val intent = Intent(
            this@HomeActivity,
            UserSetupActivity::class.java
        )
        startActivity(intent)
    }

    override fun navigateToProgress() {
        val intent = Intent(
            this@HomeActivity,
            ProgressActivity::class.java
        )
        startActivity(intent)
    }

    override fun navigateToWorkoutPlan(workoutProgram: Map<String, WorkoutSession>) {
        val intent = Intent(
            this@HomeActivity,
            WorkoutPlanActivity::class.java
        )
        intent.putExtra("WORKOUT_PROGRAM", HashMap(workoutProgram))
        startActivity(intent)
    }

    override fun navigateToWorkoutCalendar(workoutProgram: Map<String, WorkoutSession>) {
        val intent = Intent(
            this@HomeActivity,
            WorkoutCalendarActivity::class.java
        )
        intent.putExtra("WORKOUT_PROGRAM", HashMap(workoutProgram))
        startActivity(intent)
    }

    override fun showError(message: String) {
        // Example: Show a Toast
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}