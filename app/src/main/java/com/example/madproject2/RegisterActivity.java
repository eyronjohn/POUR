package com.example.madproject2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
    private TextView pickDateButton;
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
        signInBtn.setOnClickListener(v -> {
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

        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || username.isEmpty() ||
                password.isEmpty() || confirmPass.isEmpty() || selectedGender.isEmpty() || selectedDOB.isEmpty()) {
            Toast.makeText(c, "Please fill all fields.", Toast.LENGTH_LONG).show();
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

        HashMap<String, String> userData = new HashMap<>();
        userData.put("firstName", fName);
        userData.put("lastName", lName);
        userData.put("email", email);
        userData.put("username", username);
        userData.put("password", password);
        userData.put("gender", selectedGender);
        userData.put("dob", selectedDOB);

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
