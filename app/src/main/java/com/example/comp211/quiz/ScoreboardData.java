package com.example.comp211.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

/**
 * Created by delon on 31.12.2016.
 */

public class ScoreboardData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ScoreBoard";
    // --------------------- Table for Leaderboard ---------------------------------------
    // table name
    private static final String TABLE_SCORE = "score";
    // Table columns
    private static final String USER_ID = "_id";
    private static final String USER_NAME = "name";
    private static final String USER_SCORE = "score";

    // Constructor
    public ScoreboardData(Context context, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // in the activity life-cycle this is called upon creation of the activity as the name suggests
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create scoreboard table
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORE + " ( "
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME + " TEXT, "
                + USER_SCORE + " INTEGER);";
        db.execSQL(CREATE_SCORE_TABLE);
    }

    // if the database version changes. previously i used to manaually change the database version
    // each time a new quiz was started to reset questions however, this was inefficient and I
    // created a new method for resetting questions (by deleting the table) instead
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        // Create tables again
        onCreate(db);
    }

    // adding players into the leaderboard
    public int addPlayer(Player player)
    {
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, player.getName());
        cv.put(USER_SCORE, player.getScore());
        SQLiteDatabase db = getWritableDatabase();
        long studentID = db.insert(TABLE_SCORE, null, cv);
        db.close();
        return (int) studentID;
    }

    // get the player data from the table row i, given that the table has been ordered by score
    // before. This is used when creating the leaderboard as we order the table in here and use the
    // method in the Scoreboard.class to read the player data into a new array list and then display
    // that array list (shown in the Scoreboard.class code)
    public Player getRowProductData(int i) {
        Player newPlayer = new Player();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SCORE + " ORDER BY " + USER_SCORE + " DESC;";
        Cursor c = db.rawQuery(query, null);
        c.moveToPosition(i);
        newPlayer.setName(c.getString(c.getColumnIndex(USER_NAME)));
        newPlayer.setScore(Integer.parseInt(c.getString(c.getColumnIndex(USER_SCORE))));
        return newPlayer;
    }

    // get the total number of rows
    public int numRows()
    {  int r;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SCORE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        r = c.getCount();
        return r;
    }
}
