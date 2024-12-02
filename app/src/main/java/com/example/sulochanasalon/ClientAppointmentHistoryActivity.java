package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ClientAppointmentHistoryActivity extends AppCompatActivity 
{
    private ListView appointmentsListView;
    private RadioGroup radioGroup;
    private RadioButton pastAppointmentsRadioButton, futureAppointmentsRadioButton;
    private Button backToDashboardButton;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_appointment_history);



        // Initialize UI components
        appointmentsListView = findViewById(R.id.appointmentsListView);
        radioGroup = findViewById(R.id.radioGroup);
        pastAppointmentsRadioButton = findViewById(R.id.pastAppointmentsRadioButton);
        futureAppointmentsRadioButton = findViewById(R.id.futureAppointmentsRadioButton);
        backToDashboardButton = findViewById(R.id.backToDashboardButton); // Bind the button
        dbHelper = DatabaseHelper.getInstance(this);

        // Get UserID from intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Set default view to future appointments
        loadAppointments("future");

        // Handle radio button changes
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.pastAppointmentsRadioButton) {
                loadAppointments("past");
            } else if (checkedId == R.id.futureAppointmentsRadioButton) {
                loadAppointments("future");
            }
        });

        // Handle Back to Dashboard button
        backToDashboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(ClientAppointmentHistoryActivity.this, ClientDashboardActivity.class);
            intent.putExtra("USER_ID", userId); // Pass the user ID back to the dashboard
            startActivity(intent);
            finish();
        });
    }

    private void loadAppointments(String type) {
        // Pass both userId and type ("past" or "future")
        ArrayList<String> appointments = dbHelper.fetchAppointmentsByDate(userId, type);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointments);
        appointmentsListView.setAdapter(adapter);
    }
}