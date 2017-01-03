package com.example.comp211.quiz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class Questions_List_Activity extends AppCompatActivity {

    public String playerName;
    public int score;
    public int[] questionsList = new int[11];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerName = getIntent().getStringExtra("PlayerName");
        questionsList = getIntent().getIntArrayExtra("questionList");
        score = getIntent().getIntExtra("score",0);

        Log.d("Arith ql act sc", Integer.toString(score));
        Log.d("Arith ql act", Integer.toString(questionsList[1]));

        setContentView(R.layout.activity_questions__list_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        questionslistFragment.getPlayerName(playerName);
        questionslistFragment.receiveScore(score);

        /*Log.d("startMenu", PlayerName);
        if (PlayerName != null)
            Log.d("startmenu", "not null");
        if (questionslistFragment != null)
            Log.d("questionlistFragment", "not null");
        */
        //questionslistFragment.getPlayerName(PlayerName);
    }
}
