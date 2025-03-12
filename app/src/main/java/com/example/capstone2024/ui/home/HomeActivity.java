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
import com.google.android.material.navigation.NavigationView;

import com.example.capstone2024.R;
import com.example.capstone2024.contracts.HomeContract;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.presenters.HomePresenter;
import com.example.capstone2024.ui.ProgressActivity;
import com.example.capstone2024.ui.WorkoutCalendarActivity;
import com.example.capstone2024.ui.usersetup.UserSetupActivity;
import com.example.capstone2024.ui.workoutplan.WorkoutPlanActivity;
//import com.example.capstone2024.ui.settings.SettingsActivity;
//import com.example.capstone2024.ui.user.UserProfileActivity;
//import com.example.capstone2024.ui.auth.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private ImageButton homeButton;
    private ImageButton progressButton;
    private ImageButton heartButton;
    private ImageButton surveyButton;
    private ImageButton chartButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private HomeContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI
        homeButton = findViewById(R.id.button_home);
        progressButton = findViewById(R.id.button_progress);
        chartButton = findViewById(R.id.button_chart);
        heartButton = findViewById(R.id.button_heart);
        surveyButton = findViewById(R.id.button_survey);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Initialize Presenter
        presenter = new HomePresenter(this, this);

        // Set up button listeners
        homeButton.setOnClickListener(v -> {});
        progressButton.setOnClickListener(v -> presenter.handleProgressNavigation());
        chartButton.setOnClickListener(v -> presenter.handleWorkoutPlanNavigation());
        heartButton.setOnClickListener(v -> presenter.handleWorkoutCalendarNavigation());
        surveyButton.setOnClickListener(v -> presenter.handleSurveyNavigation());

        // Initialize Workout Plan
        presenter.initializeWorkoutPlan();

        // Set up navigation drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_user) {
                    startActivity(new Intent(HomeActivity.this, UserSetupActivity.class));
                } else if (id == R.id.nav_progress) {
                    startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
                } else if (id == R.id.nav_workout_plans) {
                    startActivity(new Intent(HomeActivity.this, WorkoutPlanActivity.class));
                } else if (id == R.id.nav_create_workout) {
                    // Assuming CreateWorkoutActivity exists
                    startActivity(new Intent(HomeActivity.this, WorkoutCalendarActivity.class));
                //} else if (id == R.id.nav_settings) {
                //    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public void displayWorkoutProgram(Map<String, WorkoutSession> workoutProgram) {
        // Placeholder: Logic for updating UI with workout program if needed
    }

    @Override
    public void navigateToSurvey() {
        startActivity(new Intent(HomeActivity.this, UserSetupActivity.class));
    }

    @Override
    public void navigateToProgress() {
        startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
    }

    @Override
    public void navigateToWorkoutPlan(Map<String, WorkoutSession> workoutProgram) {
        Intent intent = new Intent(HomeActivity.this, WorkoutPlanActivity.class);
        intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
        startActivity(intent);
    }

    @Override
    public void navigateToWorkoutCalendar(Map<String, WorkoutSession> workoutProgram) {
        Intent intent = new Intent(HomeActivity.this, WorkoutCalendarActivity.class);
        intent.putExtra("WORKOUT_PROGRAM", new HashMap<>(workoutProgram));
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
