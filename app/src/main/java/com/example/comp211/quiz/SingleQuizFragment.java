package com.example.comp211.quiz;

import java.security.SecureRandom;
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
    //int score = 0;
    int qid = 0, databaseVersion, jumpTo;
    private Question currentQ;

    // for score submission
    ScoreboardData scoredb;
    String nameID;

    private List<String> fileNameList; // flag file names

    //private Set<String> regionsSet; // world regions in current quiz
    private List<String> quizQuestionList;
    private String correctAnswer; // correct answer
    private int totalGuesses; // number of guesses made
    private int correctAnswers; // number of correct guesses
    private int totalSkipped; // number of skipped questions
    private int totalCheat; // number of times cheating and showing the answer
    //private int guessRows; // number of rows displaying guess Buttons
    private SecureRandom random; // used to randomize the quiz
    private Handler handler; // used to delay loading next question
    private Animation shakeAnimation; // animation for incorrect guess

    private LinearLayout quizLinearLayout; // layout that contains the quiz
    private TextView questionNumberTextView; // shows current question #
    private TextView mathsQuestionTextView; // displays a maths question
    private LinearLayout[] guessLinearLayouts; // rows of answer Buttons
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

        /*
        fileNameList = new ArrayList<>();
        random = new SecureRandom();
        */

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

        //solveQuestionView = (ImageView) view.findViewById(R.id.solveQuestionView);
        //guessLinearLayouts = new LinearLayout[2];
        //guessLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        //guessLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.row2LinearLayout);
        //answerTextView = (TextView) view.findViewById(R.id.answerTextView);
        button1.setOnClickListener(guessButtonListener);
        button2.setOnClickListener(guessButtonListener);
        button3.setOnClickListener(guessButtonListener);
        button4.setOnClickListener(guessButtonListener);
        button5.setOnClickListener(guessButtonListener);
        button6.setOnClickListener(guessButtonListener);
        /*
        // configure listeners for the guess Buttons
        for (LinearLayout row : guessLinearLayouts) {
            for (int column = 0; column < row.getChildCount(); column++) {
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(guessButtonListener);
            }
        }
        */

        return view; // return the fragment's view for display
    }

    public void resetQuiz() {
        correctAnswers = 0;
        totalGuesses = 0;
        questionTracker = new int[11];
        //databaseVersion++;
        //totalCheat = 0;
        //totalSkipped = 0;
        qid = 0;
        db = new QuestionCreate(getActivity());  // my question bank class
        db.resetQuestions();
        quesList = db.getAllQuestions();  // this will fetch all questions
        Log.d(TAG, quesList.get(0).getANSWER());
        /* add random questions into questionsList until we get NUMBER_OF_QUESTIONS (10)
        for (int questionCounter = 1; questionCounter <= NUMBER_OF_QUESTIONS; questionCounter++) {
            // code to create a question in String format
            // String question = formula and make into string format
            // append question into list
            quizQuestionList.add(question);
        }
        */
        scoredb = new ScoreboardData(getActivity(),1);

        loadNextQuestion();
    }

    public void jumpToQuestion(int i) {

        qid = i - 1;
        db = new QuestionCreate(getActivity());
        scoredb = new ScoreboardData(getActivity(),1);
        quesList = db.getAllQuestions();  // this will fetch all questions

        //if (questiontracker[i] == i)
        //    loadNextQuestion();
        //else {
            qid = i - 1;
            db = new QuestionCreate(getActivity());
            scoredb = new ScoreboardData(getActivity(), 1);
            quesList = db.getAllQuestions();  // this will fetch all questions
            Log.d(TAG, quesList.get(0).getANSWER());


            questionNumberTextView.setText(getString(R.string.questions, (qid + 1), NUMBER_OF_QUESTIONS));

            answerTextView.setText(""); // clear answerTextView
            currentQ = quesList.get(qid); // the current question

            // assign values to guess buttons
            //for (int row = 0; row < guessRows; row++) {
            mathsQuestionTextView.setText(currentQ.getQUESTION());
            button1.setText(currentQ.getANSA());
            button2.setText(currentQ.getANSB());
            button3.setText(currentQ.getANSC());
            button4.setText(currentQ.getANSD());
            button5.setText("Cheat");
            button6.setText("Skip");
            enableButtons();
        //}
    }
    // after the user guesses a correct flag, load the next flag
    private void loadNextQuestion() {
        // set questionNumberTextView's text
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
        else
            if (qid == 10) {
                qid = qid - 10;
                loadNextQuestion();
            }
            else if (questionTracker[(qid+1)] == (qid + 1)) {
                qid++;
                loadNextQuestion();
            }
            else {

                questionNumberTextView.setText(getString(R.string.questions, (qid + 1), NUMBER_OF_QUESTIONS));

                answerTextView.setText(""); // clear answerTextView
                currentQ = quesList.get(qid); // the current question

                // assign values to guess buttons
                //for (int row = 0; row < guessRows; row++) {
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

            /* restore state */
            /*
        // get file name of the next flag and remove it from the list
        String nextQuestion = quizQuestionList.remove(0);
        correctAnswer = nextQuestion; // update the correct answer
        answerTextView.setText(""); // clear answerTextView

        // display current question number
        questionNumberTextView.setText(getString(
                R.string.questions, (correctAnswers + 1), NUMBER_OF_QUESTIONS));

        // extract the region from the next image's name
        //String region = nextImage.substring(0, nextImage.indexOf('-'));

        // use AssetManager to load next image from assets folder
        //AssetManager assets = getActivity().getAssets();

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

        Collections.shuffle(fileNameList); // shuffle file names

        // put the correct answer at the end of fileNameList
        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));

        // add 2, 4, 6 or 8 guess Buttons based on the value of guessRows
        //for (int row = 0; row < guessRows; row++) {
        for (int row = 0; row < 2; row++) {
                // place Buttons in currentTableRow
                for (int column = 0;
                     column < guessLinearLayouts[row].getChildCount();
                     column++) {
                    // get reference to Button to configure
                    Button newGuessButton = (Button) guessLinearLayouts[row].getChildAt(column);
                    newGuessButton.setEnabled(true);

                    // get country name and set it as newGuessButton's text
                    String filename = fileNameList.get((row * 2) + column);
                    // get sample answers and display them
                    newGuessButton.setText(getCountryName(filename));
                }
            }

        // randomly replace one Button with the correct answer
        //int row = random.nextInt(guessRows); // pick random row
        int row = random.nextInt(2); // pick random row
        int column = random.nextInt(2); // pick random column
        LinearLayout randomRow = guessLinearLayouts[row]; // get the row
        String countryName = getCountryName(correctAnswer);
        ((Button) randomRow.getChildAt(column)).setText(countryName);#
        */
    }

    /*
    // parses the country flag file name and returns the country name
    private String getCountryName(String name) {
        return name.substring(name.indexOf('-') + 1).replace('_', ' ');
    }
    */

    // animates the entire quizLinearLayout on or off screen
    private void animate(boolean animateOut) {

        // calculate center x and center y
        int centerX = (quizLinearLayout.getLeft() +
                quizLinearLayout.getRight()) / 2; // calculate center x
        int centerY = (quizLinearLayout.getTop() +
                quizLinearLayout.getBottom()) / 2; // calculate center y

        // calculate animation radius
        int radius = Math.max(quizLinearLayout.getWidth(),
                quizLinearLayout.getHeight());

        Animator animator;

        // if the quizLinearLayout should animate out rather than in
        if (animateOut) {
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
        } else { // if the quizLinearLayout should animate in
            animator = ViewAnimationUtils.createCircularReveal(
                    quizLinearLayout, centerX, centerY, 0, radius);
        }

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
                // answer is correct but quiz is not over
                // load the next question after a 2-second delay
                /*handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                animate(true); // animate the flag off the screen
                            }
                        }, 2000); // 2000 milliseconds for 2-second delay
                */
                qid++;

            }
            //if use cheat button and if use skip button needs to be included here
            else if (guess.equals("Cheat")) {
                // display cheat message and answer in red
                answerTextView.setText(getString(R.string.cheat, answer));
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, getContext().getTheme()));
                qid++;
            }

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
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            animate(true);
                        }
                    }, 1000); // 2000 milliseconds for 2-second delay

            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (totalGuesses == NUMBER_OF_QUESTIONS)    // check if quiz ends ie all questions have been answered
                                quizEnds();
                            else
                                loadNextQuestion();
                        }
                    }, 1500); // 2000 milliseconds for 2-second delay

        }
    };

    public void quizEnds() {

        AlertDialog.Builder display = new AlertDialog.Builder(getContext());
        display.setTitle(R.string.alertDialog);
        display.setMessage(getString(R.string.results,
                correctAnswers,
                ((100 * correctAnswers) / (double) NUMBER_OF_QUESTIONS)));

        display.setNegativeButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        resetQuiz();
                    }
                }
        );

        display.setNeutralButton(R.string.backToMenu, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int id) {
                Intent f = new Intent(getActivity(), MainActivity.class);
                startActivity(f);
            }
        });

        // add score submission
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

        display.setCancelable(false);

        //create alert dialog that was built
        AlertDialog scorePopup = display.create();
        scorePopup.show();
        Button button = scorePopup.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
        if (nameID.equals("")) {
            button.setEnabled(false);
        }
        // use FragmentManager to display the DialogFragment
        //quizResults.setCancelable(false);
        //quizResults.show(getFragmentManager(), "quiz results");
    }


    // utility method that disables all answer Buttons
    private void disableButtons() {
        /*
        //for (int row = 0; row < guessRows; row++) {
        for (int row = 0; row < 2; row++) {
            LinearLayout guessRow = guessLinearLayouts[row];
            for (int i = 0; i < guessRow.getChildCount(); i++)
                guessRow.getChildAt(i).setEnabled(false);
        }
        */
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


    public void getPlayerName(String username) {
        nameID = username;
        Log.d("getPlayerName", nameID);
    }

    public int[] sendQuestionTracker() {
        return questionTracker;
    }

    public int sendScore() {
        return correctAnswers;
    }

    public void receiveScore(int score) {
        correctAnswers = score;
    }

    public void receiveQuestionTracker(int[] tracker) {
        questionTracker = tracker;
    }

/*
    public static class MyDialogFragment extends DialogFragment {

        public static MyDialogFragment newInstance(int title) {
            MyDialogFragment fm = new MyDialogFragment();
            Bundle arguments = new Bundle();
            arguments.putInt("title", title);
            fm.setArguments(arguments);
            return fm;
        }

        //DialogFragment quizResults = new DialogFragment() {
            // create an AlertDialog and return it
        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            int title = getArguments().getInt("title");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // set a title
            builder.setTitle(title);

            // add an Icon
            //builder.setIcon();

            // set message
            builder.setMessage(getString(R.string.results,
                    totalGuesses,
                    (1000 / (double) totalGuesses)));

            // "Reset Quiz" Button
            builder.setPositiveButton(R.string.reset_quiz,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            getParentFragment().resetQuiz();
                        }
                    }
            );

            return builder.create(); // return the AlertDialog
        }
    };
    }
*/

}
