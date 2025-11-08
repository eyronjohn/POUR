package com.example.madproject2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Context c = this;
    EditText usernameET, passwordET;
    Button loginBtn, signUpBtn;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        initialize();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void initialize(){
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

        sharedPrefs = getSharedPreferences("userDB", MODE_PRIVATE);

        loginBtn.setOnClickListener(v -> loginUser());

        signUpBtn.setOnClickListener(v -> {
            Intent intent = new Intent(c, RegisterActivity.class);
            startActivity(intent);
        });

        String prefillUsername = getIntent().getStringExtra("username");
        if (prefillUsername != null) {
            usernameET.setText(prefillUsername);
        }
    }

    public void loginUser(){
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(c, "Please fill all fields.", Toast.LENGTH_LONG).show();
        } else if(!sharedPrefs.contains(username)){
            Toast.makeText(c, "Username not found", Toast.LENGTH_SHORT).show();
        } else {
            String userData = sharedPrefs.getString(username, null);
            if (userData == null) {
                Toast.makeText(c, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                return;
            }
            try{
                JSONObject userJson = new JSONObject(userData);
                String storedUsername = userJson.getString("username");
                String storedPassword = userJson.getString("password");

                if (password.equals(storedPassword)) {
                    Toast.makeText(c, "Welcome back, " + storedUsername + "!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(c, ProfileActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    //finish();
                } else{
                    Toast.makeText(c, "Invalid user credentials.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(c, "Error reading user data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}//class