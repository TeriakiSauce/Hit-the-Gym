package com.example.capstone2024.ui.exercisesession;

import static nl.dionsegijn.konfetti.core.Position.Relative;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_session);

        // Initialize UI components
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView);
        exerciseImageView = findViewById(R.id.exerciseImageView);
        exerciseInstructionsTextView = findViewById(R.id.exerciseInstructionsTextView);
        setsTableLayout = findViewById(R.id.setsTableLayout);

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
                Drawable nextImage = loadImageFromAssets(exerciseName, currentImageIndex);
                if (nextImage != null) {
                    exerciseImageView.setImageDrawable(nextImage);
                }

                // Schedule the next cycle
                imageCycleHandler.postDelayed(this, 1000); // Change image every 1 second
            }
        }, 1000);
    }

    private Drawable loadImageFromAssets(String exerciseName, int imageIndex) {
        try {
            AssetManager assetManager = getAssets();
            String imagePath = "images/" + exerciseName + "/" + imageIndex + ".jpg";
            return Drawable.createFromStream(assetManager.open(imagePath), null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the image cycling to avoid memory leaks
        isCyclingImages = false;
        imageCycleHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void setupSetsTable(int numberOfSets) {

        // Initialize progress bar
        ProgressBar progressBar = findViewById(R.id.exerciseProgressBar);
        progressBar.setMax(numberOfSets); // Set the maximum value to the total number of sets
        progressBar.setProgress(0); // Start with 0 completed sets

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
                    Toast.makeText(this, "Set " + setNumber + " completed!", Toast.LENGTH_SHORT).show();
                } else {
                    completedSets[0]--;
                    Toast.makeText(this, "Set " + setNumber + " uncompleted!", Toast.LENGTH_SHORT).show();
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

    @NonNull
    private CheckBox getCheckBox(int i, int[] completedSets, ProgressBar progressBar) {
        final int setNumber = i + 1;

        CheckBox completionCheckBox = new CheckBox(this);
        completionCheckBox.setPadding(8, 8, 8, 8);
        completionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                completedSets[0]++;
                Toast.makeText(this, "Set " + setNumber + " completed!", Toast.LENGTH_SHORT).show();
            } else {
                completedSets[0]--;
                Toast.makeText(this, "Set " + setNumber + " uncompleted!", Toast.LENGTH_SHORT).show();
            }

            // Update progress bar
            progressBar.setProgress(completedSets[0]);
        });
        return completionCheckBox;
    }

    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }

    private EditText createEditText(String hint) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setPadding(8, 8, 8, 8);
        return editText;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}