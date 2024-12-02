package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AdminAppointmentManagementActivity extends AppCompatActivity {
    private ListView appointmentsListView;
    private Button backToDashboardButton;
    private RadioButton pastAppointmentsButton, futureAppointmentsButton;
    private DatabaseHelper dbHelper;
    private AppointmentAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_appointment_management);

        // Initialize UI components
        appointmentsListView = findViewById(R.id.appointmentsListView);
        backToDashboardButton = findViewById(R.id.backToDashboardButton);
        pastAppointmentsButton = findViewById(R.id.pastAppointmentsButton);
        futureAppointmentsButton = findViewById(R.id.futureAppointmentsButton);

        dbHelper = DatabaseHelper.getInstance(this);

        // Load future appointments by default
        loadAppointments("future");

        // Button listeners
        backToDashboardButton.setOnClickListener(v -> navigateToDashboard());

        pastAppointmentsButton.setOnClickListener(v -> loadAppointments("past"));
        futureAppointmentsButton.setOnClickListener(v -> loadAppointments("future"));

        // Handle item clicks for managing appointments
        appointmentsListView.setOnItemClickListener((parent, view, position, id) -> {
            String appointment = adapter.getItem(position);
            showActionDialog(appointment);
        });
    }

    private void loadAppointments(String type) {
        // Use userId and type ("past" or "future")
        ArrayList<String> appointments = dbHelper.fetchAppointmentsByDate(userId, type);
        adapter = new AppointmentAdapter(this, appointments);
        appointmentsListView.setAdapter(adapter);
    }

    private void showActionDialog(String appointment) {
        // Extract AppointmentID
        String[] parts = appointment.split(" \\| "); // Split by the delimiter used in fetchAppointments
        int appointmentID = Integer.parseInt(parts[0]); // The first part is the AppointmentID

        new AlertDialog.Builder(this)
                .setTitle("Manage Appointment")
                .setMessage("Would you like to accept or reject this appointment?")
                .setPositiveButton("Accept", (dialog, which) -> updateAppointmentStatus(appointmentID, "Confirmed"))
                .setNegativeButton("Reject", (dialog, which) -> updateAppointmentStatus(appointmentID, "Rejected"))
                .show();
    }

    private void updateAppointmentStatus(int appointmentID, String status) {
        dbHelper.updateAppointmentStatus(appointmentID, status);
        // Reload appointments based on selected RadioButton (Past or Future)
        loadAppointments(futureAppointmentsButton.isChecked() ? "future" : "past");
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(AdminAppointmentManagementActivity.this, AdminDashboardActivity.class);
        startActivity(intent);
        finish();
    }
}