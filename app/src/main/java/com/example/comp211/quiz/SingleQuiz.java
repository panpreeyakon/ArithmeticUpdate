package com.example.comp211.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import static com.example.comp211.quiz.R.id.quizFragment;
import static com.example.comp211.quiz.R.id.toolbar;

public class SingleQuiz extends AppCompatActivity {

    public String playerName;
    public boolean switchedQuestion;
    public int Score, jumpToQuestion;
    public int[] questionList;
    private boolean phoneDevice = true; // force portrait mode when device is a phone

    //configuring the SingleQuiz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_quiz);

        // pass username from the edittext field that the user entered in the AlertDialog
        playerName = getIntent().getStringExtra("PlayerName");
        switchedQuestion = getIntent().getBooleanExtra("accessedQuestionlist", false);
        Score = getIntent().getIntExtra("score", 0);
        jumpToQuestion = getIntent().getIntExtra("jumpTo", 0);
        if (switchedQuestion) {
            questionList = getIntent().getIntArrayExtra("questionList");
            Log.d("Arith back sq", "switched");
            Log.d("Arith backsq ql 1", Integer.toString(questionList[1]));
            Log.d("Arith backsq ql 2", Integer.toString(questionList[2]));
            Log.d("Arith backsq ql 3", Integer.toString(questionList[3]));
            Log.d("Arith backsq ql 4", Integer.toString(questionList[4]));
            Log.d("Arith backsq ql 5", Integer.toString(questionList[5]));
        }
            //System.arraycopy(getIntent().getIntArrayExtra("questionList"),0, Questionlist,0,10);

        //switchedQuestion = getIntent().getBooleanExtra("accessedQuestionlist", false);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
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
        */

    }


    // called after onCreate completes execution
    @Override
    protected void onStart() {
        super.onStart();
        // to get values from the progress the player had before switching

        SingleQuizFragment quizFragment = (SingleQuizFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.quizFragment);
        Log.d("startQuiz", playerName);
        if (playerName != null)
            Log.d("startQuiz", "not null");
        if (quizFragment != null)
            Log.d("quizFragment", "not null");

        quizFragment.getPlayerName(playerName);

        if (switchedQuestion) {
            //pass values (questiontracker and score) to fragment and do not reset questions
            quizFragment.receiveScore(Score);
            quizFragment.receiveQuestionTracker(questionList);
            quizFragment.jumpToQuestion(jumpToQuestion);
        }
        else
        quizFragment.resetQuiz();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // check orientation first
        int orientation = getResources().getConfiguration().orientation;

        // Inflate menu only if orientation is portrait
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        SingleQuizFragment quizFragment = (SingleQuizFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.quizFragment);
        Intent questionListIntent = new Intent(this, Questions_List_Activity.class);
        questionListIntent.putExtra("PlayerName", playerName);
        questionListIntent.putExtra("questionList", quizFragment.sendQuestionTracker());
        questionListIntent.putExtra("score", quizFragment.sendScore());

        Log.d("Arith send Qtrack", Integer.toString(quizFragment.sendQuestionTracker()[1]));

        startActivity(questionListIntent);

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {}

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
