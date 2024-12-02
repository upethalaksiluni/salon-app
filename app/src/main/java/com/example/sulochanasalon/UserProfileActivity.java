package com.example.sulochanasalon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserProfileActivity extends AppCompatActivity
{
    private TextView userName, userEmail, userPhone, userAddress, userDOB;
    private Button closeProfileButton;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        // Initialize UI components
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.userPhone);
        userAddress = findViewById(R.id.userAddress);
        userDOB = findViewById(R.id.userDOB);
        closeProfileButton = findViewById(R.id.closeProfileButton);

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper.getInstance(this);

        // Get UserID passed from previous activity
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Fetch user details from the database
        User user = dbHelper.getUserById(userId);

        // If user exists, display the user details
        if (user != null) {
            userName.setText("Full Name: " + user.getFullName());
            userEmail.setText("Email: " + user.getEmail());
            userPhone.setText("Phone: " + user.getPhone());
            userAddress.setText("Address: " + user.getAddress());
            userDOB.setText("Date of Birth: " + user.getDateOfBirth());
        } else {
            // Handle case where user details are not found
            userName.setText("User not found");
        }

        // Set up Close Profile button
        closeProfileButton.setOnClickListener(v -> finish());  // Close the profile activity and return to the previous screen
    }
}