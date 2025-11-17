package com.example.madproject2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class lifeLatelyList extends AppCompatActivity {

    Context c = this;
    Button lifeEntry;
    //ScrollView lifeContainer;
    LinearLayout lifeStyleMain;

    int small = 30;

    ActivityResultLauncher<Intent> lifeFormLauncher;
    HashMap<String, Object> userData;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_life_lately_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lifeLatelyList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        userData = UserDatabase.getUser(username);
        //Toast.makeText(this, "Test:" + username, Toast.LENGTH_SHORT).show();

        lifeFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Intent getLife = result.getData();

                        ArrayList<Integer> cbIcons = getLife.getIntegerArrayListExtra("cbIcons");
                        String noteLife = getLife.getStringExtra("lifeNote");

                        addLately(cbIcons, noteLife);
                    }
                }
        );
        addLife();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                Intent homeIntent = new Intent(lifeLatelyList.this, HomeActivity.class);
                homeIntent.putExtra("username", username);
                startActivity(homeIntent);
                return true;
            } else if (id == R.id.nav_journal) {
                //startActivity(new Intent(HomeActivity.this, journalLists.class));
                Intent journalIntent = new Intent(lifeLatelyList.this, journalLists.class);
                journalIntent.putExtra("username", username);
                startActivity(journalIntent);
                return true;
            } else if (id == R.id.nav_mood) {
                //startActivity(new Intent(HomeActivity.this, moodList.class));
                Intent moodIntent = new Intent(lifeLatelyList.this, moodList.class);
                moodIntent.putExtra("username", username);
                startActivity(moodIntent);
                return true;
            } else if (id == R.id.nav_lifestyle) {
                //startActivity(new Intent(HomeActivity.this, lifeLatelyList.class));
                //Intent lifestyleIntent = new Intent(lifeLatelyList.this, lifeLatelyList.class);
                //lifestyleIntent.putExtra("username", username);
                //startActivity(lifestyleIntent);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent profileIntent = new Intent(lifeLatelyList.this, ProfileActivity.class);
                profileIntent.putExtra("username", username);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }

    private void addLife() {
        lifeEntry = findViewById(R.id.lifeEntry);
        lifeEntry.setOnClickListener(v -> {
            Intent addLifeStyle = new Intent(c, lifelatelyForm.class);
            lifeFormLauncher.launch(addLifeStyle);
        });
    }

    private void addLately(ArrayList<Integer> icon, String note) {
        float scale = getResources().getDisplayMetrics().density;
        int size20 = (int) (small * scale + 0.5f);
        //lifeContainer = findViewById(R.id.lifeContainer);
        lifeStyleMain = findViewById(R.id.lifeStyleMain);

        LinearLayout timeDate = new LinearLayout(c);
        timeDate.setOrientation(LinearLayout.HORIZONTAL);
        timeDate.setGravity(Gravity.CENTER);
        timeDate.setPadding(0,0,0,20);

        Date current = new Date();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");

        String date = currentDate.format(current);
        String time = currentTime.format(current);


        TextView spaceBoth = new TextView(c);
        spaceBoth.setText(date + "     " + time);
        spaceBoth.setTextSize(20);

        timeDate.addView(spaceBoth);

        lifeStyleMain.addView(timeDate);

        LinearLayout cbIcon = new LinearLayout(c);
        cbIcon.setOrientation(LinearLayout.HORIZONTAL);
        cbIcon.setGravity(Gravity.CENTER);
        cbIcon.setPadding(0,0,0,20);

        if( icon != null) {
            for (int iconEach : icon) {
                ImageView img = new ImageView(c);
                img.setImageResource(iconEach);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size20,size20);
                params.setMargins(5,5,5,5);
                img.setLayoutParams(params);
                cbIcon.addView(img);
            }
        }
        lifeStyleMain.addView(cbIcon);

        TextView life = new TextView(c);
        life.setText(note);
        life.setTextSize(15);
        life.setPadding(0,0,0,20);
        life.setGravity(Gravity.CENTER);

        lifeStyleMain.addView(life);

        View bottomLine = new View(c);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        dividerParams.setMargins(0,30,0,30);
        bottomLine.setLayoutParams(dividerParams);
        bottomLine.setBackgroundColor(Color.parseColor("#6C6E1F"));
        lifeStyleMain.addView(bottomLine);


    }

}