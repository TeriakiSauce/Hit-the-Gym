package com.example.capstone2024.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.capstone2024.R;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerTheme;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "app_preferences";
    private static final String THEME_KEY = "theme_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        // Set up window insets for padding (Edge to Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String themePreference = sharedPreferences.getString(THEME_KEY, "dark");

        // Apply the saved theme at startup
        setThemeBasedOnPreference(themePreference);

        // Initialize Spinner (Theme Selector)
        spinnerTheme = findViewById(R.id.spinner_theme);

        // Set up spinner with Light and Dark mode options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.theme_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(adapter);

        // Set spinner selection based on saved theme preference
        if (themePreference.equals("light")) {
            spinnerTheme.setSelection(1);
        } else {
            spinnerTheme.setSelection(0);
        }

        // Spinner item selected listener
        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                String selectedTheme = position == 0 ? "dark" : "light";
                // Save theme preference
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(THEME_KEY, selectedTheme);
                editor.apply();
                // Apply theme change
                setThemeBasedOnPreference(selectedTheme);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setThemeBasedOnPreference(String themePreference) {
        if (themePreference.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Light mode
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Dark mode
        }
    }
}
