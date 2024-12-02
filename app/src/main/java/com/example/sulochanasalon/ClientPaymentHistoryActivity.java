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

public class ClientPaymentHistoryActivity extends AppCompatActivity
{
    private ListView paymentHistoryListView;
    private DatabaseHelper dbHelper;
    private int userId;
    private Button goToDashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_payment_history);

        paymentHistoryListView = findViewById(R.id.paymentHistoryListView);
        dbHelper = DatabaseHelper.getInstance(this);

        // Get the user ID from the intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Initialize the "Go to Dashboard" button
        goToDashboardButton = findViewById(R.id.goToDashboardButton);

        // Set click listener for the button
        goToDashboardButton.setOnClickListener(v -> navigateToDashboard());

        loadPaymentHistory();
    }

    private void loadPaymentHistory() {
        ArrayList<String> paymentHistory = dbHelper.fetchPaymentsByUser(userId);

        if (paymentHistory.isEmpty()) {
            Toast.makeText(this, "No payment history found.", Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paymentHistory);
            paymentHistoryListView.setAdapter(adapter);
        }
    }
    private void navigateToDashboard() {
        Intent intent = new Intent(ClientPaymentHistoryActivity.this, ClientDashboardActivity.class);
        intent.putExtra("USER_ID", userId); // Pass user ID to the dashboard
        startActivity(intent);
        finish();
    }
}