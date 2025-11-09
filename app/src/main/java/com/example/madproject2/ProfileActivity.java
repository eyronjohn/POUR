package com.example.madproject2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    Context c = this;
    EditText usernameET, fNameET, lNameET, genderET, bioET;
    TextView pickDateButton;
    Spinner genderSpinner;
    int year, month, day;
    String selectedGender = "", selectedDOB = "";
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        initialize();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void initialize() {
        usernameET = findViewById(R.id.username);
        fNameET = findViewById(R.id.fName);
        lNameET = findViewById(R.id.lName);
        pickDateButton = findViewById(R.id.pickDateButton);
        genderSpinner = findViewById(R.id.gender_spinner);
        bioET = findViewById(R.id.bio);
        registerBtn = findViewById(R.id.registerBtn);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Get data from Intent
        Intent intent = getIntent();
        String uname = intent.getStringExtra("username");
        String fName = intent.getStringExtra("fName");
        String lName = intent.getStringExtra("lName");
        String dob = intent.getStringExtra("dob");
        String gender = intent.getStringExtra("gender");
        String bio = intent.getStringExtra("bio");

        // Set data to fields
        if (uname != null) usernameET.setText(uname);
        if (fName != null) fNameET.setText(fName);
        if (lName != null) lNameET.setText(lName);
        if (dob != null) pickDateButton.setText(dob);
        if (bio != null) bioET.setText(bio);

        // Setup gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_list, // define this in res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Set spinner selection to user's gender
        if (gender != null) {
            int spinnerPosition = adapter.getPosition(gender);
            if (spinnerPosition >= 0) {
                genderSpinner.setSelection(spinnerPosition);
            }
        }

        // Handle date selection
        pickDateButton.setOnClickListener(v -> {
            showDatePickerDialog();
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    selectedDOB = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    pickDateButton.setText(selectedDOB);
                    year = selectedYear;
                    month = selectedMonth;
                    day = selectedDayOfMonth;
                },
                year, month, day
        );
        datePickerDialog.show();
    }

}
