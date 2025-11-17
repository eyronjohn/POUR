package com.example.madproject2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Context c;
    private EditText usernameET, passwordET, confirmPassET, fNameET, lNameET, emailET;
    private Button registerBtn, signInBtn ;
    private Spinner genderSpinner;
    private TextView pickDateButton, signInLink;
    private int year, month, day;
    private String selectedGender = "", selectedDOB = "";
    List<String> spinnerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        c = this;
        initialize();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initialize() {
        fNameET = findViewById(R.id.fName);
        lNameET = findViewById(R.id.lName);
        emailET = findViewById(R.id.email);
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        confirmPassET = findViewById(R.id.confirmPass);

        registerBtn = findViewById(R.id.registerBtn);
        signInBtn = findViewById(R.id.signInBtn);
        genderSpinner = findViewById(R.id.gender_spinner);
        pickDateButton = findViewById(R.id.pickDateButton);
        signInLink = findViewById(R.id.signInLink);

        spinnerItems.add("Male");
        spinnerItems.add("Female");
        spinnerItems.add("Non-binary");
        spinnerItems.add("Prefer not to say");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGender = "";
            }
        });

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        pickDateButton.setOnClickListener(v -> showDatePickerDialog());

        registerBtn.setOnClickListener(v -> registerUser());

        signInLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String fName = fNameET.getText().toString().trim();
        String lName = lNameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPass = confirmPassET.getText().toString().trim();

        /*
        // Empty Fields Checker
        if (fName.isEmpty()) {
            Toast.makeText(c, "First name is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lName.isEmpty()) {
            Toast.makeText(c, "Last name is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(c, "Email is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.isEmpty()) {
            Toast.makeText(c, "Username is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(c, "Password is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmPass.isEmpty()) {
            Toast.makeText(c, "Confirm password is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDOB.isEmpty()) {
            Toast.makeText(c, "Date of birth is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Validations
        if (fName.length() < 2) {
            Toast.makeText(c, "First name must be at least 2 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!fName.matches("[a-zA-Z ]+")) {
            Toast.makeText(c, "First name must contain only letters.", Toast.LENGTH_SHORT).show();
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

        if (username.length() < 4) {
            Toast.makeText(c, "Username must be at least 4 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!username.matches("[a-zA-Z0-9_]+")) {
            Toast.makeText(c, "Username can only contain letters, numbers, or underscores.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDOB.isEmpty() || !isValidDOB(selectedDOB)) {
            Toast.makeText(c, "Please select a valid date of birth (not in the future).", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(c, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(c, "Password is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(c, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.matches(".*[A-Za-z].*")) {
            Toast.makeText(c, "Password must contain at least one letter.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.matches(".*[0-9].*")) {
            Toast.makeText(c, "Password must contain at least one number.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPass)) {
            Toast.makeText(c, "Passwords do not match.", Toast.LENGTH_LONG).show();
            return;
        }

        if (UserDatabase.userExists(username)) {
            Toast.makeText(c, "A user with that username already exists.", Toast.LENGTH_SHORT).show();
            return;
        }
         */

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("firstName", fName);
        userData.put("lastName", lName);
        userData.put("email", email);
        userData.put("username", username);
        userData.put("password", password);
        userData.put("gender", selectedGender);
        userData.put("dob", selectedDOB);

        Bitmap defaultImg = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);
        userData.put("profileImage", defaultImg);
        UserDatabase.registerUser(username, userData);

        Toast.makeText(c, "Registration successful!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(c, LoginActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }, 1000);

        fNameET.setText("");
        lNameET.setText("");
        emailET.setText("");
        usernameET.setText("");
        passwordET.setText("");
        confirmPassET.setText("");
        genderSpinner.setSelection(0);
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
                        Toast.makeText(RegisterActivity.this, "Future dates are not allowed.", Toast.LENGTH_SHORT).show();
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
