package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Load the fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Apply fade-in animation to the logo and texts
        ImageView logo = findViewById(R.id.logo);
        TextView splashTitle = findViewById(R.id.splashTitle);
        TextView splashQuote = findViewById(R.id.splashQuote);

        logo.startAnimation(fadeIn);
        splashTitle.startAnimation(fadeIn);
        splashQuote.startAnimation(fadeIn);

        // Transition to the LoginActivity after 3 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close SplashActivity
        }, 4000); // 4 seconds delay
    }
}