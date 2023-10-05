package edu.hitsz.rankingList;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Rankinglist {
    private String username;
    private int score;
    private String time;
    private String degree;


    public Rankinglist(String username, int score, String time, String degree) {
        this.username = username;
        this.score = score;
        this.time = time;
        this.degree = degree;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setDegree(String degree) { this.degree = degree; }


    public String getUsername() { return username; }
    public int getScore() { return score; }
    public String getTime() { return time; }
    public String getDegree() { return degree; }


}
