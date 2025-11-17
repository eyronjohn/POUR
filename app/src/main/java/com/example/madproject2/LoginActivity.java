package com.example.madproject2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private Context c;
    private EditText usernameET, passwordET;
    private TextView signUpLink;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        c = this;
        initialize();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initialize() {
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signUpLink = findViewById(R.id.signUpLink);

        loginBtn.setOnClickListener(v -> loginUser());

        signUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        String prefillUsername = getIntent().getStringExtra("username");
        if (prefillUsername != null) usernameET.setText(prefillUsername);
    }

    private void loginUser() {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if(username.isEmpty()){
            Toast.makeText(c, "Please enter your username.", Toast.LENGTH_LONG).show();
            return;
        }

        if(password.isEmpty()){
            Toast.makeText(c, "Please enter your password.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!UserDatabase.userExists(username)) {
            Toast.makeText(c, "Username not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (UserDatabase.validateLogin(username, password)) {
            HashMap<String, Object> userData = UserDatabase.getUser(username);
            Intent intent = new Intent(c, HomeActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("fName", (String) userData.get("firstName"));
            intent.putExtra("lName", (String) userData.get("lastName"));
            //intent.putExtra("email", (String) userData.get("email"));
            //intent.putExtra("gender", (String) userData.get("gender"));
            //intent.putExtra("dob", (String) userData.get("dob"));
            startActivity(intent);

            Toast.makeText(c, "Welcome back, " + username + "!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(c, "Invalid user credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}
