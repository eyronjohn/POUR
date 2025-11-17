package com.example.madproject2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class moodList extends AppCompatActivity {

    Button moodCRUD;
    Context c = this;

    LinearLayout iconLayout;

    ActivityResultLauncher<Intent> moodFormLauncher;
    HashMap<String, Object> userData;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mood_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mood_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        userData = UserDatabase.getUser(username);

        moodFormLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK && o.getData() != null) {
                            Intent moodData = o.getData();
                            int moodRN = moodData.getIntExtra("moodCurrent", 0);

                            if (moodRN!=0) {
                                ImageView iconic = new ImageView(c);
                                iconic.setImageResource(moodRN);
                                moodPutter(moodRN);
                            }
                        }
                    }
                }
        );

        iconLayout = findViewById(R.id.iconLayout);
        moodLister();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                Intent homeIntent = new Intent(moodList.this, HomeActivity.class);
                homeIntent.putExtra("username", username);
                startActivity(homeIntent);
                return true;
            } else if (id == R.id.nav_journal) {
                //startActivity(new Intent(HomeActivity.this, journalLists.class));
                Intent journalIntent = new Intent(moodList.this, journalLists.class);
                journalIntent.putExtra("username", username);
                startActivity(journalIntent);
                return true;
            } else if (id == R.id.nav_mood) {
                //startActivity(new Intent(HomeActivity.this, moodList.class));
                //Intent moodIntent = new Intent(moodList.this, moodList.class);
                //moodIntent.putExtra("username", username);
                //startActivity(moodIntent);
                return true;
            } else if (id == R.id.nav_lifestyle) {
                //startActivity(new Intent(HomeActivity.this, lifeLatelyList.class));
                Intent lifestyleIntent = new Intent(moodList.this, lifeLatelyList.class);
                lifestyleIntent.putExtra("username", username);
                startActivity(lifestyleIntent);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent profileIntent = new Intent(moodList.this, ProfileActivity.class);
                profileIntent.putExtra("username", username);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }

    private void moodPutter(int moodRes) {

        LinearLayout row = new LinearLayout(c);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(20,20,20,20);

        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        rowParams.setMargins(20,15,20,15);
        row.setLayoutParams(rowParams);
        row.setGravity(Gravity.CENTER_VERTICAL);

        ImageView icon = new ImageView(c);
        LinearLayout.LayoutParams iconSize =
                new LinearLayout.LayoutParams(200, 200);
        iconSize.gravity = Gravity.CENTER;
        icon.setLayoutParams(iconSize);
        icon.setImageResource(moodRes);


        TextView moodText = new TextView(c);

        if (moodRes==R.drawable.scared) {
            moodText.setText("Scared");
            moodText.setTextColor(getResources().getColor(R.color.purple));
        }
        else if (moodRes==R.drawable.happy) {
            moodText.setText("Happy");
            moodText.setTextColor(getResources().getColor(R.color.yellow));
        }
        else if (moodRes==R.drawable.angry) {
            moodText.setText("Angry");
            moodText.setTextColor(getResources().getColor(R.color.red));
        }
        else if (moodRes==R.drawable.dizzy) {
            moodText.setText("Dizzy");
            moodText.setTextColor(getResources().getColor(R.color.orange));
        }
        else if (moodRes==R.drawable.drool) {
            moodText.setText("Drool");
            moodText.setTextColor(getResources().getColor(R.color.green));
        }
        else if (moodRes==R.drawable.kiss) {
            moodText.setText("Smooch");
            moodText.setTextColor(getResources().getColor(R.color.pink));
        }
        else if (moodRes==R.drawable.sad) {
            moodText.setText("Sad");
            moodText.setTextColor(getResources().getColor(R.color.blue));
        }

        moodText.setTextSize(30);
        moodText.setTypeface(null, Typeface.BOLD);

        TextView dateNow = new TextView(c);
        dateNow.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateNow.setGravity(Gravity.START);

        TextView timeNow = new TextView(c);
        timeNow.setText(new SimpleDateFormat("hh:mm a").format(new Date()));
        timeNow.setGravity(Gravity.START);

        LinearLayout textColumn = new LinearLayout(c);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        textColumn.setPadding(30, 0, 30, 0);
        textColumn.setGravity(Gravity.CENTER);

        textColumn.addView(moodText);
        textColumn.addView(dateNow);
        textColumn.addView(timeNow);

        row.addView(icon);
        row.addView(textColumn);

        row.setBackgroundResource(R.drawable.round);

        iconLayout.addView(row);
    }

    private void moodLister() {
        moodCRUD = findViewById(R.id.moodCRUD);

        moodCRUD.setOnClickListener(v -> {
            Intent switchMood = new Intent(c, moodForm.class);
            moodFormLauncher.launch(switchMood);
        });
    }
}