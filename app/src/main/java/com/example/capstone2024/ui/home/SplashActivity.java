package com.example.capstone2024.ui.home;

import com.example.capstone2024.R;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.models.UserSetup;
import com.example.capstone2024.ui.usersetup.UserSetupActivity;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(this);
        // Get the list of users synchronously
        List<UserSetup> users = helper.fetchAllUsersSync();

        // Get the views
        setContentView(R.layout.activity_splash);
        ImageView splashLogo = findViewById(R.id.splash_logo);
        TextView splashText = findViewById(R.id.splash_text);

        // Set initial properties
        splashLogo.setAlpha(0f);
        splashLogo.setTranslationY(500f);
        splashText.setAlpha(0f);
        splashText.setTranslationY(100f);

        splashLogo.post(() -> {
            // Fade-in logo
            ObjectAnimator fadeInLogo = ObjectAnimator.ofFloat(splashLogo, "alpha", 0f, 1f);
            fadeInLogo.setDuration(1500);

            // Move logo up
            ObjectAnimator liftLogo = ObjectAnimator.ofFloat(splashLogo, "translationY", 500f, 0f);
            liftLogo.setDuration(1000);

            // Scale-up logo
            ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(splashLogo, "scaleX", 0.5f, 1f); // Scale width
            ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(splashLogo, "scaleY", 0.5f, 1f); // Scale height
            scaleUpX.setDuration(1000);
            scaleUpY.setDuration(1000);

            // Fade-in text
            ObjectAnimator fadeInText = ObjectAnimator.ofFloat(splashText, "alpha", 0f, 1f);
            fadeInText.setDuration(1700);

            // Move text up
            ObjectAnimator liftText = ObjectAnimator.ofFloat(splashText, "translationY", 100f, 0f);
            liftText.setDuration(1000);

            // Combine all animations into an AnimatorSet
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(fadeInLogo, liftLogo, scaleUpX, scaleUpY, fadeInText, liftText); // Play all animations together
            animatorSet.start();
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Start HomeActivity after animation
            if (users.isEmpty()) {
                startActivity(new Intent(SplashActivity.this, UserSetupActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }
            finish();
        }, 2000);
    }
}
