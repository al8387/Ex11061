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

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner seriesSpinner;
    private TextView resultTextView;

    private String seriesType;
    private double firstTerm;
    private double difference;
    private ArrayList<Double> seriesTerms = new ArrayList<>();

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

    private void calculateSeries() {
        seriesTerms.clear();
        double currentTerm = firstTerm;
        for (int i = 0; i < 20; i++) {
            seriesTerms.add(currentTerm);
            if (seriesType.equals("סדרה חשבונית")) {
                currentTerm += difference;
            } else { // Geometric series
                currentTerm *= difference;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        double selectedTerm = seriesTerms.get(position);
        double nextTerm;
        double sum = 0;

        if (seriesType.equals("סדרה חשבונית")) {
            nextTerm = selectedTerm + difference;
            sum = (position + 1) / 2.0 * (2 * firstTerm + position * difference);
        } else { // Geometric series
            nextTerm = selectedTerm * difference;
            if (difference == 1) {
                sum = (position + 1) * firstTerm;
            } else {
                sum = firstTerm * (Math.pow(difference, position + 1) - 1) / (difference - 1);
            }
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        resultTextView.setText("");
    }
}