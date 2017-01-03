package com.example.comp211.quiz;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class Questions_List_ActivityFragment extends Fragment {

    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;
    //private String nameID;
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

        Log.d("Arith ql frag", Integer.toString(questionList[1]));

        disableButtons();

        return view;
    }

    public View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button questionButton = ((Button) v);
            String guess = questionButton.getText().toString();
            int question_no = Integer.parseInt(guess);

            Log.d("Arith Qlist final", Integer.toString(questionList[1]));

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
        questionList = q;
        //System.arraycopy(q,0,questionList,0,10);
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
        enableButtons();
        checkButtons(questionList);
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.detach(this).attach(this).commit();
    }



    public void receiveScore(int score) {
        Score = score;
    }

    public void getPlayerName(String username) {
        playerName = username;
        Log.d("getPlayerName", playerName);
    }

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

    private void checkButtons(int[] questionList) {
        //disable buttons to questions that have been answered by user before
        if (questionList[1] == 1) {
            button1.setEnabled(false);
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
