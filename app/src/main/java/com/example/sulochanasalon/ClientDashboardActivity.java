package com.example.sulochanasalon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;


public class ClientDashboardActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView welcomeText;
    private ViewPager imageSlider;
    private LinearLayout dotsLayout;
    private int currentPage = 0;
    private int userId;

    private final int[] imageResources = {
            R.drawable.admindb1,
            R.drawable.admindb2,
            R.drawable.admindb3,
            R.drawable.admindb4,
            R.drawable.admindb5
    };

    private final Handler handler = new Handler();
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentPage == imageResources.length) {
                currentPage = 0;
            }
            imageSlider.setCurrentItem(currentPage++, true);
            handler.postDelayed(this, 3000); // Schedule next update in 3 seconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_dashboard);

        initializeUI();

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Redirecting to login.", Toast.LENGTH_SHORT).show();
            navigateToLogin();
            return;
        }

        displayWelcomeMessage();
        setupNavigationDrawer();
        setupImageSlider();
    }

    private void initializeUI() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        welcomeText = findViewById(R.id.welcomeText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

    private void displayWelcomeMessage() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        User client = dbHelper.getUserById(userId);

        if (client != null) {
            String clientName = client.getFullName();
            welcomeText.setText("Welcome to Client Dashboard, " + clientName);
        } else {
            welcomeText.setText("Welcome to Client Dashboard");
        }
    }

    private void setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(item -> {
            try {
                NavigationItem menuItem = NavigationItem.fromMenuId(item.getItemId());
                handleNavigation(menuItem);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Unknown menu item selected.", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupImageSlider() {
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

    private void handleNavigation(NavigationItem menuItem) {
        switch (menuItem) {
            case PROFILE:
                navigateToUserProfile();
                break;
            case APPOINTMENTS:
                navigateToAppointments();
                break;
            case SERVICES:
                navigateToServices();
                break;
            case PAYMENT_HISTORY:
                navigateToPaymentHistory();
                break;
            case NOTIFICATIONS:
                navigateToNotifications();
                break;
            case LOGOUT:
                logout();
                break;
        }
    }

    private void navigateToUserProfile() {
        Intent intent = new Intent(ClientDashboardActivity.this, UserProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void navigateToAppointments() {
        Intent intent = new Intent(ClientDashboardActivity.this, ClientAppointmentBookingActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void navigateToServices() {
        Intent intent = new Intent(ClientDashboardActivity.this, ClientServiceActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void navigateToPaymentHistory() {
        Intent intent = new Intent(ClientDashboardActivity.this, ClientPaymentHistoryActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void navigateToNotifications() {
        Intent intent = new Intent(ClientDashboardActivity.this, NotificationActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("UserSession", MODE_PRIVATE).edit();
        editor.clear().apply();
        navigateToLogin();
    }

    private void navigateToLogin() {
        startActivity(new Intent(ClientDashboardActivity.this, LoginActivity.class));
        finish();
    }

    private void startSliderTimer() {
        handler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Session expired. Redirecting to login.", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(sliderRunnable);
        super.onDestroy();
    }
}