package com.example.capstone2024.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.HomeContract;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.presenters.HomePresenter;
import com.example.capstone2024.ui.ProgressActivity;
import com.example.capstone2024.ui.SettingsActivity;
import com.example.capstone2024.ui.UserProfileActivity;
import com.example.capstone2024.ui.WorkoutCalendarActivity;
import com.example.capstone2024.ui.usersetup.UserSetupActivity;
import com.example.capstone2024.ui.usersetup.UserStatusActivity;
import com.example.capstone2024.ui.workoutplan.WorkoutPlanActivity;
import com.example.capstone2024.ui.workoutsession.WorkoutSessionActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private ImageButton homeButton, progressButton, heartButton, surveyButton, chartButton, menuIcon;
    private HomeContract.Presenter presenter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "app_preferences";
    private static final String FONT_SIZE_KEY = "font_size_key";

    private TextView titleHome;
    private TextView textWorkout1, textWorkout2, textWorkout3, textWorkout4, textWorkout5;
    private TextView individualWorkoutsTitle, weeklyWorkoutTitle, personalInfoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize UI components
        initializeUI();

        // Initialize Presenter
        presenter = new HomePresenter(this, this);

        // Set up button click listeners
        setButtonListeners();

        // Initialize Navigation Drawer
        setupNavigationDrawer();

        // Initialize Workout Plan
        presenter.initializeWorkoutPlan();
    }

    private void initializeUI() {
        // Bottom Navigation Buttons
        chartButton = findViewById(R.id.button_chart);
        surveyButton = findViewById(R.id.button_survey);

        // Drawer and Navigation View
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu_icon);

        // Initialize Workout Buttons
        initializeWorkoutButtons();

        // Initialize TextViews
        titleHome = findViewById(R.id.title_home);
        textWorkout1 = findViewById(R.id.text_workout1);
        textWorkout2 = findViewById(R.id.text_workout2);
        textWorkout3 = findViewById(R.id.text_workout3);
        textWorkout4 = findViewById(R.id.text_workout4);
        textWorkout5 = findViewById(R.id.text_workout5);
        individualWorkoutsTitle = findViewById(R.id.individual_workouts_title);
        weeklyWorkoutTitle = findViewById(R.id.weekly_workout_title);
        personalInfoTitle = findViewById(R.id.personal_info_title);

        // Apply font sizes
        applyFontSizes();

        // Set up menu icon click listener
        menuIcon.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }
    private void initializeWorkoutButtons() {
        LinearLayout workout1Container = findViewById(R.id.workout_button1);
        LinearLayout workout2Container = findViewById(R.id.workout_button2);
        LinearLayout workout3Container = findViewById(R.id.workout_button3);
        LinearLayout workout4Container = findViewById(R.id.workout_button4);
        LinearLayout workout5Container = findViewById(R.id.workout_button5);

        workout1Container.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, WorkoutSessionActivity.class);
            intent.putExtra("WORKOUT_NAME", "Workout 1");
            startActivity(intent);
        });
        workout2Container.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, WorkoutSessionActivity.class);
            intent.putExtra("WORKOUT_NAME", "Workout 2");
            startActivity(intent);
        });
        workout3Container.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, WorkoutSessionActivity.class);
            intent.putExtra("WORKOUT_NAME", "Workout 3");
            startActivity(intent);
        });
        workout4Container.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, WorkoutSessionActivity.class);
            intent.putExtra("WORKOUT_NAME", "Workout 4");
            startActivity(intent);
        });
        workout5Container.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, WorkoutSessionActivity.class);
            intent.putExtra("WORKOUT_NAME", "Workout 5");
            startActivity(intent);
        });

    }

    private void applyFontSizes() {
        String fontSizePref = sharedPreferences.getString(FONT_SIZE_KEY, "small");

        applyFontSizeToView(titleHome, fontSizePref);
        applyFontSizeToView(textWorkout1, fontSizePref);
        applyFontSizeToView(textWorkout2, fontSizePref);
        applyFontSizeToView(textWorkout3, fontSizePref);
        applyFontSizeToView(textWorkout4, fontSizePref);
        applyFontSizeToView(textWorkout5, fontSizePref);
        applyFontSizeToView(individualWorkoutsTitle, fontSizePref);
        applyFontSizeToView(weeklyWorkoutTitle, fontSizePref);
        applyFontSizeToView(personalInfoTitle, fontSizePref);
    }

    private void applyFontSizeToView(TextView textView, String fontSizePref) {
        if (textView == null) return;

        switch (fontSizePref) {
            case "small":
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.font_size_small));
                break;
            case "medium":
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.font_size_medium));
                break;
            case "large":
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.font_size_large));
                break;
        }
    }

    private void setButtonListeners() {
        chartButton.setOnClickListener(v -> presenter.handleWorkoutPlanNavigation());
        surveyButton.setOnClickListener(v -> presenter.handleSurveyNavigation());
    }

    private void setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_user) {
                    startActivity(new Intent(HomeActivity.this, UserStatusActivity.class));
                } else if (id == R.id.nav_user_setup) {
                    startActivity(new Intent(HomeActivity.this, UserSetupActivity.class));
                } else if (id == R.id.nav_workout_plans) {
                    startActivity(new Intent(HomeActivity.this, WorkoutPlanActivity.class));
                } else if (id == R.id.nav_create_workout) {
                    createCustomWorkout();
                } else if (id == R.id.nav_calendar) {
                    startActivity(new Intent(HomeActivity.this, WorkoutCalendarActivity.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    // Creates a new custom workout and brings the user to it
    private void createCustomWorkout() {
        // Initialize helper
        UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(HomeActivity.this);

        // Retrieve the workout program
        Map<String, WorkoutSessionWithExercises> program = helper.getStoredWorkoutProgram();

        // Get set of used day numbers
        HashSet<Integer> usedDays = new HashSet<>();
        for (String key : program.keySet()) {
            try {
                int dayNum = Integer.parseInt(key.replace("Workout ", "").trim());
                usedDays.add(dayNum);
            } catch (NumberFormatException ignored) {
            }
        }

        // Find the smallest integer not used
        int nextDay = 1;
        while (usedDays.contains(nextDay)) {
            nextDay++;
        }

        // Create a new workout session with the computed day number
        WorkoutSession newSession = new WorkoutSession(nextDay);
        helper.insertWorkoutSession(newSession);

        // Navigate directly to the WorkoutSessionActivity with the new day name
        Intent intent = new Intent(HomeActivity.this, WorkoutSessionActivity.class);
        intent.putExtra("WORKOUT_NAME", "Workout " + nextDay);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyFontSizes();
    }

    // Presenter Callbacks
    @Override
    public void displayWorkoutProgram(Map<String, WorkoutSessionWithExercises> workoutProgram) {
        // Implementation
    }

    @Override
    public void navigateToSurvey() {
        startActivity(new Intent(HomeActivity.this, UserStatusActivity.class));
    }

    @Override
    public void navigateToProgress() {
        startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
    }

    @Override
    public void navigateToWorkoutPlan(Map<String, WorkoutSessionWithExercises> workoutProgram) {
        Intent intent = new Intent(HomeActivity.this, WorkoutPlanActivity.class);
        intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
        startActivity(intent);
    }

    @Override
    public void navigateToWorkoutCalendar(Map<String, WorkoutSessionWithExercises> workoutProgram) {
        Intent intent = new Intent(HomeActivity.this, WorkoutCalendarActivity.class);
        intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}