package com.example.comp211.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

// the launch page and main menu page
public class MainActivity extends AppCompatActivity {

    // Buttons that navigate the user around
    Button spQuiz, mpQuiz, scoreboard, instructions;

    // setup of main menu page, defining buttons and the onClickListeners which react to the users
    // selection
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spQuiz = (Button) findViewById(R.id.spQuiz);
        mpQuiz = (Button) findViewById(R.id.mpQuiz);
        scoreboard = (Button) findViewById(R.id.leaderboard);
        instructions = (Button) findViewById(R.id.instructionsButton);

        // if the user presses single player mode button
        spQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add a dialog to register name or dismiss - start quiz afterwards
                AlertDialog.Builder display = new AlertDialog.Builder(MainActivity.this);
                display.setTitle(R.string.submitName);
                display.setMessage(getString(R.string.submitMessage));

                final EditText userInput = new EditText(MainActivity.this);
                userInput.setMaxLines(1);
                userInput.setHint("Enter name here");
                display.setView(userInput);

                // if the user presses "do not register" and plays without registering
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

                // add name submission and play
                display.setPositiveButton(R.string.saveName, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                String username = userInput.getText().toString();
                                Log.d("Arith3", username);
                                Intent k = new Intent (MainActivity.this, SingleQuiz.class);
                                k.putExtra("accessedQuestionlist", false);
                                k.putExtra("PlayerName", username);
                                startActivity(k);
                            }
                        }
                );

                // so that the alertdialog can be dismissed if the user changes his mind about
                // wanting to play single player
                display.setCancelable(true);

                //create alert dialog that was built
                AlertDialog loginPopup = display.create();
                loginPopup.show();
            }
        });

        // if the user selects to play Multiplayer mode
        mpQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add a dialog to register name or dismiss - start quiz afterwards
                AlertDialog.Builder display = new AlertDialog.Builder(MainActivity.this);
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                display.setTitle(R.string.submitNames);

                // here we have to editText boxes for both players names

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


                // adding names and starting the activity multiplayer
                display.setPositiveButton(R.string.mpStart, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                String username1 = userInput1.getText().toString();
                                String username2 = userInput2.getText().toString();
                                // input test
                                Log.d("Arith3", username1);
                                Log.d("Arith3", username2);

                                Intent h = new Intent (MainActivity.this, Multiplayer.class);
                                // adding user names to be passed onto Multiplayer.class
                                h.putExtra("Player1", username1);
                                h.putExtra("Player2", username2);
                                startActivity(h);
                            }
                        }
                );

                // allow going back
                display.setCancelable(true);

                //create alert dialog that was built
                AlertDialog loginPopup = display.create();
                loginPopup.show();

                //The check for input below does NOT work, since continuous checking would be needed
                // the checker below checks for input at creation
                // hence button would always be disabled
                // Such a continuous checker could be something added in the future
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

        // if user wants to see leaderboard
        scoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, Scoreboard.class);
                startActivity(k);
            }
        });

        // for user to see instructions
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, Instructions.class);
                startActivity(k);
            }
        });
    }
}
