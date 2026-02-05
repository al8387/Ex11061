package com.example.ex11061;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The main entry point of the application. This activity serves as the main screen for user interaction.
 * It presents a spinner for the user to select a type of mathematical series (Arithmetic, Geometric)
 * or to navigate to a credits screen. Upon selecting a series type, it prompts the user for
 * necessary parameters and passes them to the SecondActivity for calculation and display.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner sP;
    private String[] Types = {"בחר סוג סדרה", "סדרה חשבונית", "סדרה הנדסית", "קרדיטים"};
    private String seriesType;

    /**
     * Called when the activity is first created. This method initializes the user interface,
     * sets the content view, and configures the main spinner with its options and listener.
     * @param savedInstanceState If the activity is being re-initialized after
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sP = findViewById(R.id.sP);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sP.setAdapter(adapter);
        sP.setOnItemSelectedListener(this);
    }

    /**
     * Callback method to be invoked when an item in this view has been selected. If the user
     * selects a series type, it triggers a dialog for further input. If 'Credits' is selected,
     * it navigates to the CreditsActivity.
     * @param parent The AdapterView where the selection happened.
     * @param view The view within the AdapterView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that is selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }

        String selection = parent.getItemAtPosition(position).toString();
        if (selection.equals("קרדיטים")) {
            startActivity(new Intent(this, CreditsActivity.class));
            sP.setSelection(0);
        } else {
            seriesType = selection;
            showInputDialog();
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this view.
     * This method is intentionally left empty as no action is required.
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Displays a dialog to prompt the user for series parameters. It dynamically creates input
     * fields for the first term and the series difference/ratio. After input validation,
     * it packages the data into an Intent and starts the SecondActivity.
     */
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("הכנס נתונים");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText firstTermInput = new EditText(this);
        firstTermInput.setHint("האיבר הראשון");
        firstTermInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(firstTermInput);

        final EditText differenceInput = new EditText(this);
        differenceInput.setHint(seriesType.equals("סדרה חשבונית") ? "הפרש הסדרה" : "מנת הסדרה");
        differenceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(differenceInput);

        builder.setView(layout);

        builder.setPositiveButton("המשך", (dialog, which) -> {
            String firstTermStr = firstTermInput.getText().toString();
            String differenceStr = differenceInput.getText().toString();

            if (TextUtils.isEmpty(firstTermStr) || TextUtils.isEmpty(differenceStr)) {
                Toast.makeText(MainActivity.this, "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
                sP.setSelection(0);
                return;
            }

            try {
                double firstTerm = Double.parseDouble(firstTermStr);
                double difference = Double.parseDouble(differenceStr);

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("seriesType", seriesType);
                intent.putExtra("firstTerm", firstTerm);
                intent.putExtra("difference", difference);
                startActivity(intent);
                sP.setSelection(0);

            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "קלט לא תקין, נא להזין מספרים בלבד", Toast.LENGTH_SHORT).show();
                sP.setSelection(0);
            }
        });
        builder.setNegativeButton("ביטול", (dialog, which) -> dialog.cancel());
        builder.setOnCancelListener(dialog -> sP.setSelection(0));

        builder.show();
    }
}