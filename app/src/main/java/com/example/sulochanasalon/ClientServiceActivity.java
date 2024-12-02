package com.example.sulochanasalon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ClientServiceActivity extends AppCompatActivity
{
    private ListView serviceListView;
    private DatabaseHelper dbHelper;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    private Button backToDashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_service);

        // Initialize Views
        TextView headingTextView = findViewById(R.id.headingTextView);
        headingTextView.setText("Available Services in Our Salon");

        dbHelper = DatabaseHelper.getInstance(this);
        serviceListView = findViewById(R.id.clientServiceListView);
        backToDashboardButton = findViewById(R.id.backToDashboardButton); // Ensure the button ID is correct

        loadServices();

        // Handle click on a service to navigate to booking
        serviceListView.setOnItemClickListener((parent, view, position, id) -> {
            Service service = serviceList.get(position);
            Intent intent = new Intent(ClientServiceActivity.this, ClientAppointmentBookingActivity.class);
            intent.putExtra("SERVICE_ID", service.getId());  // Pass service ID to the booking activity
            intent.putExtra("SERVICE_NAME", service.getName());
            intent.putExtra("SERVICE_PRICE", service.getPrice());
            startActivity(intent);
        });

        // Set click listener for "Back to Dashboard" button
        backToDashboardButton.setOnClickListener(v -> navigateToDashboard());
    }

    private void loadServices() {
        serviceList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllServices();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                int parentId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARENT_SERVICE_ID));

                serviceList.add(new Service(id, name, price, description, parentId));
            } while (cursor.moveToNext());
            cursor.close();
        }

        serviceAdapter = new ServiceAdapter(this, serviceList, false);
        serviceListView.setAdapter(serviceAdapter);
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(ClientServiceActivity.this, ClientDashboardActivity.class);
        intent.putExtra("USER_ID", getIntent().getIntExtra("USER_ID", -1)); // Ensure User ID is passed back
        startActivity(intent);
        finish();
    }
}
