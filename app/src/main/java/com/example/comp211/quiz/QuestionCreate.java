package com.example.comp211.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by delon on 28.12.2016.
 *
 * Class that manages the database containing the table with all questions.
 * Since this is a Maths/Arithmetic quiz, all questions will be simply addition problems however
 * the questions will all be randomly generated.
 */

public class QuestionCreate extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "simpleMaths";
    private static final int DATABASE_VERSION = 1;

    // --------------------- Table for Questions ---------------------------------------
    // tasks table name
    private static final String TABLE_QUEST = "aListOfQuestions";
    // tasks Table Columns names
    private static final String KEY_ID = "qid";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER = "answer"; // correct Answer
    private static final String KEY_ANSA = "ansa"; // Answer a
    private static final String KEY_ANSB = "ansb"; // Answer b
    private static final String KEY_ANSC = "ansc"; // Answer c
    private static final String KEY_ANSD = "ansd"; // Answer d


    // define database
    private SQLiteDatabase dbase;

    // Constructor
    public QuestionCreate(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // this is empty so that nothing happens when the database is created/ called
        if (TableExists(db, TABLE_QUEST))
            // checking if the table existed
            Log.d("Arith Question Database", "table exists");
    }

    // We will call this method to get a new table with new randomly generated questions
    public void resetQuestions() {
        // get write access
        dbase = this.getWritableDatabase();

        // simply log to check status of table
        if (TableExists(dbase, TABLE_QUEST))
            Log.d("resetQuestions method", "table exists");

        // delete existing table with questions if it exists
        dbase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // create new question table
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUEST + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_QUESTION + " TEXT , "
                + KEY_ANSWER + " TEXT , "
                + KEY_ANSA + " TEXT , "
                + KEY_ANSB + " TEXT , "
                + KEY_ANSC + " TEXT , "
                + KEY_ANSD + " TEXT )";
        dbase.execSQL(CREATE_QUIZ_TABLE);
        // use addQuestions() to populate table with new questions that are randomly generated
        addQuestions(dbase);
    }

    // generating all questions and saving into dbase
    private void addQuestions(SQLiteDatabase db) {
        int NUMBER_OF_QUESTIONS = 10;
        // the code below generates 10 addition problems randomly
        for (int questNumber = 0; questNumber < NUMBER_OF_QUESTIONS; questNumber++){
            int randomNumber_1 = ThreadLocalRandom.current().nextInt(10, 100);
            int randomNumber_2 = ThreadLocalRandom.current().nextInt(10, 100);
            int sum = randomNumber_1 + randomNumber_2;
            // this line below makes sure that we assign the correct answer to a differently
            // positioned button each time randomly
            int fix = ThreadLocalRandom.current().nextInt(sum-2, sum+2);
            int answerA = fix - 1;
            int answerB = fix;
            int answerC = fix + 1;
            int answerD = fix + 2;
            // use Question class to create a Question object with all the data for the question
            Question q1 = new Question(randomNumber_1 + " + " + randomNumber_2 + " = ?",
                    Integer.toString(sum), Integer.toString(answerA), Integer.toString(answerB),
                    Integer.toString(answerC), Integer.toString(answerD));

            // using this object and the methods from Question.class we insert these values
            // into the table
            // * the step of including a Question class may seem needless, however it is useful
            // for other functions to have a Question class that allows access and an easy way to
            // read and get questions* hence there is harm using it here as well for form
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION, q1.getQUESTION());
            values.put(KEY_ANSWER, q1.getANSWER());
            values.put(KEY_ANSA, q1.getANSA());
            values.put(KEY_ANSB, q1.getANSB());
            values.put(KEY_ANSC, q1.getANSC());
            values.put(KEY_ANSD, q1.getANSD());
            // Inserting Row
            db.insert(TABLE_QUEST, null, values);
        }
    }

    // If database version upgrades, the table is reset. Used this method before writing the
    // the resetQuestions() method in order to delete old questions upon a launch of a new quiz
    // however the resetQuestions() approach is much simpler and involves one less parameter:
    // the version number
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // Create tables again
        onCreate(db);
    }

    // same case as for onUpgrade()
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        Log.d("Database", "Table removed");
        onCreate(db);
    }

    //this method is used in both single quiz fragment and multiplayer to load the questions
    // from the database table into a usable list<Question> so that those classes can read the
    // questions
    public List<Question> getAllQuestions() {

        List<Question> questionList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;

        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding them to list of questions
        if (cursor.moveToFirst()) {
            do {
                Question frage = new Question();
                frage.setID(cursor.getInt(0));
                frage.setQUESTION(cursor.getString(1));
                frage.setANSWER(cursor.getString(2));
                frage.setANSA(cursor.getString(3));
                frage.setANSB(cursor.getString(4));
                frage.setANSC(cursor.getString(5));
                frage.setANSD(cursor.getString(6));

                questionList.add(frage);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("Database", "getQuestions called");
        // return question list
        return questionList;
    }

    //  check whether the table exists based on the table name
    public boolean TableExists(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
