package com.example.madproject2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    ImageView ivSelectedEmoji, yellow, blue, orange, green, red, purple, pink;
    TextView tvSelectedMood, tvFullName;
    HashMap<String, Object> userData;
    String username, fName, lName;
    ShapeableImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        initialize();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });
    }

    private void initialize() {
        tvFullName = findViewById(R.id.tvFullName);
        profileImage = findViewById(R.id.profileImage);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        userData = UserDatabase.getUser(username);

        //load profileImage if changed
        Bitmap savedImage = (Bitmap) userData.get("profileImage");
        if (savedImage != null) {
            profileImage.setImageBitmap(savedImage);
        }

        fName = (String) userData.get("firstName");
        lName = (String) userData.get("lastName");
        String fullName = fName + " " + lName;
        //Toast.makeText(HomeActivity.this, "Welcome back, " + fullName + "!", Toast.LENGTH_SHORT).show();


        tvFullName.setText(fullName);

        ivSelectedEmoji = findViewById(R.id.ivSelectedEmoji);
        tvSelectedMood = findViewById(R.id.tvSelectedMood);

        yellow = findViewById(R.id.yellow);
        blue = findViewById(R.id.blue);
        orange = findViewById(R.id.orange);
        green = findViewById(R.id.green);
        red = findViewById(R.id.red);
        purple = findViewById(R.id.purple);
        pink = findViewById(R.id.pink);

        yellow.setOnClickListener(clicked);
        blue.setOnClickListener(clicked);
        orange.setOnClickListener(clicked);
        green.setOnClickListener(clicked);
        red.setOnClickListener(clicked);
        purple.setOnClickListener(clicked);
        pink.setOnClickListener(clicked);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                return true;
            } else if (id == R.id.nav_journal) {
                //startActivity(new Intent(HomeActivity.this, journalLists.class));
                Intent journalIntent = new Intent(HomeActivity.this, journalLists.class);
                journalIntent.putExtra("username", username);
                startActivity(journalIntent);
                return true;
            } else if (id == R.id.nav_mood) {
                //startActivity(new Intent(HomeActivity.this, moodList.class));
                Intent moodIntent = new Intent(HomeActivity.this, moodList.class);
                moodIntent.putExtra("username", username);
                startActivity(moodIntent);
                return true;
            } else if (id == R.id.nav_lifestyle) {
                //startActivity(new Intent(HomeActivity.this, lifeLatelyList.class));
                Intent lifestyleIntent = new Intent(HomeActivity.this, lifeLatelyList.class);
                lifestyleIntent.putExtra("username", username);
                startActivity(lifestyleIntent);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                profileIntent.putExtra("username", username);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }

    View.OnClickListener clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.yellow){
                ivSelectedEmoji.setImageResource(R.drawable.yellow);
                tvSelectedMood.setText("I'm feeling Happy!");
                tvSelectedMood.setTextColor(getColor(R.color.yellow));
            }
            else if (v.getId()==R.id.blue) {
                ivSelectedEmoji.setImageResource(R.drawable.blue);
                tvSelectedMood.setText("I'm feeling Sad!");
                tvSelectedMood.setTextColor(getColor(R.color.blue));
            }
            else if (v.getId()==R.id.orange) {
                ivSelectedEmoji.setImageResource(R.drawable.orange);
                tvSelectedMood.setText("I'm feeling Anxious!");
                tvSelectedMood.setTextColor(getColor(R.color.orange));
            }
            else if (v.getId()==R.id.green) {
                ivSelectedEmoji.setImageResource(R.drawable.green);
                tvSelectedMood.setText("I'm feeling Sick!");
                tvSelectedMood.setTextColor(getColor(R.color.green));
            }
            else if (v.getId()==R.id.red) {
                ivSelectedEmoji.setImageResource(R.drawable.red);
                tvSelectedMood.setText("I'm feeling Angry!");
                tvSelectedMood.setTextColor(getColor(R.color.red));
            }
            else if (v.getId()==R.id.purple) {
                ivSelectedEmoji.setImageResource(R.drawable.purple);
                tvSelectedMood.setText("I'm feeling Scared!");
                tvSelectedMood.setTextColor(getColor(R.color.purple));
            }
            else if (v.getId()==R.id.pink) {
                ivSelectedEmoji.setImageResource(R.drawable.pink);
                tvSelectedMood.setText("I'm feeling Loved!");
                tvSelectedMood.setTextColor(getColor(R.color.pink));
            }
        }
    };
}