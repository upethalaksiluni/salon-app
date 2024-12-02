package com.example.sulochanasalon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BillActivity extends AppCompatActivity
{
    private TextView serviceTextView, priceTextView, stylistTextView, totalTextView;
    private TextView clientNameTextView, clientEmailTextView, clientPhoneTextView, todayBillTextView, thankYouTextView;
    private Button acceptButton, backToServicesButton, backToBookingButton;
    private int appointmentId;
    private double price;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill);

        // Initialize the views
        serviceTextView = findViewById(R.id.serviceTextView);
        priceTextView = findViewById(R.id.priceTextView);
        stylistTextView = findViewById(R.id.stylistTextView);
        totalTextView = findViewById(R.id.totalTextView);
        clientNameTextView = findViewById(R.id.clientNameTextView);
        clientEmailTextView = findViewById(R.id.clientEmailTextView);
        clientPhoneTextView = findViewById(R.id.clientPhoneTextView);
        todayBillTextView = findViewById(R.id.todayBillTextView);
        thankYouTextView = findViewById(R.id.thankYouTextView);
        acceptButton = findViewById(R.id.acceptButton);
        backToServicesButton = findViewById(R.id.backToServicesButton);
        backToBookingButton = findViewById(R.id.backToBookingButton);

        dbHelper = DatabaseHelper.getInstance(this);

        // Get userId and other details from the intent
        appointmentId = getIntent().getIntExtra("APPOINTMENT_ID", -1);
        String service = getIntent().getStringExtra("SERVICE");
        price = getIntent().getDoubleExtra("PRICE", 0.0);
        String stylist = getIntent().getStringExtra("STYLIST");

        // Get userId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = prefs.getInt("USER_ID", -1);  // Retrieve userId from SharedPreferences

        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID.", Toast.LENGTH_SHORT).show();
            navigateToLogin();
            return;
        }

        // Display the bill details
        serviceTextView.setText(service);
        priceTextView.setText("$" + price);
        stylistTextView.setText(stylist);
        totalTextView.setText("Total: $" + price);

        // Fetch user details based on userId and display it
        fetchAndDisplayUserDetails(userId);

        // Set up the "Accept" button
        acceptButton.setOnClickListener(v -> navigateToFeedback());

        // Set up "Back to Services" and "Back to Booking" buttons
        backToServicesButton.setOnClickListener(v -> navigateToClientServices());
        backToBookingButton.setOnClickListener(v -> navigateToClientBooking());
    }

    private void fetchAndDisplayUserDetails(int userId) {
        User user = dbHelper.getUserById(userId);
        if (user != null) {
            clientNameTextView.setText(user.getFullName());
            clientEmailTextView.setText(user.getEmail());
            clientPhoneTextView.setText(user.getPhone());
        } else {
            Toast.makeText(this, "User details not found for ID: " + userId, Toast.LENGTH_SHORT).show();
            navigateToLogin();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(BillActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Navigate to FeedbackActivity to collect feedback
    private void navigateToFeedback() {
        Intent intent = new Intent(BillActivity.this, FeedbackActivity.class);
        intent.putExtra("APPOINTMENT_ID", appointmentId);
        startActivity(intent);
        finish();
    }

    // Navigate to Client Services Activity
    private void navigateToClientServices() {
        Intent intent = new Intent(BillActivity.this, ClientServiceActivity.class);
        startActivity(intent);
        finish();
    }

    // Navigate to Client Appointment Booking Activity
    private void navigateToClientBooking() {
        Intent intent = new Intent(BillActivity.this, ClientAppointmentBookingActivity.class);
        startActivity(intent);
        finish();
    }
}