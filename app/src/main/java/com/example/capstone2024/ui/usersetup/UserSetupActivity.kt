package com.example.capstone2024.ui.usersetup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone2024.R
import com.example.capstone2024.contracts.UserSetupContract
import com.example.capstone2024.presenters.UserSetupPresenter
import com.example.capstone2024.ui.home.HomeActivity

class UserSetupActivity : AppCompatActivity(), UserSetupContract.View {
    private lateinit var presenter: UserSetupContract.Presenter

    // UI components declared as nullable
    private var name: EditText? = null
    private var age: EditText? = null
    private var height: EditText? = null
    private var currentWeight: EditText? = null
    private var targetWeight: EditText? = null
    private var workoutLevelGroup: RadioGroup? = null
    private var targetBodyParts: MutableList<CheckBox>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setup)

        // Initialize views after the layout is set
        name = findViewById(R.id.answer1)
        age = findViewById(R.id.answer2)
        height = findViewById(R.id.answer3)
        currentWeight = findViewById(R.id.answer4)
        targetWeight = findViewById(R.id.answer5)
        workoutLevelGroup = findViewById(R.id.answer6)

        targetBodyParts = mutableListOf(
            findViewById(R.id.answer7Biceps),
            findViewById(R.id.answer7Triceps),
            findViewById(R.id.answer7Chest),
            findViewById(R.id.answer7Abs),
            findViewById(R.id.answer7Shoulders),
            findViewById(R.id.answer7Glutes),
            findViewById(R.id.answer7Legs)
        )

        val submitButton = findViewById<Button>(R.id.submitButton)

        // Initialize presenter
        presenter = UserSetupPresenter(this)

        submitButton.setOnClickListener {
            // Ensure non-null usage or use safe calls where needed
            val nameStr = name?.text.toString()
            val ageStr = age?.text.toString()
            val heightStr = height?.text.toString()
            val currentWeightStr = currentWeight?.text.toString()
            val targetWeightStr = targetWeight?.text.toString()

            // Get selected workout level
            val workoutLevelId = workoutLevelGroup?.checkedRadioButtonId ?: -1
            val selectedWorkoutLevel = if (workoutLevelId != -1) findViewById<RadioButton>(workoutLevelId) else null
            val workoutLevelStr = selectedWorkoutLevel?.text?.toString() ?: ""

            // Get selected body parts
            val selectedBodyParts: MutableList<String> = ArrayList()
            targetBodyParts?.forEach { checkBox ->
                if (checkBox.isChecked) {
                    selectedBodyParts.add(checkBox.text.toString())
                }
            }

            presenter.submitSurvey(
                nameStr,
                ageStr,
                heightStr,
                currentWeightStr,
                targetWeightStr,
                workoutLevelStr,
                selectedBodyParts
            )
        }
    }

    override fun showValidationError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        val intent = Intent(
            this@UserSetupActivity,
            HomeActivity::class.java
        )
        startActivity(intent)
        finish()
    }
}