package com.example.sulochanasalon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import android.os.Handler;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class AdminDashboardActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ViewPager imageSlider;
    private LinearLayout dotsLayout;
    private int currentPage = 0;
    private DatabaseHelper dbHelper;

    private int[] imageResources = {
            R.drawable.admindb1,
            R.drawable.admindb2,
            R.drawable.admindb3,
            R.drawable.admindb4,
            R.drawable.admindb5
    };

    private Handler handler = new Handler();
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentPage == imageResources.length) {
                currentPage = 0;
            }
            imageSlider.setCurrentItem(currentPage++, true);
            handler.postDelayed(this, 3000);  // Schedule next update in 3 seconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        dbHelper = DatabaseHelper.getInstance(this);

        // Initialize navigation view and drawer layout
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        SessionManager sessionManager = new SessionManager(this);
        String adminName = sessionManager.getAdminName();
        int adminId = sessionManager.getAdminId();

        if (adminId == -1) {
            Toast.makeText(this, "Admin ID not found. Redirecting to login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome to Admin Dashboard, " + adminName);

        // Access the header layout from the navigation view
        View headerView = navigationView.getHeaderView(0);
        TextView adminNameTextView = headerView.findViewById(R.id.adminName);

        // Set the admin name in the TextView
        if (adminNameTextView != null) {
            adminNameTextView.setText(adminName);
        }

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the toggle for the drawer
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up navigation item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            AdminMenuAction action = AdminMenuAction.fromId(item.getItemId());
            if (action != null) {
                switch (action) {
                    case MANAGE_SERVICES:
                        startActivity(new Intent(this, AdminServiceManagementActivity.class));
                        break;
                    case VIEW_USERS:
                        // Launch User Details Activity
                        viewUserDetails();
                        break;
                    case VIEW_PAYMENTS:
                        startActivity(new Intent(this, AdminPaymentHistoryActivity.class));
                        break;
                    case ADD_SERVICE:
                        startActivity(new Intent(this, AddEditServiceActivity.class));
                        break;
                    case NAV_APPOINTMENTS: // Handle the Manage Appointments case
                        startActivity(new Intent(this, AdminAppointmentManagementActivity.class));
                        break;
                    case NAV_NOTIFICATIONS:
                        startActivity(new Intent(this, AdminNotificationsActivity.class));
                        break;
                    case LOGOUT:
                        logout();
                        break;
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Set up image slider
        imageSlider = findViewById(R.id.imageSlider);
        dotsLayout = findViewById(R.id.dotsLayout);
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageResources);
        imageSlider.setAdapter(adapter);

        addDots(0);
        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                // Dynamically update content description
                String description = "Showing image " + (position + 1) + " of " + imageResources.length;
                imageSlider.setContentDescription(description);

                addDots(position);
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        startSliderTimer();
    }


    private void addDots(int position) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < imageResources.length; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageResource(i == position ? R.drawable.dot_active : R.drawable.dot_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotsLayout.addView(dot);
        }
    }

    private void viewUserDetails() {
        // Redirect to UserDetailsActivity
        Intent intent = new Intent(AdminDashboardActivity.this, UserDetailsActivity.class);
        startActivity(intent);
    }

    private void logout() {
        // Clear session or any saved data (if applicable)
        // Example: Clear SharedPreferences (if you are storing user data)
        getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply();

        // Navigate to LoginActivity
        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close AdminDashboardActivity
    }


    private void startSliderTimer() {
        handler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(sliderRunnable);
        super.onDestroy();
    }
}