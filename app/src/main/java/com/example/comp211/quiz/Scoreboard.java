package com.example.comp211.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by delon on 31.12.2016.
 */

public class Scoreboard extends AppCompatActivity {

    String myQuiz = "";
    Button returnToMenu;
    TextView leaderboardDescription;
    ScoreboardData db = new ScoreboardData(this, 1);
    int rows;
    ArrayList<Player> players = new ArrayList<>();

    LinearLayout l;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);

        returnToMenu = (Button) findViewById(R.id.bReturnToMenu);
        leaderboardDescription = (TextView) findViewById(R.id.leaderboardDescription);

        // score bit
        l = (LinearLayout) findViewById(R.id.outlineLinearScroll);

        //Bundle extras = getIntent().getExtras();
        //if (extras != null)
        //    myQuiz = extras.getString("quiz");

        leaderboardDescription.setText("Quiz Leaderboard");
        loadLeaderboard();
        /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.leaderboardFrag, outlineFrag);
        fragmentTransaction.commit();
        */

        //if click button
        returnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Scoreboard.this, MainActivity.class);
                startActivity(k);
            }
        });
    }

    public void loadLeaderboard() {


        db = new ScoreboardData(this, 1);
        rows = db.numRows();

        for (int i = 0; i <= rows - 1; i++) {
            Player playerToAdd;
            playerToAdd = db.getRowProductData(i);
            players.add(playerToAdd);
        }

        for (int i = 0; i < players.size(); i++) {

            LinearLayout l2 = new LinearLayout(this);
            TextView nameTV = new TextView(this);
            TextView scoreTV = new TextView(this);

            nameTV.setText(players.get(i).getName());
            //nameTV.setPadding(0, 0, 35, 0);
            nameTV.setWidth(435);
            nameTV.setTextSize(20);
            scoreTV.setText(Integer.toString(players.get(i).getScore()));
            scoreTV.setTextSize(20);
            //scoreTV.setPadding(0, 35, 0, 0);
            //double roundOff = Math.round(products.get(i).getSellPrice() * 100.0) / 100.0;

            l2.addView(nameTV);
            l2.addView(scoreTV);
            l.addView(l2);

        }
    }
}

