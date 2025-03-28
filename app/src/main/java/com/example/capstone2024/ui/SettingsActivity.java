package com.example.capstone2024.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone2024.R;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerTheme;
    private SharedPreferences sharedPreferences;
    private TextView textFontSizeValue, textTitle, textFontSizeLabel, textThemeLabel;
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
        String fontSizePreference = sharedPreferences.getString(FONT_SIZE_KEY, "medium");

        // Apply the saved theme at startup
        setThemeBasedOnPreference(themePreference);

        // Initialize UI components
        spinnerTheme = findViewById(R.id.spinner_theme);
        textFontSizeValue = findViewById(R.id.text_font_size_value);
        textTitle = findViewById(R.id.title_settings);
        textFontSizeLabel = findViewById(R.id.text_font_size_label);
        textThemeLabel = findViewById(R.id.text_theme_label);

        Button buttonSmall = findViewById(R.id.button_small);
        Button buttonMedium = findViewById(R.id.button_medium);
        Button buttonLarge = findViewById(R.id.button_large);

        // Set up theme spinner
        setupThemeSpinner(themePreference);

        // Set up font size controls
        setupFontSizeControls(fontSizePreference);

        // Apply initial font sizes
        updateFontSizeViews(fontSizePreference);
    }

    private void setupThemeSpinner(String themePreference) {
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
    }

    private void setupFontSizeControls(String fontSizePreference) {
        Button buttonSmall = findViewById(R.id.button_small);
        Button buttonMedium = findViewById(R.id.button_medium);
        Button buttonLarge = findViewById(R.id.button_large);

        // Set initial font size display text
        textFontSizeValue.setText(fontSizePreference.substring(0, 1).toUpperCase() + fontSizePreference.substring(1));

        // Set button click listeners
        buttonSmall.setOnClickListener(v -> setFontSize("small"));
        buttonMedium.setOnClickListener(v -> setFontSize("medium"));
        buttonLarge.setOnClickListener(v -> setFontSize("large"));
    }

    private void setThemeBasedOnPreference(String themePreference) {
        if (themePreference.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void setFontSize(String sizeKey) {
        // Update the display text
        textFontSizeValue.setText(sizeKey.substring(0, 1).toUpperCase() + sizeKey.substring(1));

        // Save the preference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FONT_SIZE_KEY, sizeKey);
        editor.apply();

        // Update current activity views
        updateFontSizeViews(sizeKey);
    }

    private void updateFontSizeViews(String sizeKey) {
        float size;
        switch (sizeKey) {
            case "small":
                size = getResources().getDimension(R.dimen.font_size_small);
                break;
            case "medium":
                size = getResources().getDimension(R.dimen.font_size_medium);
                break;
            case "large":
                size = getResources().getDimension(R.dimen.font_size_large);
                break;
            default:
                size = getResources().getDimension(R.dimen.font_size_medium);
        }

        textFontSizeValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textFontSizeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textThemeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
}