package com.hackernight.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighScore(int score){
        int currentScore = score;
        int lastScore = preferences.getInt("high_Score",0);
        if (currentScore>lastScore){
            //we have highest and save the score
            preferences.edit().putInt("high_Score",currentScore).apply();
        }
    }

    public int getHighScore(){
        return preferences.getInt("high_Score",0);
    }

    public void setState(int index){
        preferences.edit().putInt("index_state",index).apply();
    }

    public int getState(){
        return preferences.getInt("index_State",0);
    }

}
