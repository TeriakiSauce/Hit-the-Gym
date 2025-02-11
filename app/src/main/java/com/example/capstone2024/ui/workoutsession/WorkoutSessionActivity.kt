package com.example.capstone2024.ui.workoutsession

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone2024.R
import com.example.capstone2024.contracts.WorkoutSessionContract
import com.example.capstone2024.models.ExerciseSession
import com.example.capstone2024.presenters.WorkoutSessionPresenter
import com.example.capstone2024.ui.exercisesession.ExerciseSessionActivity


class WorkoutSessionActivity : AppCompatActivity(), WorkoutSessionContract.View {
    private var dayName: String? = null
    private var exercisesLayout: LinearLayout? = null
    private lateinit var presenter: WorkoutSessionContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_session)

        exercisesLayout = findViewById(R.id.exercisesLayout)

        // Initialize the presenter
        presenter = WorkoutSessionPresenter(this)

        // Retrieve data from Intent
        dayName = intent.getStringExtra("DAY_NAME")
        val workoutSessionData: Any? = intent.getSerializableExtra("WORKOUT_SESSION")

        // Load workout session
        presenter.loadWorkoutSession(dayName, workoutSessionData)
    }

    override fun displayExercises(exerciseSessions: List<ExerciseSession>) {
        exercisesLayout!!.removeAllViews() // Clear any existing views

        for (exerciseSession in exerciseSessions) {
            val exercise = exerciseSession.exercise
            val name = exercise.name
            val sets = exerciseSession.sets
            val reps = exerciseSession.reps
            val progressPercentage = calculateProgress(exerciseSession)

            // Inflate the custom layout
            val exerciseCard =
                layoutInflater.inflate(R.layout.exercise_card, exercisesLayout, false)

            // Bind data to views
            val exerciseName = exerciseCard.findViewById<TextView>(R.id.exerciseName)
            val exerciseDetails = exerciseCard.findViewById<TextView>(R.id.exerciseDetails)
            val progressBar = exerciseCard.findViewById<ProgressBar>(R.id.progressBar)
            val progressText = exerciseCard.findViewById<TextView>(R.id.progressText)

            exerciseName.text = name
            exerciseDetails.text = "Sets: $sets | Reps: $reps"
            progressBar.progress = progressPercentage
            progressText.text = "$progressPercentage%"


            // Set the OnClickListener for the card
            exerciseCard.setOnClickListener { v: View? ->
                val intent =
                    Intent(
                        this@WorkoutSessionActivity,
                        ExerciseSessionActivity::class.java
                    )
                intent.putExtra("EXERCISE", exercise)
                startActivity(intent)
            }

            // Add the card to the layout
            exercisesLayout!!.addView(exerciseCard)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun calculateProgress(session: ExerciseSession): Int {
        val totalSets = session.sets
        val completedSets = session.completedSets // Assuming this method exists
        return ((completedSets / totalSets.toFloat()) * 100).toInt()
    }
}