package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class PaymentHistoryActivity extends AppCompatActivity
{
    private ListView paymentHistoryListView;
    private Button backToDashboardButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_history);

        // Bind UI elements
        paymentHistoryListView = findViewById(R.id.paymentHistoryListView);
        backToDashboardButton = findViewById(R.id.backToDashboardButton);

        // Retrieve payment history from the Intent
        ArrayList<String> paymentHistory = getIntent().getStringArrayListExtra("PAYMENT_HISTORY");

        if (paymentHistory != null && !paymentHistory.isEmpty()) {
            // Set up the list adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paymentHistory);
            paymentHistoryListView.setAdapter(adapter);
        } else {
            // If no payment history, show a message
            Toast.makeText(this, "No payment history available", Toast.LENGTH_SHORT).show();
        }

        // Set up the "Back to Dashboard" button click listener
        backToDashboardButton.setOnClickListener(v -> navigateToAdminDashboard());
    }

        private void navigateToAdminDashboard() {
            // Navigate to Admin Dashboard
            Intent intent = new Intent(PaymentHistoryActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();  // Close the current activity
        }
    }
