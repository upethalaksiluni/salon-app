package com.example.sulochanasalon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FeedbackActivity extends AppCompatActivity
{
    private EditText feedbackEditText;
    private Button submitFeedbackButton, skipFeedbackButton;
    private DatabaseHelper dbHelper;
    private int paymentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);

        // Bind UI elements
        feedbackEditText = findViewById(R.id.feedbackEditText);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);
        skipFeedbackButton = findViewById(R.id.skipFeedbackButton);

        // Initialize UI components
        TextView feedbackHeading = findViewById(R.id.feedbackHeading);
        ImageView feedbackImageView = findViewById(R.id.feedbackImageView);

        // Initialize dbHelper
        dbHelper = DatabaseHelper.getInstance(this);

        // Retrieve payment ID passed from BillActivity
        paymentId = getIntent().getIntExtra("PAYMENT_ID", -1);

        // Set listeners for buttons
        submitFeedbackButton.setOnClickListener(v -> submitFeedback());
        skipFeedbackButton.setOnClickListener(v -> skipFeedback());
    }

    private void submitFeedback() {
        // Get feedback text
        String feedback = feedbackEditText.getText().toString();

        if (!feedback.isEmpty()) {
            // Save feedback in the database
            boolean feedbackSaved = dbHelper.addFeedback(paymentId, feedback);

            if (feedbackSaved) {
                Toast.makeText(this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to submit feedback. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter some feedback.", Toast.LENGTH_SHORT).show();
        }
        navigateToThankYouPage();
    }

    private void skipFeedback() {
        // Skip feedback and navigate to ThankYouActivity
        navigateToThankYouPage();
    }

    private void navigateToThankYouPage() {
        // Navigate to ThankYouActivity
        Intent intent = new Intent(FeedbackActivity.this, ThankYouActivity.class);
        startActivity(intent);
        finish();
    }
}