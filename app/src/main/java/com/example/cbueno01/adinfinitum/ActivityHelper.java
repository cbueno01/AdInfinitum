package com.example.cbueno01.adinfinitum;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

/**
 * Created by joyal on 2016-05-03.
 */
public class ActivityHelper {

    public static void initialize(Activity activity) {
        //Do all sorts of common task for your activities here including:

        SharedPreferences mPrefs = activity.getSharedPreferences("preferences", 0); //0 for MODE_PRIVATE
        boolean IsLandscape = mPrefs.getBoolean("pref_orientation",
                activity.getResources().getBoolean(R.bool.default_orientation));

        if(IsLandscape) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }
}
