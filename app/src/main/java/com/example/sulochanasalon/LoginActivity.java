package com.example.sulochanasalon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView signUpLink, forgotPasswordLink;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize views
        ImageView loginImage = findViewById(R.id.loginImage);
        emailField = findViewById(R.id.loginEmail);
        passwordField = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpLink = findViewById(R.id.signUpLink);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

        dbHelper = DatabaseHelper.getInstance(this);

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (validateInput(email, password)) {
                User user = dbHelper.loginUser(email, password);

                if (user != null) {
                    navigateToDashboard(user);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sign-up link listener
        signUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Forgot password link listener
        forgotPasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void navigateToDashboard(User user) {
        SessionManager sessionManager = new SessionManager(this);

        if (user.getRole().equalsIgnoreCase("Admin")) {
            sessionManager.saveAdminSession(user.getUserId(), user.getFullName());
            startActivity(new Intent(this, AdminDashboardActivity.class));
        } else {
            sessionManager.saveUserSession(user.getUserId());
            Intent intent = new Intent(this, ClientDashboardActivity.class);
            intent.putExtra("USER_ID", user.getUserId());
            startActivity(intent);
        }

        finish();
    }
}