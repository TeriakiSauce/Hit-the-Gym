package com.example.capstone2024.ui.exercisesession;

import static nl.dionsegijn.konfetti.core.Position.Relative;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.ExerciseSessionContract;
import com.example.capstone2024.models.Exercise;
import com.example.capstone2024.presenters.ExerciseSessionPresenter;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.xml.image.ImageUtil;

public class ExerciseSessionActivity extends AppCompatActivity implements ExerciseSessionContract.View {
    private TextView exerciseNameTextView;
    private ImageView exerciseImageView;
    private TextView exerciseInstructionsTextView;
    private TableLayout setsTableLayout;

    private ExerciseSessionContract.Presenter presenter;
    private Handler imageCycleHandler = new Handler();
    private int currentImageIndex = 0; // Tracks which image to display (0 or 1)
    private String exerciseName; // Used to load images
    private boolean isCyclingImages = false; // To stop the cycling when needed

    private KonfettiView konfettiView = null;
    private Shape.DrawableShape drawableShape = null;

    private ProgressBar restProgressBar;
    private CountDownTimer currentRestTimer; // Global timer for all sets
    private TextView timeRemainingTextView;
    private FrameLayout timerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_session);

        // Initialize UI components
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView);
        exerciseImageView = findViewById(R.id.exerciseImageView);
        exerciseInstructionsTextView = findViewById(R.id.exerciseInstructionsTextView);
        setsTableLayout = findViewById(R.id.setsTableLayout);
        timerContainer = findViewById(R.id.timerContainer);
        restProgressBar = findViewById(R.id.restProgressBar);
        timeRemainingTextView = findViewById(R.id.timeRemainingTextView);

        Button toggleInstructionsButton = findViewById(R.id.toggleInstructionsButton);

        // Set initial state for the toggle button
        toggleInstructionsButton.setOnClickListener(new View.OnClickListener() {
            private boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // Collapse instructions
                    exerciseInstructionsTextView.setMaxLines(5);
                    exerciseInstructionsTextView.setEllipsize(TextUtils.TruncateAt.END);
                    toggleInstructionsButton.setText("Show More");
                } else {
                    // Expand instructions
                    exerciseInstructionsTextView.setMaxLines(Integer.MAX_VALUE);
                    exerciseInstructionsTextView.setEllipsize(null);
                    toggleInstructionsButton.setText("Show Less");
                }
                isExpanded = !isExpanded;
            }
        });

        // Initialize presenter
        presenter = new ExerciseSessionPresenter(this);

        // Load exercise data from Intent
        Exercise exercise = (Exercise) getIntent().getSerializableExtra("EXERCISE");

        if (exercise != null) {
            exerciseName = exercise.getName().replace(" ", "_"); // Convert exercise name to folder name
        }

        presenter.loadExerciseSession(exercise);
    }

    @Override
    public void displayExerciseDetails(String name, Drawable imageResource, String instructions) {
        exerciseNameTextView.setText(name);
        exerciseImageView.setImageDrawable(imageResource);
        exerciseInstructionsTextView.setText(instructions);

        startImageCycling();
    }

    private void startImageCycling() {
        isCyclingImages = true;
        imageCycleHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isCyclingImages) return;

                // Load the next image (0 -> 1 -> 0)
                currentImageIndex = (currentImageIndex + 1) % 2;

                // Load the image dynamically from assets
                Drawable nextImage = null;
                try {
                    nextImage = loadImageFromAssets(exerciseName, currentImageIndex);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (nextImage != null) {
                    exerciseImageView.setImageDrawable(nextImage);
                }

                // Schedule the next cycle
                imageCycleHandler.postDelayed(this, 1000); // Change image every 1 second
            }
        }, 1000);
    }

    private Drawable loadImageFromAssets(String exerciseName, int imageIndex) throws IOException {
        try {
            AssetManager assetManager = getAssets();
            String imagePath = "images/" + exerciseName + "/" + imageIndex + ".jpg";
            return Drawable.createFromStream(assetManager.open(imagePath), null);
        } catch (IOException e) {
            AssetManager assetManager = getAssets();
            String imagePath = "images/placeholder_image.png";
            return Drawable.createFromStream(assetManager.open(imagePath), null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the image cycling
        isCyclingImages = false;
        imageCycleHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void setupSetsTable(int numberOfSets) {

        // Initialize progress bar
        ProgressBar progressBar = findViewById(R.id.exerciseProgressBar);
        progressBar.setMax(numberOfSets); // Set the maximum value to the total number of sets
        progressBar.setProgress(0); // Start with 0 completed sets

        restProgressBar = findViewById(R.id.restProgressBar);

        setsTableLayout.removeAllViews(); // Clear any existing rows

        // Find the KonfettiView and initialize parameters
        final Drawable drawable =
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
        drawableShape = ImageUtil.loadDrawable(drawable, true, true);
        konfettiView = findViewById(R.id.konfettiView);
        konfettiView.bringToFront();
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);

        // Add table headers
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createHeaderTextView("Set Type"));
        headerRow.addView(createHeaderTextView("Reps"));
        headerRow.addView(createHeaderTextView("Weight"));
        headerRow.addView(createHeaderTextView("Complete"));
        setsTableLayout.addView(headerRow);

        // Track completed sets
        final int[] completedSets = {0};

        // Add rows for sets
        for (int i = 0; i < numberOfSets; i++) {
            TableRow setRow = new TableRow(this);

            EditText setTypeInput = createEditText("Set " + (i + 1));
            EditText repsInput = createEditText("Reps");
            EditText weightInput = createEditText("Weight");

            final int setNumber = i + 1;

            CheckBox completionCheckBox = new CheckBox(this);
            completionCheckBox.setPadding(8, 8, 8, 8);
            completionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                // Get Position of button
                if (isChecked) {
                    resetGlobalTimer();
                    int[] location = new int[2];
                    buttonView.getLocationOnScreen(location); // Get button coordinates on screen
                    float x = location[0] + buttonView.getWidth() / 2f; // Center of the button
                    float y = location[1] + buttonView.getHeight() / 2f;

                    Party party =
                            new PartyFactory(emitterConfig)
                                    .spread(360)
                                    .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                                    .colors(Arrays.asList(0x00FF00))
                                    .setSpeedBetween(0f, 30f)
                                    .position(new Relative(x / buttonView.getRootView().getWidth(), y / buttonView.getRootView().getHeight()))
                                    .build();

                    completedSets[0]++;
                    konfettiView.start(party); // Trigger confetti
                    //Toast.makeText(this, "Set " + setNumber + " completed!", Toast.LENGTH_SHORT).show();
                } else {
                    completedSets[0]--;
                    // Check if any checkboxes remain checked.

                    timerContainer.setVisibility(View.GONE);
                    restProgressBar.setProgress(100);
                    timeRemainingTextView.setText("02:00");


                    //Toast.makeText(this, "Set " + setNumber + " uncompleted!", Toast.LENGTH_SHORT).show();
                }
                // Update progress bar
                progressBar.setProgress(completedSets[0]);
            });

            setRow.addView(setTypeInput);
            setRow.addView(repsInput);
            setRow.addView(weightInput);
            setRow.addView(completionCheckBox);

            setsTableLayout.addView(setRow);
        }
    }
    private void resetGlobalTimer() {
        // Cancel any existing timer
        if (currentRestTimer != null) {
            currentRestTimer.cancel();
        }
        // Show the timer container
        timerContainer.setVisibility(View.VISIBLE);

        // Start a new 2-minute countdown
        currentRestTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Calculate progress (100 means full, 0 means empty)
                int progress = (int) ((millisUntilFinished / 120000f) * 100);
                restProgressBar.setProgress(progress);

                // Format remaining time as mm:ss and update the TextView
                int minutes = (int) (millisUntilFinished / 60000);
                int seconds = (int) ((millisUntilFinished % 60000) / 1000);
                timeRemainingTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }
            @Override
            public void onFinish() {
                restProgressBar.setProgress(0);
                timeRemainingTextView.setText("Time for the next set!");
                // Optionally hide the timer if finished:
                // timerContainer.setVisibility(View.GONE);
            }
        }.start();
    }
    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(14);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        textView.setGravity(Gravity.CENTER);   // Center text horizontally
        return textView;
    }

    private EditText createEditText(String hint) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setTextSize(14);
        editText.setTextColor(ContextCompat.getColor(this, R.color.white));
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.gray));
        editText.setGravity(Gravity.CENTER);   // Center text horizontally
        return editText;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}