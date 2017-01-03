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
    // tasks table name
    private static final String TABLE_SCORE = "score";
    // tasks TAble columns
    private static final String USER_ID = "_id";
    private static final String USER_NAME = "name";
    private static final String USER_SCORE = "score";

    // define database
    private SQLiteDatabase dbase;

    // Constructor
    public ScoreboardData(Context context, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create scoreboard table
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORE + " ( "
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME + " TEXT, "
                + USER_SCORE + " INTEGER);";
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        // Create tables again
        onCreate(db);
    }


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

    public int numRows()
    {  int r;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SCORE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        r = c.getCount();
        return r;
    }
}
