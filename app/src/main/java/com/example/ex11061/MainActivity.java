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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner sP;
    private String[] seriesTypes = {"בחר סוג סדרה", "סדרה חשבונית", "סדרה הנדסית"};
    private String seriesType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sP = findViewById(R.id.sP);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, seriesTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sP.setAdapter(adapter);
        sP.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        seriesType = parent.getItemAtPosition(position).toString();
        showInputDialog();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

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
                // Reset spinner for next use
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