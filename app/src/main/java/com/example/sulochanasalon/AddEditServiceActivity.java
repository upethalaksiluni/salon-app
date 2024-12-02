package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEditServiceActivity extends AppCompatActivity {
    private EditText serviceNameInput, serviceDescriptionInput, servicePriceInput;
    private Button saveServiceButton, backToDashboardButton;
    private ImageView serviceImageView;
    private DatabaseHelper dbHelper;
    private int serviceId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_service);

        dbHelper = DatabaseHelper.getInstance(this);
        serviceNameInput = findViewById(R.id.serviceNameInput);
        backToDashboardButton = findViewById(R.id.backToDashboardButton);
        serviceDescriptionInput = findViewById(R.id.serviceDescriptionInput);
        servicePriceInput = findViewById(R.id.servicePriceInput);
        serviceImageView = findViewById(R.id.serviceImageView);
        saveServiceButton = findViewById(R.id.saveServiceButton);

        // Set a default image for the service
        serviceImageView.setImageResource(R.drawable.service);

        // Check if this is for editing an existing service
        serviceId = getIntent().getIntExtra("SERVICE_ID", -1);
        if (serviceId != -1) {
            // Populate fields with existing data
            serviceNameInput.setText(getIntent().getStringExtra("SERVICE_NAME"));
            serviceDescriptionInput.setText(getIntent().getStringExtra("SERVICE_DESCRIPTION"));
            servicePriceInput.setText(String.valueOf(getIntent().getDoubleExtra("SERVICE_PRICE", 0.0)));
        }

        saveServiceButton.setOnClickListener(v -> saveService());
        backToDashboardButton.setOnClickListener(v -> navigateToDashboard());
    }


    private void saveService() {
        String name = serviceNameInput.getText().toString().trim();
        String description = serviceDescriptionInput.getText().toString().trim();
        String priceText = servicePriceInput.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceText);

        if (serviceId == -1) {
            // Add new service
            boolean isAdded = dbHelper.addService(name, description, price, null, null);
            if (isAdded) {
                Toast.makeText(this, "Service added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add service", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing service
            boolean isUpdated = dbHelper.updateService(serviceId, name, description, price);
            if (isUpdated) {
                Toast.makeText(this, "Service updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update service", Toast.LENGTH_SHORT).show();
            }
        }

        finish(); // Return to AdminServiceManagementActivity
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(AddEditServiceActivity.this, AdminDashboardActivity.class);
        intent.putExtra("USER_ID", getIntent().getIntExtra("USER_ID", -1));
        startActivity(intent);
        finish();
    }
}

