package com.example.comp211.quiz;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A fragment that fills the Question List Activity and presents the user with a list of all questions
 * and allows him/her to select a question to answer first - the switch-question function
 */
public class Questions_List_ActivityFragment extends Fragment {

    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;
    public int[] questionList = new int[11];
    public int Score;
    public String playerName;

    public Questions_List_ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_questions__list_, container, false);

        // setting up the ten buttons for all ten questions
        button1 = (Button) view.findViewById(R.id.q1);
        button2 = (Button) view.findViewById(R.id.q2);
        button3 = (Button) view.findViewById(R.id.q3);
        button4 = (Button) view.findViewById(R.id.q4);
        button5 = (Button) view.findViewById(R.id.q5);
        button6 = (Button) view.findViewById(R.id.q6);
        button7 = (Button) view.findViewById(R.id.q7);
        button8 = (Button) view.findViewById(R.id.q8);
        button9 = (Button) view.findViewById(R.id.q9);
        button10 = (Button) view.findViewById(R.id.q10);

        // setting up the buttons to able to react on clicking them
        button1.setOnClickListener(guessButtonListener);
        button2.setOnClickListener(guessButtonListener);
        button3.setOnClickListener(guessButtonListener);
        button4.setOnClickListener(guessButtonListener);
        button5.setOnClickListener(guessButtonListener);
        button6.setOnClickListener(guessButtonListener);
        button7.setOnClickListener(guessButtonListener);
        button8.setOnClickListener(guessButtonListener);
        button9.setOnClickListener(guessButtonListener);
        button10.setOnClickListener(guessButtonListener);

        // button labelling for each question number
        button1.setText("1");
        button2.setText("2");
        button3.setText("3");
        button4.setText("4");
        button5.setText("5");
        button6.setText("6");
        button7.setText("7");
        button8.setText("8");
        button9.setText("9");
        button10.setText("10");

        // input test
        Log.d("Arith ql frag", Integer.toString(questionList[1]));

        // inititally disable all buttons when onCreate is called
        disableButtons();

        return view;
    }

    // setting up the onClickListener - here we only need one that responds depending on the
    // question no of the button
    public View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button questionButton = ((Button) v);
            String guess = questionButton.getText().toString();
            int question_no = Integer.parseInt(guess);

            //testing data input
            Log.d("Arith Qlist final", Integer.toString(questionList[1]));

            // using an Intent to switch back to the quiz while adding the necessary information
            Intent k = new Intent(getActivity(), SingleQuiz.class);
            k.putExtra("jumpTo", question_no);
            k.putExtra("accessedQuestionlist", true);
            k.putExtra("PlayerName", playerName);
            k.putExtra("score", Score);
            k.putExtra("questionList", questionList);
            startActivity(k);
        }
    };


    public void getQuestionList(int[] q) {
        // first get information about which questions have been answered from the question list
        // activity which in turn received the information through the intent when it was called
        questionList = q;
        // used during developing to test whether data had been transmitted between activities
        Log.d("Arith Qlist", "called");
        Log.d("Arith Qlist 1", Integer.toString(questionList[1]));
        Log.d("Arith Qlist 2", Integer.toString(questionList[2]));
        Log.d("Arith Qlist 3", Integer.toString(questionList[3]));
        Log.d("Arith Qlist 4", Integer.toString(questionList[4]));
        Log.d("Arith Qlist 5", Integer.toString(questionList[5]));
        Log.d("Arith Qlist 6", Integer.toString(questionList[6]));
        Log.d("Arith Qlist 7", Integer.toString(questionList[7]));
        Log.d("Arith Qlist 8", Integer.toString(questionList[8]));
        Log.d("Arith Qlist 9", Integer.toString(questionList[9]));
        Log.d("Arith Qlist 10", Integer.toString(questionList[10]));

        // enable all buttons
        enableButtons();
        // then disable those that correspond to the questions that have been answered before
        checkButtons(questionList);
    }

    // to allow the fragment to receive the users score before he switched questions
    public void receiveScore(int score) {
        Score = score;
    }

    // to allow for the fragment to receive the username from the Question_List_Activity
    // this is needed because we switch to the singlequiz activity directly from the fragment
    public void getPlayerName(String username) {
        playerName = username;
        Log.d("getPlayerName", playerName);
    }

    // to disable all ten question buttons
    private void disableButtons() {
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        button5.setEnabled(false);
        button6.setEnabled(false);
        button7.setEnabled(false);
        button8.setEnabled(false);
        button9.setEnabled(false);
        button10.setEnabled(false);
    }

    // utility method that disables all answer Buttons
    private void enableButtons() {
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        button5.setEnabled(true);
        button6.setEnabled(true);
        button7.setEnabled(true);
        button8.setEnabled(true);
        button9.setEnabled(true);
        button10.setEnabled(true);
    }

    // this method is using the array to check a question has been answered before, if so the
    // corresponding button will be disabled
    // (remember that we use the array by adding a the number x of the question at the x - position
    // in the array only if the question has been answered)
    private void checkButtons(int[] questionList) {
        //disable buttons to questions that have been answered by user before
        if (questionList[1] == 1) {
            button1.setEnabled(false);
            // test data input
            Log.d("Arith test", "it ran");
        }
        if (questionList[2] == 2)
            button2.setEnabled(false);
        if (questionList[3] == 3)
            button3.setEnabled(false);
        if (questionList[4] == 4)
            button4.setEnabled(false);
        if (questionList[5] == 5)
            button5.setEnabled(false);
        if (questionList[6] == 6)
            button6.setEnabled(false);
        if (questionList[7] == 7)
            button7.setEnabled(false);
        if (questionList[8] == 8)
            button8.setEnabled(false);
        if (questionList[9] == 9)
            button9.setEnabled(false);
        if (questionList[10] == 10)
            button10.setEnabled(false);

    }

}
