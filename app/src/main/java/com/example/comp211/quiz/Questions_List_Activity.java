package com.example.comp211.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class Questions_List_Activity extends AppCompatActivity {

    public String playerName;
    public int score;
    // the array that saves the progress of the user
    public int[] questionsList = new int[11];

    // Executed first when activity is called.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // receive information
        playerName = getIntent().getStringExtra("PlayerName");
        questionsList = getIntent().getIntArrayExtra("questionList");
        score = getIntent().getIntExtra("score",0);

        // test correct data receipt
        Log.d("Arith ql act sc", Integer.toString(score));
        Log.d("Arith ql act", Integer.toString(questionsList[1]));

        // set-up
        setContentView(R.layout.activity_questions__list_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get reference to fragment and send the question list tracking user progress
        Questions_List_ActivityFragment questionslistFragment = (Questions_List_ActivityFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.questions_list_fragment);
        questionslistFragment.getQuestionList(questionsList);
    }

    // called after onCreate completes execution
    @Override
    protected void onStart() {
        super.onStart();
        Questions_List_ActivityFragment questionslistFragment = (Questions_List_ActivityFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.questions_list_fragment);
        // send information on score and player name to fragment
        questionslistFragment.getPlayerName(playerName);
        questionslistFragment.receiveScore(score);
    }
}
