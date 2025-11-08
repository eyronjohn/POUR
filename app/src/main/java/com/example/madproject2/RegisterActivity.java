package com.example.madproject2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Context c = this;
    EditText usernameET, passwordET, confirmPassET;
    Button registerBtn, signInBtn;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
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
        confirmPassET = findViewById(R.id.confirmPass);
        registerBtn = findViewById(R.id.registerBtn);
        signInBtn = findViewById(R.id.signInBtn);

        sharedPrefs = getSharedPreferences("userDB", MODE_PRIVATE);

        registerBtn.setOnClickListener(v -> registerUser());

        signInBtn.setOnClickListener(v -> {
            Intent intent = new Intent(c, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmPass = confirmPassET.getText().toString();

        if (username.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(c, "Please fill all fields.", Toast.LENGTH_LONG).show();
        } else if (!password.equals(confirmPass)) {
            Toast.makeText(c, "Password does not match.", Toast.LENGTH_LONG).show();
        } else if(sharedPrefs.contains(username)){
            Toast.makeText(c, "A user with that username already exists.", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject userJson = new JSONObject();
            try {
                userJson.put("username", username);
                userJson.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sharedPrefs.edit().putString(username, userJson.toString()).apply();

            Toast.makeText(c, "Registration successful!", Toast.LENGTH_SHORT).show();new Handler().postDelayed(() -> {
                Intent intent = new Intent(c, LoginActivity.class);
                // pass the username to pre-fill username in login
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }, 1000);

            usernameET.setText("");
            passwordET.setText("");
            confirmPassET.setText("");
        }
    }

}//class