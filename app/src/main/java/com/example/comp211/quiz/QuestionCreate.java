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
        // Drop older table if existed
        if (TableExists(db, TABLE_QUEST))
            Log.d("Arith Question Database", "table exists");
    }

    // We will call this method to get a new table with new randomly generated questions
    public void resetQuestions() {
        dbase = this.getWritableDatabase();
        if (TableExists(dbase, TABLE_QUEST))
            Log.d("resetQuestions method", "table exists");
        // delete existing table with questions if it exists
        dbase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // create question table
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUEST + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_QUESTION + " TEXT , "
                + KEY_ANSWER + " TEXT , "
                + KEY_ANSA + " TEXT , "
                + KEY_ANSB + " TEXT , "
                + KEY_ANSC + " TEXT , "
                + KEY_ANSD + " TEXT )";
        dbase.execSQL(CREATE_QUIZ_TABLE);
        // dbase.close();
        addQuestions(dbase);
    }

    // generating all questions and saving into dbase
    private void addQuestions(SQLiteDatabase db) {
        int NUMBER_OF_QUESTIONS = 10;
        for (int questNumber = 0; questNumber < NUMBER_OF_QUESTIONS; questNumber++){
            int randomNumber_1 = ThreadLocalRandom.current().nextInt(10, 100);
            int randomNumber_2 = ThreadLocalRandom.current().nextInt(10, 100);
            int sum = randomNumber_1 + randomNumber_2;
            int fix = ThreadLocalRandom.current().nextInt(sum-2, sum+2);
            int answerA = fix - 1;
            int answerB = fix;
            int answerC = fix + 1;
            int answerD = fix + 2;
            Question q1 = new Question(randomNumber_1 + " + " + randomNumber_2 + " = ?",
                    Integer.toString(sum), Integer.toString(answerA), Integer.toString(answerB),
                    Integer.toString(answerC), Integer.toString(answerD));
            //this.addQuestion(q1);
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // Create tables again
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        Log.d("Database", "Table removed");
        onCreate(db);
    }


    public List<Question> getAllQuestions() {

        List<Question> questionList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;

        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
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
