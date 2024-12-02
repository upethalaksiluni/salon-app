package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class NotificationActivity extends AppCompatActivity
{
    private ListView notificationListView;
    private NotificationAdapter adapter;
    private List<UserNotification> notifications;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        notificationListView = findViewById(R.id.notificationListView);
        dbHelper = DatabaseHelper.getInstance(this);

        // Get the UserID passed via Intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        loadNotifications();
    }

    private void loadNotifications() {
        // Fetch notifications from the database
        notifications = dbHelper.fetchAllNotifications();

        if (notifications.isEmpty()) {
            Toast.makeText(this, "No notifications available.", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new NotificationAdapter(this, notifications);
        notificationListView.setAdapter(adapter);
    }

    private void navigateToBillActivity(int appointmentID) {
        Intent intent = new Intent(NotificationActivity.this, BillActivity.class);
        intent.putExtra("APPOINTMENT_ID", appointmentID);
        startActivity(intent);
    }
}