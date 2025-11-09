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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    Context c = this;
    EditText fNameET, lNameET, genderET, bioET;
    TextView usernameTV, pickDateButton;
    Spinner genderSpinner;
    int year, month, day;
    String selectedGender = "", selectedDOB = "";
    String username = "";
    Button saveBtn;
    Button registerBtn, redirectBtn;

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
        usernameTV = findViewById(R.id.username);
        fNameET = findViewById(R.id.fName);
        lNameET = findViewById(R.id.lName);
        pickDateButton = findViewById(R.id.pickDateButton);
        genderSpinner = findViewById(R.id.gender_spinner);
        bioET = findViewById(R.id.bio);
        registerBtn = findViewById(R.id.registerBtn);
        saveBtn = findViewById(R.id.saveBtn);
        redirectBtn = findViewById(R.id.redirectBtn);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        String fName = intent.getStringExtra("fName");
        String lName = intent.getStringExtra("lName");
        String dob = intent.getStringExtra("dob");
        String gender = intent.getStringExtra("gender");
        String bio = intent.getStringExtra("bio");

        if (username != null) usernameTV.setText(username);
        if (fName != null) fNameET.setText(fName);
        if (lName != null) lNameET.setText(lName);
        if (dob != null) pickDateButton.setText(dob);
        if (bio != null) bioET.setText(bio);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_list,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        if (gender != null) {
            int spinnerPosition = adapter.getPosition(gender);
            if (spinnerPosition >= 0) {
                genderSpinner.setSelection(spinnerPosition);
            }
        }

        pickDateButton.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        saveBtn.setOnClickListener(v -> updateProfile());

        redirectBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(ProfileActivity.this, ProfileTester.class);
            intent1.putExtra("username", username);
            startActivity(intent1);
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

    private void updateProfile(){
        HashMap<String, String> userData = UserDatabase.getUser(username);

        String fName = fNameET.getText().toString().trim();
        String lName = lNameET.getText().toString().trim();
        String dob = pickDateButton.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String bio = bioET.getText().toString().trim();

        userData.put("firstName", fName);
        userData.put("lastName", lName);
        userData.put("dob", dob);
        userData.put("gender", gender);
        userData.put("bio", bio);

        //getIntent().putExtra("firstName", fName);

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "New Full Name: " + fNameET.getText().toString() + " " +  lNameET.getText().toString(),  Toast.LENGTH_SHORT).show();
    }

}
