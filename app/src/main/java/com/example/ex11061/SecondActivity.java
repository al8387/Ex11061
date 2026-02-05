package com.example.ex11061;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This activity is responsible for displaying the results of the series calculation.
 * It receives series parameters from MainActivity, calculates the first 20 terms,
 * and displays them in a Spinner. When a term is selected, it shows detailed information
 * about that term in a TextView.
 */
public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner seriesSpinner;
    private TextView resultTextView;

    private String seriesType;
    private double firstTerm;
    private double difference;
    private final ArrayList<Double> seriesTerms = new ArrayList<>();

    /**
     * Called when the activity is first created. This method initializes the UI,
     * retrieves the series data from the intent, calculates the series terms,
     * and populates the spinner.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        seriesSpinner = findViewById(R.id.seriesSpinner);
        resultTextView = findViewById(R.id.resultTextView);

        Intent intent = getIntent();
        seriesType = intent.getStringExtra("seriesType");
        firstTerm = intent.getDoubleExtra("firstTerm", 0);
        difference = intent.getDoubleExtra("difference", 0);

        calculateSeries();

        ArrayList<String> formattedTerms = new ArrayList<>();
        for (Double term : seriesTerms) {
            formattedTerms.add(String.format(Locale.getDefault(), "%.2f", term));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formattedTerms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesSpinner.setAdapter(adapter);
        seriesSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Calculates the first 20 terms of the mathematical series based on its type
     * (Arithmetic or Geometric) and populates the seriesTerms list.
     */
    private void calculateSeries() {
        seriesTerms.clear();
        double currentTerm = firstTerm;
        for (int i = 0; i < 20; i++) {
            seriesTerms.add(currentTerm);
            if (seriesType.equals("סדרה חשבונית")) {
                currentTerm += difference;
            } else { // Geometric
                currentTerm *= difference;
            }
        }
    }

    /**
     * Callback method to be invoked when an item in the spinner has been selected.
     * It calculates and displays information for the selected term, including its position,
     * the next term in the series, and the sum up to the selected term.
     * @param parent The AdapterView where the selection happened.
     * @param view The view within the AdapterView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that is selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        double selectedTerm = seriesTerms.get(position);
        double nextTerm;
        double sum = 0;

        if (seriesType.equals("סדרה חשבונית")) {
            nextTerm = selectedTerm + difference;
            sum = (position + 1) / 2.0 * (2 * firstTerm + position * difference);
        } else { // Geometric
            if (difference == 1) {
                sum = (position + 1) * firstTerm;
            } else {
                sum = firstTerm * (Math.pow(difference, position + 1) - 1) / (difference - 1);
            }
            nextTerm = selectedTerm * difference;
        }

        String result = String.format(Locale.getDefault(),
                "מקומו של האיבר בסדרה: %d\n\n" +
                "האיבר הבא אחריו: %.2f\n\n" +
                "סכום הסדרה עד לאיבר הנבחר: %.2f",
                position + 1,
                nextTerm,
                sum);

        resultTextView.setText(result);
    }

    /**
     * Callback method to be invoked when the selection disappears from this view.
     * This method is intentionally left empty as no action is required.
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        resultTextView.setText("");
    }
}