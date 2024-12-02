package com.example.sulochanasalon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity
{
    private EditText nameField, emailField, passwordField, confirmPasswordField, phoneField, dobField, addressField;
    private RadioGroup genderGroup;
    private Button signUpButton;
    private TextView loginLink;
    private ImageView profileImageView;
    private Uri profileImageUri;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        nameField = findViewById(R.id.signUpName);
        emailField = findViewById(R.id.signUpEmail);
        passwordField = findViewById(R.id.signUpPassword);
        confirmPasswordField = findViewById(R.id.signUpConfirmPassword);
        phoneField = findViewById(R.id.signUpPhone);
        dobField = findViewById(R.id.signUpDob);
        addressField = findViewById(R.id.signUpAddress);
        genderGroup = findViewById(R.id.genderGroup);
        profileImageView = findViewById(R.id.profileImageView);
        signUpButton = findViewById(R.id.signUpButton);
        loginLink = findViewById(R.id.signUpLoginLink);

        dbHelper = DatabaseHelper.getInstance(this);

        // Set up DOB picker
        dobField.setOnClickListener(v -> openDatePicker());

        // Set up profile image selection
        profileImageView.setOnClickListener(v -> selectProfileImage());

        // Sign up button click listener
        signUpButton.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();
            String dob = dobField.getText().toString().trim();
            String address = addressField.getText().toString().trim();
            RadioButton selectedGenderButton = findViewById(genderGroup.getCheckedRadioButtonId());
            String gender = selectedGenderButton != null ? selectedGenderButton.getText().toString() : "";
            String profileImage = profileImageUri != null ? profileImageUri.toString() : "";

            if (validateInput(name, email, password, confirmPassword, dob, gender)) {
                User newUser = new User(0, name, email, password, "Client", phone, gender, dob, address, profileImage);

                if (dbHelper.registerUser(newUser)) {
                    Toast.makeText(SignUpActivity.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(SignUpActivity.this, "Error saving user. Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Login link click listener
        loginLink.setOnClickListener(v -> navigateToLogin());
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) ->
                dobField.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePicker.show();
    }

    private void selectProfileImage() {
        profileImageLauncher.launch("image/*");
    }

    private final ActivityResultLauncher<String> profileImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    profileImageUri = uri;
                    profileImageView.setImageURI(uri);
                }
            });

    private boolean validateInput(String name, String email, String password, String confirmPassword, String dob, String gender) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Invalid email format!");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordField.setError("Passwords do not match!");
            return false;
        }

        return true;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}