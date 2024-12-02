package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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

public class UserDetailsActivity extends AppCompatActivity
{
    private ListView userListView;
    private DatabaseHelper dbHelper;
    private Button backToDashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);

        userListView = findViewById(R.id.userListView);
        dbHelper = DatabaseHelper.getInstance(this);

        // Set the message text
        TextView usersMessage = findViewById(R.id.usersMessage);
        usersMessage.setText("All of the users logged into the system");

        // Initialize the Back to Dashboard Button
        backToDashboardButton = findViewById(R.id.backToDashboardButton);

        // Set up the button click listener to navigate back to the Admin Dashboard
        backToDashboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDetailsActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish(); // Close UserDetailsActivity
        });

        // Load the user list
        loadUserList();
    }

    private void loadUserList() {
        ArrayList<User> users = dbHelper.fetchUsers();
        if (users.isEmpty()) {
            Toast.makeText(this, "No users found.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> userDetailsList = new ArrayList<>();
        for (User user : users) {
            String userDetails = "Name: " + user.getFullName() +
                    "\nEmail: " + user.getEmail() +
                    "\nPhone: " + user.getPhone() +
                    "\nRole: " + user.getRole();
            userDetailsList.add(userDetails);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDetailsList);
        userListView.setAdapter(adapter);
    }
}