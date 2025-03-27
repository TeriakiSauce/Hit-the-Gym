package com.example.capstone2024.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
    private TextView textFontSizeValue;
    private static final String PREFS_NAME = "app_preferences";
    private static final String THEME_KEY = "theme_key";
    private static final String FONT_SIZE_KEY = "font_size_key";

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.theme_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(adapter);

        // Set spinner selection based on saved theme preference
        spinnerTheme.setSelection(themePreference.equals("light") ? 1 : 0);

        // Spinner item selected listener
        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTheme = position == 0 ? "dark" : "light";
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(THEME_KEY, selectedTheme);
                editor.apply();
                setThemeBasedOnPreference(selectedTheme);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Font Size Controls
        textFontSizeValue = findViewById(R.id.text_font_size_value);
        Button buttonSmall = findViewById(R.id.button_small);
        Button buttonMedium = findViewById(R.id.button_medium);
        Button buttonLarge = findViewById(R.id.button_large);

        // Load saved font size preference
        float savedFontSize = sharedPreferences.getFloat(FONT_SIZE_KEY, getResources().getInteger(R.integer.font_size_medium));
        updateFontSize(savedFontSize);

        // Set button click listeners
        buttonSmall.setOnClickListener(v -> setFontSize(R.string.font_size_small, R.integer.font_size_small));
        buttonMedium.setOnClickListener(v -> setFontSize(R.string.font_size_medium, R.integer.font_size_medium));
        buttonLarge.setOnClickListener(v -> setFontSize(R.string.font_size_large, R.integer.font_size_large));
    }

    private void setThemeBasedOnPreference(String themePreference) {
        if (themePreference.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void setFontSize(int labelResId, int sizeResId) {
        float fontSize = getResources().getInteger(sizeResId);
        textFontSizeValue.setText(getString(labelResId));
        textFontSizeValue.setTextSize(fontSize);

        // Save font size preference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(FONT_SIZE_KEY, fontSize);
        editor.apply();
    }

    private void updateFontSize(float fontSize) {
        textFontSizeValue.setTextSize(fontSize);
    }
}
