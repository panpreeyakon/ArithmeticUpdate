package com.example.comp211.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

public class SingleQuiz extends AppCompatActivity {

    public String playerName;
    public boolean switchedQuestion;
    public int Score, jumpToQuestion;
    public int[] questionList;

    //configuring the SingleQuiz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_quiz);

        // pass username from the edittext field that the user entered in the AlertDialog through an
        // intent
        playerName = getIntent().getStringExtra("PlayerName");
        switchedQuestion = getIntent().getBooleanExtra("accessedQuestionlist", false);
        Score = getIntent().getIntExtra("score", 0);
        jumpToQuestion = getIntent().getIntExtra("jumpTo", 0);

        //retrieve previously answered questions through the intent if user has switched questions
        if (switchedQuestion) {
            questionList = getIntent().getIntArrayExtra("questionList");
            // used when developing the code to check correct transmission of list
            Log.d("Arith back sq", "switched");
            Log.d("Arith backsq ql 1", Integer.toString(questionList[1]));
            Log.d("Arith backsq ql 2", Integer.toString(questionList[2]));
            Log.d("Arith backsq ql 3", Integer.toString(questionList[3]));
            Log.d("Arith backsq ql 4", Integer.toString(questionList[4]));
            Log.d("Arith backsq ql 5", Integer.toString(questionList[5]));
        }

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    // called after onCreate completes execution
    @Override
    protected void onStart() {
        super.onStart();
        // to get values from the progress the player had before switching we create a reference to
        // the fragment below
        SingleQuizFragment quizFragment = (SingleQuizFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.quizFragment);

        // used to test correct input and output from transmission
        // originally used to avoid NPE null pointer exception
        Log.d("startQuiz", playerName);
        if (playerName != null)
            Log.d("startQuiz", "not null");
        if (quizFragment != null)
            Log.d("quizFragment", "not null");

        // send the players name to the fragment through the method so the fragment can use it later
        quizFragment.getPlayerName(playerName);

        // here we distinguish what happens depending on whether the player was switching to another
        // activity bc he switched questions or started a new quiz
        if (switchedQuestion) {
            //pass values (questiontracker and score) to fragment and do not reset questions
            quizFragment.receiveScore(Score);
            quizFragment.receiveQuestionTracker(questionList);
            quizFragment.jumpToQuestion(jumpToQuestion);
        }
        else
        quizFragment.resetQuiz();
    }

    // to create the options menu ( list of questions) when the button is pressed
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //vwhen the user pressed the button on the top right corner to see the full list of questions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // sending information from current progress (player name, score and questions answered
        // so progress is not lost when he returns or selects a question
        SingleQuizFragment quizFragment = (SingleQuizFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.quizFragment);
        Intent questionListIntent = new Intent(this, Questions_List_Activity.class);
        questionListIntent.putExtra("PlayerName", playerName);
        questionListIntent.putExtra("questionList", quizFragment.sendQuestionTracker());
        questionListIntent.putExtra("score", quizFragment.sendScore());

        // testing data transmission
        Log.d("Arith send Qtrack", Integer.toString(quizFragment.sendQuestionTracker()[1]));

        // switching to another activity via Intent
        startActivity(questionListIntent);

        return super.onOptionsItemSelected(item);
    }

    // to disable the back button on the phone
    @Override
    public void onBackPressed() {}


    // ------------------------------------ Can be ignored ---------------------------------------
    // this was the original method I found on the internet to retain information within an activity
    // when a user switches between different activies, the methods below should save information
    // when onStop is executed and reload the information back when the activity is relaunched
    // however for simplicity and ability to test data I later decided to simply use putExtra() into
    // Intents instead - as seen above
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // save playername registered before, if app is interrupted, due to launching question list activity
        SingleQuizFragment quizFragment = (SingleQuizFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.quizFragment);
        savedInstanceState.putString("PlayerName", playerName);
        savedInstanceState.putInt("score", quizFragment.sendScore());
        savedInstanceState.putIntArray("questionList", quizFragment.sendQuestionTracker());
        Log.d("Arith Save data", playerName);


        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);


        // Restore state members from saved instance
        playerName = savedInstanceState.getString("PlayerName");
        Score = savedInstanceState.getInt("score");
        questionList = savedInstanceState.getIntArray("questionList");
        Log.d("Arith restore data", playerName);
    }
}
