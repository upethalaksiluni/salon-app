package com.example.sulochanasalon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class AdminServiceManagementActivity extends AppCompatActivity
{
    private ListView serviceListView;
    private Button addServiceButton, backToDashboardButton;
    private DatabaseHelper dbHelper;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_service_management);

        // Initialize views
        serviceListView = findViewById(R.id.serviceListView);
        addServiceButton = findViewById(R.id.addServiceButton);
        backToDashboardButton = findViewById(R.id.backToDashboardButton);
        dbHelper = DatabaseHelper.getInstance(this);

        // Load services from the database
        loadServices();

        // Add service button click listener
        addServiceButton.setOnClickListener(v -> {
            Toast.makeText(this, "Navigating to Add Service Page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminServiceManagementActivity.this, AddEditServiceActivity.class);
            startActivity(intent);
        });

        // Back to Dashboard button click listener
        backToDashboardButton.setOnClickListener(v -> navigateToDashboard());

        // Set up list view click listener for editing services
        serviceListView.setOnItemClickListener((parent, view, position, id) -> {
            Service service = serviceList.get(position);
            Intent intent = new Intent(AdminServiceManagementActivity.this, AddEditServiceActivity.class);
            intent.putExtra("SERVICE_ID", service.getId());
            intent.putExtra("SERVICE_NAME", service.getName());
            intent.putExtra("SERVICE_DESCRIPTION", service.getDescription());
            intent.putExtra("SERVICE_PRICE", service.getPrice());
            startActivity(intent);
        });
    }

    private void loadServices() {
        // Fetch services from the database
        serviceList = new ArrayList<>(dbHelper.getAllServicesAsList());

        // Set up the adapter and attach it to the ListView
        serviceAdapter = new ServiceAdapter(this, serviceList, true);
        serviceListView.setAdapter(serviceAdapter);
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(AdminServiceManagementActivity.this, AdminDashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload services when returning to the activity
        loadServices();
    }
}