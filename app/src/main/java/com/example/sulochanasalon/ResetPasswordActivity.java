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

public class ResetPasswordActivity extends AppCompatActivity
{
    private EditText emailField, phoneField, newPasswordField, confirmPasswordField;
    private Button resetPasswordButton, backToLoginButton;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        // Initialize UI components
        TextView heading = findViewById(R.id.resetPasswordHeading);
        ImageView resetPasswordImage = findViewById(R.id.resetPasswordImage);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        db = DatabaseHelper.getInstance(this);

        // Get userId passed from ForgotPasswordActivity
        userId = getIntent().getIntExtra("USER_ID", -1); // Get the userId

        // Fetch user details from the database
        User user = db.getUserById(userId);
        if (user != null) {
            // Optionally pre-fill email and phone from the database for convenience
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
        }

        resetPasswordButton.setOnClickListener(v -> {
            String emailInput = emailField.getText().toString().trim();
            String phoneInput = phoneField.getText().toString().trim();
            String newPassword = newPasswordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            // Validate input fields
            if (TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(phoneInput) ||
                    TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(ResetPasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ResetPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify email and phone number
            if (db.verifyEmailAndPhone(emailInput, phoneInput)) {
                // Proceed with password reset
                if (db.resetPassword(emailInput, newPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                    finish(); // Close the reset password activity
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Password reset failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Email and phone number do not match our records", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the "Back to Login" button listener
        backToLoginButton.setOnClickListener(v -> {
            // Navigate back to the LoginActivity
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Close ResetPasswordActivity
        });

    }
}