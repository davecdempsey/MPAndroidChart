package com.xxmassdeveloper.mpchartexample.realm;


import io.realm.RealmObject;

/**
 * our data object
 */
public class Score extends RealmObject {

    private double totalScore;

    private double scoreNr;

    private String playerName;

    public Score() {
    }

    public Score(double totalScore, double scoreNr, String playerName) {
        this.scoreNr = scoreNr;
        this.playerName = playerName;
        this.totalScore = totalScore;
    }

    // all getters and setters...

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getScoreNr() {
        return scoreNr;
    }

    public void setScoreNr(double scoreNr) {
        this.scoreNr = scoreNr;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}