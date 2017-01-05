package com.example.comp211.quiz;

/**
 * Created by delon on 31.12.2016.
 */

public class Player {

    String name;
    int score;

    //Constructor taking both the player name and score, this will be used to add players and their
    // records into the leaderboard
    Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    //Constructor
    Player() {}

    // methods to set the Name or score
    public void setName(String name) {this.name = name;}
    public void setScore(int score) {this.score = score;}

    //methods for getting the name or score of a player
    public String getName() {return this.name;}
    public int getScore() {return this.score;}
}
