package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class AdminNotificationsActivity extends AppCompatActivity
{
    private ListView notificationListView;
    private NotificationAdapter adapter;
    private List<UserNotification> notifications;
    private Button backToDashboardButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_notifications);

        notificationListView = findViewById(R.id.notificationListView);
        backToDashboardButton = findViewById(R.id.backToDashboardButton);
        dbHelper = DatabaseHelper.getInstance(this);

        loadNotifications();

        // Set the "Back to Admin Dashboard" button functionality
        backToDashboardButton.setOnClickListener(v -> navigateToAdminDashboard());
    }

    private void loadNotifications() {
        // Fetch all notifications from the database for the admin
        notifications = dbHelper.fetchAllNotifications();

        if (notifications.isEmpty()) {
            Toast.makeText(this, "No notifications available.", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new NotificationAdapter(this, notifications);
        notificationListView.setAdapter(adapter);
    }

    private void navigateToAdminDashboard() {
        // Navigate back to Admin Dashboard Activity
        Intent intent = new Intent(AdminNotificationsActivity.this, AdminDashboardActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}