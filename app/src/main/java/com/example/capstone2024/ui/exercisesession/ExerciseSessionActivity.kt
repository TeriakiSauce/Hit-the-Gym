package com.example.capstone2024.ui.exercisesession

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.capstone2024.R
import com.example.capstone2024.contracts.ExerciseSessionContract
import com.example.capstone2024.models.Exercise
import com.example.capstone2024.presenters.ExerciseSessionPresenter
import nl.dionsegijn.konfetti.core.PartyFactory
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Shape.Circle
import nl.dionsegijn.konfetti.core.models.Shape.Square
import nl.dionsegijn.konfetti.xml.KonfettiView
import nl.dionsegijn.konfetti.xml.image.ImageUtil.loadDrawable
import java.io.IOException
import java.util.Arrays
import java.util.concurrent.TimeUnit

class ExerciseSessionActivity : AppCompatActivity(), ExerciseSessionContract.View {
    private var exerciseNameTextView: TextView? = null
    private var exerciseImageView: ImageView? = null
    private lateinit var exerciseInstructionsTextView: TextView
    private var setsTableLayout: TableLayout? = null

    private lateinit var presenter: ExerciseSessionContract.Presenter
    private val imageCycleHandler = Handler()
    private var currentImageIndex = 0 // Tracks which image to display (0 or 1)
    private var exerciseName: String? = null // Used to load images
    private var isCyclingImages = false // To stop the cycling when needed

    private lateinit var konfettiView: KonfettiView
    private var drawableShape: Shape.DrawableShape? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_session)

        // Initialize UI components
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView)
        exerciseImageView = findViewById(R.id.exerciseImageView)
        exerciseInstructionsTextView = findViewById(R.id.exerciseInstructionsTextView)
        setsTableLayout = findViewById(R.id.setsTableLayout)

        val toggleInstructionsButton = findViewById<Button>(R.id.toggleInstructionsButton)

        // Set initial state for the toggle button
        toggleInstructionsButton.setOnClickListener(object : View.OnClickListener {
            private var isExpanded = false

            override fun onClick(v: View) {
                if (isExpanded) {
                    // Collapse instructions
                    exerciseInstructionsTextView.setMaxLines(5)
                    exerciseInstructionsTextView.setEllipsize(TextUtils.TruncateAt.END)
                    toggleInstructionsButton.text = "Show More"
                } else {
                    // Expand instructions
                    exerciseInstructionsTextView.setMaxLines(Int.MAX_VALUE)
                    exerciseInstructionsTextView.setEllipsize(null)
                    toggleInstructionsButton.text = "Show Less"
                }
                isExpanded = !isExpanded
            }
        })

        // Initialize presenter
        presenter = ExerciseSessionPresenter(this)

        // Load exercise data from Intent
        val exercise = intent.getSerializableExtra("EXERCISE") as Exercise?

        if (exercise != null) {
            exerciseName = exercise.name.replace(" ", "_") // Convert exercise name to folder name
        }

        presenter.loadExerciseSession(exercise)
    }

    override fun displayExerciseDetails(
        name: String,
        imageResource: Drawable,
        instructions: String
    ) {
        exerciseNameTextView!!.text = name
        exerciseImageView!!.setImageDrawable(imageResource)
        exerciseInstructionsTextView!!.text = instructions

        startImageCycling()
    }

    private fun startImageCycling() {
        isCyclingImages = true
        imageCycleHandler.postDelayed(object : Runnable {
            override fun run() {
                if (!isCyclingImages) return

                // Load the next image (0 -> 1 -> 0)
                currentImageIndex = (currentImageIndex + 1) % 2

                // Load the image dynamically from assets
                var nextImage: Drawable? = null
                try {
                    nextImage = loadImageFromAssets(exerciseName, currentImageIndex)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
                if (nextImage != null) {
                    exerciseImageView!!.setImageDrawable(nextImage)
                }

                // Schedule the next cycle
                imageCycleHandler.postDelayed(this, 1000) // Change image every 1 second
            }
        }, 1000)
    }

    @Throws(IOException::class)
    private fun loadImageFromAssets(exerciseName: String?, imageIndex: Int): Drawable? {
        try {
            val assetManager = assets
            val imagePath = "images/$exerciseName/$imageIndex.jpg"
            return Drawable.createFromStream(assetManager.open(imagePath), null)
        } catch (e: IOException) {
            val assetManager = assets
            val imagePath = "images/placeholder_image.png"
            return Drawable.createFromStream(assetManager.open(imagePath), null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the image cycling to avoid memory leaks
        isCyclingImages = false
        imageCycleHandler.removeCallbacksAndMessages(null)
    }

    override fun setupSetsTable(numberOfSets: Int) {
        // Initialize progress bar

        val progressBar = findViewById<ProgressBar>(R.id.exerciseProgressBar)
        progressBar.max = numberOfSets // Set the maximum value to the total number of sets
        progressBar.progress = 0 // Start with 0 completed sets

        setsTableLayout!!.removeAllViews() // Clear any existing rows

        // Find the KonfettiView and initialize parameters
        val drawable =
            ContextCompat.getDrawable(applicationContext, R.drawable.ic_heart)
        drawableShape = loadDrawable(drawable!!, true, true)
        konfettiView = findViewById(R.id.konfettiView)
        konfettiView.bringToFront()
        val emitterConfig = Emitter(100L, TimeUnit.MILLISECONDS).max(100)

        // Add table headers
        val headerRow = TableRow(this)
        headerRow.addView(createHeaderTextView("Set Type"))
        headerRow.addView(createHeaderTextView("Reps"))
        headerRow.addView(createHeaderTextView("Weight"))
        headerRow.addView(createHeaderTextView("Complete"))
        setsTableLayout!!.addView(headerRow)

        // Track completed sets
        val completedSets = intArrayOf(0)

        // Add rows for sets
        for (i in 0 until numberOfSets) {
            val setRow = TableRow(this)

            val setTypeInput = createEditText("Set " + (i + 1))
            val repsInput = createEditText("10") // Placeholder values
            val weightInput = createEditText(if (i == 0) "70" else "100") // Placeholder values

            val setNumber = i + 1

            val completionCheckBox = CheckBox(this)
            completionCheckBox.setPadding(8, 8, 8, 8)
            completionCheckBox.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->

                // Get Position of button
                if (isChecked) {
                    val location = IntArray(2)
                    buttonView.getLocationOnScreen(location) // Get button coordinates on screen
                    val x =
                        location[0] + buttonView.width / 2f // Center of the button
                    val y = location[1] + buttonView.height / 2f

                    val party =
                        PartyFactory(emitterConfig)
                            .spread(360)
                            .shapes(
                                Arrays.asList<Shape>(
                                    Square,
                                    Circle,
                                    drawableShape
                                )
                            )
                            .colors(mutableListOf<Int>(0x00FF00))
                            .setSpeedBetween(0f, 30f)
                            .position(
                                Position.Relative(
                                    (x / buttonView.rootView.width).toDouble(),
                                    (y / buttonView.rootView.height).toDouble()
                                )
                            )
                            .build()

                    completedSets[0]++
                    konfettiView.start(party) // Trigger confetti
                    //Toast.makeText(this, "Set " + setNumber + " completed!", Toast.LENGTH_SHORT).show();
                } else {
                    completedSets[0]--
                    //Toast.makeText(this, "Set " + setNumber + " uncompleted!", Toast.LENGTH_SHORT).show();
                }
                // Update progress bar
                progressBar.progress = completedSets[0]
            }

            setRow.addView(setTypeInput)
            setRow.addView(repsInput)
            setRow.addView(weightInput)
            setRow.addView(completionCheckBox)

            setsTableLayout!!.addView(setRow)
        }
    }

    private fun createHeaderTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.textSize = 14f
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        textView.gravity = Gravity.CENTER // Center text horizontally
        return textView
    }

    private fun createEditText(hint: String): EditText {
        val editText = EditText(this)
        editText.hint = hint
        editText.textSize = 14f
        editText.setTextColor(ContextCompat.getColor(this, R.color.white))
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.gray))
        editText.gravity = Gravity.CENTER // Center text horizontally
        return editText
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}