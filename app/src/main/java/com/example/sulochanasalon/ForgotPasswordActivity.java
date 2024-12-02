package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordActivity extends AppCompatActivity
{
    private EditText emailField, phoneField;
    private Button verifyButton, backToLoginButton;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI components
        TextView heading = findViewById(R.id.forgotPasswordHeading);
        ImageView forgotPasswordImage = findViewById(R.id.forgotPasswordImage);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        verifyButton = findViewById(R.id.verifyButton);

        // Initialize Database Helper
        db = DatabaseHelper.getInstance(this);

        // Get userId passed from LoginActivity
        userId = getIntent().getIntExtra("USER_ID", -1); // Get the userId

        // Set up the verify button listener
        verifyButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();

            // Validate email and phone input
            if (TextUtils.isEmpty(email)) {
                emailField.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                phoneField.setError("Phone number is required");
                return;
            }

            // Verify if email exists in the database
            if (db.verifyEmailAndPhone(email, phone)) {
                // Check if the phone number matches the email
                User user = db.getUserById(userId);  // Get user by userId
                if (user != null && user.getPhone().equals(phone)) {
                    // Proceed to reset password if phone matches
                    Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("USER_ID", userId);  // Pass userId to reset password activity
                    startActivity(intent);
                    finish();  // Close the current activity
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Phone number doesn't match our records.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Email not found.", Toast.LENGTH_LONG).show();
            }
        });

        // Set up the "Back to Login" button listener
        backToLoginButton.setOnClickListener(v -> {
            // Navigate back to the LoginActivity
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Close ForgotPasswordActivity
        });
    }
}