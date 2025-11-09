package com.example.madproject2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class ProfileTester extends AppCompatActivity {

    EditText usernameET, fNameET, lNameET, bioET;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_tester);
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
        bioET = findViewById(R.id.bio);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        if (username != null) {
            HashMap<String, String> userData = UserDatabase.getUser(username);

            if (userData != null) {
                usernameET.setText(username);
                fNameET.setText(userData.get("firstName"));
                lNameET.setText(userData.get("lastName"));
                bioET.setText(userData.get("bio"));
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

}