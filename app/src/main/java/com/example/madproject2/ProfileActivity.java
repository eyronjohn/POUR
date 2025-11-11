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

    private Context c;
    private EditText fNameET, lNameET, bioET;
    private TextView usernameTV, pickDateButton;
    private Spinner genderSpinner;
    private int year, month, day;
    private String username = "", selectedDOB = "";
    private Button saveBtn, registerBtn, redirectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        c = this;
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

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
            Intent intent1 = new Intent(c, ProfileTester.class);
            intent1.putExtra("username", username);
            startActivity(intent1);
        });

    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth, 0, 0, 0);

                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);

                    if (selectedDate.after(today)) {
                        Toast.makeText(this, "Future dates are not allowed.", Toast.LENGTH_SHORT).show();
                        selectedDOB = "";
                        pickDateButton.setText("Select Date");
                    } else {
                        selectedDOB = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        pickDateButton.setText(selectedDOB);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void updateProfile() {
        String fName = fNameET.getText().toString().trim();
        String lName = lNameET.getText().toString().trim();
        String dob = pickDateButton.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String bio = bioET.getText().toString().trim();

        // Empty Fields Checker
        if (fName.isEmpty()) {
            Toast.makeText(c, "First name is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lName.isEmpty()) {
            Toast.makeText(c, "Last name is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dob.isEmpty()) {
            Toast.makeText(c, "Date of birth is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fName.length() < 2) {
            Toast.makeText(c, "First name must be at least 2 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!fName.matches("[a-zA-Z ]+")) { // note the space after Z
            Toast.makeText(c, "First name must contain only letters and spaces.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lName.length() < 2) {
            Toast.makeText(c, "Last name must be at least 2 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!lName.matches("[a-zA-Z ]+")) {
            Toast.makeText(c, "Last name must contain only letters.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDOB(dob)) {
            Toast.makeText(c, "Please select a valid date of birth (not in the future).", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> userData = UserDatabase.getUser(username);
        userData.put("firstName", fName);
        userData.put("lastName", lName);
        userData.put("dob", dob);
        userData.put("gender", gender);
        userData.put("bio", bio);

        Toast.makeText(c, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidDOB(String dob) {
        try {
            String[] parts = dob.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int year = Integer.parseInt(parts[2]);

            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day, 0, 0, 0);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return !selected.after(today);
        } catch (Exception e) {
            return false;
        }
    }

}//class
