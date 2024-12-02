package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ClientAppointmentBookingActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private Spinner timeSlotSpinner;
    private DatabaseHelper dbHelper;
    private Button bookButton, backToDashboardButton;
    private String selectedDate;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_appointment_booking);

        // Optional: Set heading text dynamically
        TextView headingTextView = findViewById(R.id.headingTextView); // Ensure ID is matched
        headingTextView.setText("Book an Appointment");

        // Bind UI elements
        calendarView = findViewById(R.id.calendarView);
        timeSlotSpinner = findViewById(R.id.timeSlotSpinner);
        bookButton = findViewById(R.id.bookButton); // Bind bookButton
        backToDashboardButton = findViewById(R.id.backToDashboardButton); // Bind backToDashboardButton
        dbHelper = DatabaseHelper.getInstance(this);

        // Get UserID from intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Set listeners
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            loadAvailableSlots();
        });

        bookButton.setOnClickListener(v -> bookAppointment()); // Book appointment logic

        // Navigate back to dashboard
        backToDashboardButton.setOnClickListener(v -> navigateToDashboard());
    }

    private void loadAvailableSlots() {
        ArrayList<String> unavailableSlots = dbHelper.fetchUnavailableSlots();
        ArrayList<String> allSlots = new ArrayList<>();
        allSlots.add("9:00 AM");
        allSlots.add("10:00 AM");
        allSlots.add("11:00 AM");
        allSlots.add("2:00 PM");
        allSlots.add("3:00 PM");

        ArrayList<String> availableSlots = new ArrayList<>();
        for (String slot : allSlots) {
            if (!unavailableSlots.contains(selectedDate + " " + slot)) {
                availableSlots.add(slot);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(adapter);
    }

    private void bookAppointment() {
        if (selectedDate == null || timeSlotSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a date and time slot.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedTime = timeSlotSpinner.getSelectedItem().toString();
        String service = "Haircut";
        double price = 25.0;
        String stylist = "Stylist A";

        boolean appointmentSuccess = dbHelper.addAppointment(selectedDate, selectedTime, service, stylist, "Pending", userId);

        if (appointmentSuccess) {
            int appointmentId = dbHelper.getLastInsertedAppointmentId();
            dbHelper.addPayment(appointmentId, price);

            // Pass userId along with other details
            Intent intent = new Intent(ClientAppointmentBookingActivity.this, BillActivity.class);
            intent.putExtra("APPOINTMENT_ID", appointmentId);
            intent.putExtra("SERVICE", service);
            intent.putExtra("PRICE", price);
            intent.putExtra("STYLIST", stylist);
            intent.putExtra("USER_ID", userId); // Include userId here
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to book appointment. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(ClientAppointmentBookingActivity.this, ClientDashboardActivity.class);
        intent.putExtra("USER_ID", userId); // Pass the user ID to the dashboard
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ClientAppointmentBookingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}