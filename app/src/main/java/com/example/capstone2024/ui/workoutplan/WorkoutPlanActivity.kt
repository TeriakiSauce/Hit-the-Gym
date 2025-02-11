package com.example.capstone2024.ui.workoutplan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone2024.R
import com.example.capstone2024.contracts.WorkoutPlanContract
import com.example.capstone2024.models.WorkoutSession
import com.example.capstone2024.presenters.WorkoutPlanPresenter
import com.example.capstone2024.ui.workoutsession.WorkoutSessionActivity

class WorkoutPlanActivity : AppCompatActivity(), WorkoutPlanContract.View {
    private var daysLayout: LinearLayout? = null
    private lateinit var presenter: WorkoutPlanContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_plan)

        daysLayout = findViewById(R.id.daysLayout)

        // Initialize the presenter
        presenter = WorkoutPlanPresenter(this)

        // Load the workout program
        presenter.loadWorkoutProgram(intent)
    }

    override fun displayWorkoutProgram(workoutProgram: Map<String, WorkoutSession>) {
        daysLayout!!.removeAllViews() // Clear existing views
        for ((day, workoutSession) in workoutProgram) {
            // Inflate the custom card layout
            val dayCard = layoutInflater.inflate(R.layout.day_card, daysLayout, false)

            // Bind data to the views
            val dayNameText = dayCard.findViewById<TextView>(R.id.dayName)
            dayNameText.text = day

            // Set OnClickListener for the card
            dayCard.setOnClickListener { v: View? ->
                val intent =
                    Intent(
                        this@WorkoutPlanActivity,
                        WorkoutSessionActivity::class.java
                    )
                intent.putExtra("DAY_NAME", day)
                intent.putExtra("WORKOUT_SESSION", workoutSession)
                startActivity(intent)
            }

            // Add the card to the layout
            daysLayout!!.addView(dayCard)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}