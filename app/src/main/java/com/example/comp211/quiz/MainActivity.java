package com.example.comp211.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    Button spQuiz, mpQuiz, scoreboard, instructions;
    public String username;
    private boolean phoneDevice = true; // force portrait mode when device is a phone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Portrait Orientation setting
        // determine screen size
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        // if device is a tablet, set phoneDevice to false
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            phoneDevice = false; // not a phone-sized device

        // if running on phone-sized device, allow only portrait orientation
        if (phoneDevice)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        spQuiz = (Button) findViewById(R.id.spQuiz);
        mpQuiz = (Button) findViewById(R.id.mpQuiz);
        scoreboard = (Button) findViewById(R.id.leaderboard);
        instructions = (Button) findViewById(R.id.instructionsButton);

        spQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent k = new Intent(SingleQuiz.this, SingleQuizFragment.class);
                //startActivity(k);
                // add a dialog to register name or dismiss - start quiz afterwards
                AlertDialog.Builder display = new AlertDialog.Builder(MainActivity.this);
                display.setTitle(R.string.submitName);
                display.setMessage(getString(R.string.submitMessage));

                final EditText userInput = new EditText(MainActivity.this);
                //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                //        LinearLayout.LayoutParams.MATCH_PARENT,
                //        LinearLayout.LayoutParams.MATCH_PARENT);
                userInput.setMaxLines(1);
                //userInput.setLayoutParams(lp);
                userInput.setHint("Enter name here");
                display.setView(userInput);

                display.setNegativeButton(R.string.noName, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Intent k = new Intent (MainActivity.this, SingleQuiz.class);
                                k.putExtra("PlayerName", "");
                                k.putExtra("accessedQuestionlist", false);
                                startActivity(k);
                            }
                        }
                );

                // add name submission
                display.setPositiveButton(R.string.submitScore, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                username = userInput.getText().toString();
                                Log.d("Arith3", username);
                                Intent k = new Intent (MainActivity.this, SingleQuiz.class);
                                k.putExtra("accessedQuestionlist", false);
                                k.putExtra("PlayerName", username);
                                startActivity(k);
                            }
                        }
                );

                display.setCancelable(true);

                //create alert dialog that was built
                AlertDialog loginPopup = display.create();
                loginPopup.show();
                /*
                Button button = loginPopup.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                if (userInput.getText().equals("") ||
                        userInput.getText().equals(null) ||
                        userInput.getText().toString().trim().length() == 0) {
                    button.setEnabled(false);
                } */

            }
        });

        mpQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add a dialog to register name or dismiss - start quiz afterwards
                AlertDialog.Builder display = new AlertDialog.Builder(MainActivity.this);
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                display.setTitle(R.string.submitNames);

                final EditText userInput1 = new EditText(MainActivity.this);
                userInput1.setMaxLines(1);
                //userInput1.setLayoutParams(lp);
                userInput1.setHint("Player 1/'s name");
                layout.addView(userInput1);

                final EditText userInput2 = new EditText(MainActivity.this);
                userInput2.setMaxLines(1);
                userInput2.setHint("Player 2/'s name");
                layout.addView(userInput2);

                display.setView(layout);


                // add name submission
                display.setPositiveButton(R.string.mpStart, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                String username1 = userInput1.getText().toString();
                                String username2 = userInput2.getText().toString();
                                Log.d("Arith3", username1);
                                Log.d("Arith3", username2);
                                Intent h = new Intent (MainActivity.this, Multiplayer.class);
                                h.putExtra("Player1", username1);
                                h.putExtra("Player2", username2);
                                startActivity(h);
                            }
                        }
                );

                display.setCancelable(true);

                //create alert dialog that was built
                AlertDialog loginPopup = display.create();
                loginPopup.show();

                //check for input does NOT work, since continuous checking would be needed
                // the checker below checks for input at creation
                // hence button would always be disabled
                // this continous checker could be something added in the future
                /*
                Button button = loginPopup.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);

                if (userInput1.getText().equals("") ||
                        userInput2.getText().equals("") ||
                        userInput1.getText().equals(null) ||
                        userInput2.getText().equals(null) ||
                        userInput1.getText().toString().trim().length() == 0 ||
                        userInput2.getText().toString().trim().length() == 0) {
                    button.setEnabled(false);
                }
                */

            }
        });

        scoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, Scoreboard.class);
                startActivity(k);
            }
        });
/*
        registering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(SingleQuiz.this, Instructions.class);
                startActivity(k);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(SingleQuiz.this, AboutUs.class);
                startActivity(k);
            }
        }); */
    }




}
