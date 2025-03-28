package com.example.capstone2024.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.HomeContract;
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
//import com.example.capstone2024.ui.createworkout.CreateWorkoutActivity;
//import com.example.capstone2024.ui.settings.SettingsActivity;
import com.example.capstone2024.ui.workoutsession.WorkoutSessionActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private ImageButton homeButton, progressButton, heartButton, surveyButton, chartButton, menuIcon;
    private HomeContract.Presenter presenter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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


        // Set up menu icon click listener
        menuIcon.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void setButtonListeners() {
        chartButton.setOnClickListener(v -> presenter.handleWorkoutPlanNavigation());
        surveyButton.setOnClickListener(v -> presenter.handleSurveyNavigation());
    }

    private void setupNavigationDrawer() {
        // Navigation Item Click Listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_user) {
                    startActivity(new Intent(HomeActivity.this, UserStatusActivity.class));
                //} else if (id == R.id.nav_progress) {
                //    startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
                } else if (id == R.id.nav_user_setup) {
                    startActivity(new Intent(HomeActivity.this, UserSetupActivity.class));
                } else if (id == R.id.nav_workout_plans) {
                    startActivity(new Intent(HomeActivity.this, WorkoutPlanActivity.class));
                } else if (id == R.id.nav_create_workout) {
                    startActivity(new Intent(HomeActivity.this, WorkoutSessionActivity.class));
                } else if (id == R.id.nav_calendar) {
                    startActivity(new Intent(HomeActivity.this, WorkoutCalendarActivity.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                }

                // Close the drawer after selection
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Set up Drawer Toggle
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawerLayout.addDrawerListener(toggle);
        //toggle.syncState();
    }

    //@Override
    //public void onBackPressed() {
    //    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
    //        drawerLayout.closeDrawer(GravityCompat.START);
    //    } else {
    //        super.onBackPressed();
    //    }
    //}

    // Presenter Callbacks
    @Override
    public void displayWorkoutProgram(Map<String, WorkoutSessionWithExercises> workoutProgram) {
        // Logic for updating UI with workout program (if needed)
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
