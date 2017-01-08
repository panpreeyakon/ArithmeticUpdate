package com.example.comp211.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Instructions extends AppCompatActivity {

    private TextView instructions;
    private Button returnToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        returnToMenu = (Button) findViewById(R.id.instructionsReturnToMenu);
        instructions = (TextView) findViewById(R.id.instructionsDescription);

        // adding the text for instructions in the java code because the text will be fairly long
        // alternatively could be added into separate xml resource or the string.xml
        instructions.setText("Singleplayer Mode: Challenge yourself and try to reach the top of the" +
                " Leaderboard!\nUse the button on the top right to switch to a different question." +
                "\nMultiplayer Mode: This is a Pass & Play Challenge. Enter your names " +
                "and the fun will begin. Look out in the top line to see who\'s turn it is.\n" +
                "Leaderboard: Hall of Fame for all Arithmeticians");


        //if click button
        returnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Instructions.this, MainActivity.class);
                startActivity(k);
            }
        });


    }
}
