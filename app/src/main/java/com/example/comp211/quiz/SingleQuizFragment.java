package com.example.comp211.quiz;

import java.util.List;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class SingleQuizFragment extends Fragment {

    //used when we log error messages using class Log to be able to distinguish error msgs
    private static final String TAG = "Arithmetic";

    // no of questions in the quiz
    private static final int NUMBER_OF_QUESTIONS = 10;

    // for the question database
    private QuestionCreate db;
    private List<Question> quesList;
    int qid = 0, databaseVersion, jumpTo;
    private Question currentQ;

    // for score submission
    ScoreboardData scoredb;
    String nameID;

    private int totalGuesses; // number of guesses made
    private int correctAnswers; // number of correct guesses
    private Handler handler; // used to delay loading next question
    private Animation shakeAnimation; // animation for incorrect guess

    private LinearLayout quizLinearLayout; // layout that contains the quiz
    private TextView questionNumberTextView; // shows current question #
    private TextView mathsQuestionTextView; // displays a maths question
    private TextView answerTextView; // displays correct answer
    private Button button1, button2, button3, button4, button5, button6;

    //introducing a questiontracker used for switching around with questions, int[0] shall always be
    // empty and from [1]-[11] the tracker whether a question has been answered.
    // if one of those entries is empty then the question has not been answered yet.
    private int[] questionTracker = new int[11]; // saves which questions that have been tackled

    public SingleQuizFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.single_quiz_fragment, container, false);

        // used delaying actions later
        handler = new Handler();

        // load the shake animation that's used for incorrect answers
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3); // animation repeats 3 times

        // get references to GUI components
        quizLinearLayout = (LinearLayout) view.findViewById(R.id.quizFragment);
        questionNumberTextView = (TextView) view.findViewById(R.id.questionNumberTextView);
        mathsQuestionTextView = (TextView) view.findViewById(R.id.mathsQuestionTextView);
        button1 = (Button) view.findViewById(R.id.button);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);
        button4 = (Button) view.findViewById(R.id.button4);
        button5 = (Button) view.findViewById(R.id.button5);
        button6 = (Button) view.findViewById(R.id.button6);
        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        button1.setOnClickListener(guessButtonListener);
        button2.setOnClickListener(guessButtonListener);
        button3.setOnClickListener(guessButtonListener);
        button4.setOnClickListener(guessButtonListener);
        button5.setOnClickListener(guessButtonListener);
        button6.setOnClickListener(guessButtonListener);

        return view; // return the fragment's view for display
    }

    //Called once the quiz finished and before the new quiz is started
    public void resetQuiz() {
        correctAnswers = 0;
        totalGuesses = 0;
        questionTracker = new int[11];

        qid = 0;

        db = new QuestionCreate(getActivity());  // my question bank class
        // line below resets questions in the database to ensure new questions are used
        db.resetQuestions();
        quesList = db.getAllQuestions();  // this will fetch all questions
        Log.d(TAG, quesList.get(0).getANSWER());

        scoredb = new ScoreboardData(getActivity(),1);

        loadNextQuestion();
    }

    // only used for Singleplayer mode where user is allowed to switch questions
    // this is called when he does that and wants to switch to question i
    public void jumpToQuestion(int i) {

        qid = i - 1;
        // load previous questions
        db = new QuestionCreate(getActivity());
        scoredb = new ScoreboardData(getActivity(), 1);
        quesList = db.getAllQuestions();  // this will fetch all questions

        // checking the question that was pulled - used during developing code
        Log.d(TAG, quesList.get(0).getANSWER());


        questionNumberTextView.setText(getString(R.string.questions, (qid + 1), NUMBER_OF_QUESTIONS));

        answerTextView.setText(""); // clear answerTextView
        currentQ = quesList.get(qid); // the current question

        // assign values to guess buttons
        mathsQuestionTextView.setText(currentQ.getQUESTION());
        button1.setText(currentQ.getANSA());
        button2.setText(currentQ.getANSB());
        button3.setText(currentQ.getANSC());
        button4.setText(currentQ.getANSD());
        button5.setText("Cheat");
        button6.setText("Skip");
        enableButtons();

    }

    // after the user hits a button during the quiz the next question is loaded
    private void loadNextQuestion() {

        // this boolean is true if all questions have already been answered by the user
        // if true then the quizEnds() method is called to end the quiz and report the score
        boolean allquestionsanswered = questionTracker[1] == 1 &&
                questionTracker[2] == 2 &&
                questionTracker[3] == 3 &&
                questionTracker[4] == 4 &&
                questionTracker[5] == 5 &&
                questionTracker[6] == 6 &&
                questionTracker[7] == 7 &&
                questionTracker[8] == 8 &&
                questionTracker[9] == 9 &&
                questionTracker[10] == 10 ;

        if (allquestionsanswered)
            quizEnds();
        else // if there are still unattempted/unanswered questions then the following code acts
            // so that all questions are checked before quiz ends even if the user for example
            // switches from q1 to q10, the - 10 allows that all questions are run through
            if (qid == 10) {
                qid = qid - 10;
                loadNextQuestion();
            }
            // if the questions has been answered/attempted then skip it and call the method again
            // using Recursion here
            else if (questionTracker[(qid+1)] == (qid + 1)) {
                qid++;
                loadNextQuestion();
            }
            // else just execute the normal code to load the next question
            else {
                questionNumberTextView.setText(getString(R.string.questions, (qid + 1), NUMBER_OF_QUESTIONS));

                answerTextView.setText(""); // clear answerTextView
                currentQ = quesList.get(qid); // the current question

                // assign values to guess buttons
                mathsQuestionTextView.setText(currentQ.getQUESTION());
                button1.setText(currentQ.getANSA());
                button2.setText(currentQ.getANSB());
                button3.setText(currentQ.getANSC());
                button4.setText(currentQ.getANSD());
                button5.setText("Cheat");
                button6.setText("Skip");
                enableButtons();

                Log.d(TAG, currentQ.getANSWER().toString());

        }

            /*

        // get an InputStream to the asset representing the next flag
        // and try to use the InputStream

        try (InputStream stream =
                     assets.open(region + "/" + nextImage + ".png")) {
            // load the asset as a Drawable and display on the flagImageView
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);

            animate(false); // animate the flag onto the screen
        }
        catch (IOException exception) {
            Log.e(TAG, "Error loading " + nextImage, exception);
        }
        */
    }

    // animates the entire quizLinearLayout off screen - used for when a question is attempted
    private void animate() {

        // calculate center x and center y
        int centerX = (quizLinearLayout.getLeft() +
                quizLinearLayout.getRight()) / 2; // calculate center x
        int centerY = (quizLinearLayout.getTop() +
                quizLinearLayout.getBottom()) / 2; // calculate center y

        // calculate animation radius
        int radius = Math.max(quizLinearLayout.getWidth(),
                quizLinearLayout.getHeight());

        Animator animator;

        // create circular reveal animation
        animator = ViewAnimationUtils.createCircularReveal(
                quizLinearLayout, centerX, centerY, radius, 0);
        animator.addListener(
                new AnimatorListenerAdapter() {
                    // called when the animation finishes
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.d(TAG, "finishes first question");
                    }
                }
        );


        animator.setDuration(500); // set animation duration to 500 ms
        animator.start(); // start the animation
    }

    // called when a guess Button is touched
    public OnClickListener guessButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            String answer = currentQ.getANSWER();

            //saving which questions have been answered
            questionTracker[qid+1]= qid+1;

            // increment number of guesses the user has made
            ++totalGuesses;

            if (guess.equals(answer)) { // if the guess is correct
                ++correctAnswers; // increment the number of correct answers
                // display correct answer in green text
                answerTextView.setText(getString(R.string.answerIsCorrect, currentQ.getANSWER()));
                answerTextView.setTextColor(
                        getResources().getColor(R.color.correct_answer,
                                getContext().getTheme()));

                disableButtons(); // disable all guess Buttons
                qid++;

            }
            //if use cheat button
            else if (guess.equals("Cheat")) {
                // display cheat message and answer in red
                answerTextView.setText(getString(R.string.cheat, answer));
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, getContext().getTheme()));
                qid++;
            }
            // if skip button is used
            else if (guess.equals("Skip")) {
                // display "Why skip..." in red
                answerTextView.setText(R.string.skip);
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, getContext().getTheme()));
                qid++;
            }
            else {
                // answer was incorrect
                disableButtons(); // disable all guess Buttons
                mathsQuestionTextView.startAnimation(shakeAnimation); // play shake

                // display "Incorrect!" in red
                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, getContext().getTheme()));
                qid++;
            }

            //using handler to delay the next action so user potentially has a quick second to
            // review his mistake
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            animate();
                        }
                    }, 1000); // 2000 milliseconds for 2-second delay

            // using two handlers to coordinate both actions to happen at the same time
            // animate takes 500ms so once it finished the quiz ends or next question is loaded
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // check if quiz ends ie all questions have been answered
                            if (totalGuesses == NUMBER_OF_QUESTIONS)
                                quizEnds();
                            else
                                loadNextQuestion();
                        }
                    }, 1500); // 2000 milliseconds for 2-second delay

        }
    };

    // method called when quiz ends - calls out a alertdialog that shows outcomes and allows for next
    // actions like playing again or going back to main menu or saving the score
    public void quizEnds() {

        AlertDialog.Builder display = new AlertDialog.Builder(getContext());
        display.setTitle(R.string.alertDialog);
        display.setMessage(getString(R.string.results,
                correctAnswers,
                ((100 * correctAnswers) / (double) NUMBER_OF_QUESTIONS)));

        // button for replay
        display.setNegativeButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        resetQuiz();
                    }
                }
        );

        // button for going back to the main menu
        display.setNeutralButton(R.string.backToMenu, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int id) {
                Intent f = new Intent(getActivity(), MainActivity.class);
                startActivity(f);
            }
        });

        // button for score submission and viewing the leaderboard
        display.setPositiveButton(R.string.submitScore, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        Player p = new Player(nameID, correctAnswers);
                        scoredb.addPlayer(p);
                        Intent intent = new Intent(getActivity(), Scoreboard.class);
                        startActivity(intent);
                    }
                }
        );
        // so that this alertdiagog will not dissapear unless a button selected
        display.setCancelable(false);

        //create alert dialog that was built
        AlertDialog scorePopup = display.create();
        scorePopup.show();

        // if the user did not enter a proper name like "" then he wont be allowed to submit a score
        Button button = scorePopup.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
        if (nameID.equals("")) {
            button.setEnabled(false);
        }
    }


    // utility method that disables all answer Buttons
    private void disableButtons() {
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        button5.setEnabled(false);
        button6.setEnabled(false);
    }

    // utility method that disables all answer Buttons
    private void enableButtons() {
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        button5.setEnabled(true);
        button6.setEnabled(true);
    }

    // method allowing us to pass the username from the activity down to this fragment
    public void getPlayerName(String username) {
        nameID = username;
        Log.d("getPlayerName", nameID);
    }

    // for passing the questiontracker information to the activity
    public int[] sendQuestionTracker() {
        return questionTracker;
    }

    // see above but for sending score to activity
    public int sendScore() {
        return correctAnswers;
    }

    // method we use so that activity can pass score to fragment
    public void receiveScore(int score) {
        correctAnswers = score;
    }

    // same as above but for receiving the questiontracker
    public void receiveQuestionTracker(int[] tracker) {
        questionTracker = tracker;
    }


}