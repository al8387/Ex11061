package com.example.ex11061;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An activity that displays credits information. It contains a simple text view
 * and a button to navigate back to the main screen of the application.
 */
public class CreditsActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. This method initializes the UI,
     * sets the content view, and configures the listener for the back button.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}