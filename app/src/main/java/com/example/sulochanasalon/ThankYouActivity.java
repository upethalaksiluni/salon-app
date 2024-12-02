package com.example.sulochanasalon;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ThankYouActivity extends AppCompatActivity
{

    private TextView thankYouMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thank_you);

        // Display thank you message
        thankYouMessage = findViewById(R.id.thankYouMessage);
        thankYouMessage.setText("Thank you for your visit!\nWe hope you had a great experience.");
    }
}