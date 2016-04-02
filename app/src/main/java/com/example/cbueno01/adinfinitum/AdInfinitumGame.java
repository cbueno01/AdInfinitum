package com.example.cbueno01.adinfinitum;

import android.text.format.Time;
import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Timer;

/**
 * Created by Squash on 4/1/2016.
 */
public class AdInfinitumGame {
    // Characters used to represent the human, computer, and open spots
    private static final int MAX_ADS_ONSCREEN = 10;


    // Ads are created and populated into tis lost in game
    public ArrayList<Ad> activeAds;

    // might not need to create this
    public ArrayList<Ad> availableAds;

    private Random mRand;

    private GregorianCalendar time;

    private long mScore;

    // The game's difficulty levels
    public enum DifficultyLevel {
        Easy, Medium, AdOverload
    }

    ;

    // Current difficulty level
// get this from SETTINGS
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Easy;

    //CONSTRUCTOR:
    public AdInfinitumGame() {
        // Seed the random number generator
        mRand = new Random();
        activeAds = new ArrayList<Ad>();
        time = new GregorianCalendar();


    }//end Constructor

    public void addAd (Ad ad) {
        activeAds.add(ad);
    }

    //GETTERS AND SETTERS:
    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }//end getter

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        mDifficultyLevel = difficultyLevel;
    }//end setter

    public ArrayList<Ad> getActiveAds() {
        return activeAds;
    }

    // why would this be necessary to do?
    public void setActiveAds(ArrayList<Ad> ads) {
        activeAds = ads;
    }

    //OTHER METHODS:
    public void clearActiveAds() {
        activeAds.clear();
    }//end clearBoard method


    public int checkForGameOver() {

        if (activeAds.size() > MAX_ADS_ONSCREEN) {
            return 1;
        }

        return 0;
    }//end checkForWinner method


}//end Class.