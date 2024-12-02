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

public class AdminPaymentHistoryActivity extends AppCompatActivity
{
    private ListView paymentHistoryListView;
    private DatabaseHelper dbHelper;
    private Button goToDashboardButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_payment_history);

        paymentHistoryListView = findViewById(R.id.paymentHistoryListView);
        goToDashboardButton = findViewById(R.id.goToDashboardButton);
        dbHelper = DatabaseHelper.getInstance(this);

        loadPaymentHistory();

        // Set up button listener to navigate back to the dashboard
        goToDashboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPaymentHistoryActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }


    private void loadPaymentHistory() {
        ArrayList<String> paymentHistory = dbHelper.fetchAllPayments(); // New method for fetching all payments

        if (paymentHistory.isEmpty()) {
            Toast.makeText(this, "No payment history found.", Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paymentHistory);
            paymentHistoryListView.setAdapter(adapter);
        }
    }
}
