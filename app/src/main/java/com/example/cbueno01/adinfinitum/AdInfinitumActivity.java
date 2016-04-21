package com.example.cbueno01.adinfinitum;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Squash on 4/1/2016.
 */
public class AdInfinitumActivity extends Activity {

    private static final String TAG = "AdInfinitumActivity";
    private static final double mBase = .27;
    private static final double mConstant = 10000;
    private static final int DIALOG_REPLAY_ID = 1;

    // Preference variables
    private int mDifficulty;
    private String mPlayerName;
    private boolean mSoundEffectsOn;

    // Variable to see if it was cancelled during countdown
    private boolean mTimerFinish;
    // for preferences
    //static final int DIALOG_DIFFICULTY_ID = 0;
    //static final int DIALOG_QUIT_ID = 1;
    //static final int DIALOG_ABOUT_ID = 2;
    //static final int DIALOG_CLEAR_SCORES = 3;

    private int screenWidth;
    private int screenHeight;

    // time references in milliseconds
    private long mStartTime;
    private long mElapsedTime;
    private long mAdTime;
    //    private long mTimeDifference;
    private long mTimeOfLastAd;

    // for pausing game
    private Handler mPauseHandler;
    private Runnable myRunnable;

    // Keep track of score
    private long mScore;

    // game logic
    private AdInfinitumGame mGame;

    // game loop
    private GameLoop mGameLoop;

    // random Gen
    private Random rand;

    // Various text displayed
    private GameView mGameView;
    private TextView mTimeTextView;
    private TextView mScoreTextView;
    private TextView mCountdownTextView;

    // for all the sounds  we play
    private SoundPool mSounds;
    private HashMap<Integer, Integer> mSoundIDMap;
    private int dismissAdID1;
    private int dismissAdID2;
    private int gameOverID;

    // to restore scores
    private SharedPreferences mPrefs;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_screen);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        mTimeTextView = (TextView) findViewById(R.id.time_elapsed);
        mScoreTextView = (TextView) findViewById(R.id.player_score);
        mCountdownTextView = (TextView) findViewById(R.id.countdown);
        mGameView = (GameView) findViewById(R.id.game);

        mGameView.setOnTouchListener(mTouchListener);

        rand = new Random();
        mAdTime = 1000;
        mPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        startGame();


//        mPauseHandler = new Handler();

//        Log.d(TAG, "value of savedInstanceState: " + savedInstanceState);
//        if(savedInstanceState == null)
//            startFromScratch();
//        else
//            restoreGame(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        // Setting game preferences
        mPlayerName = mPrefs.getString("pref_reset_profile", "Sheeple");
        mSoundEffectsOn = mPrefs.getBoolean("pref_soundfx", true);
        String difficultyLevel = mPrefs.getString("pref_difficulty_level", getResources().getString(R.string.difficulty_level_easy));
        String[] levels = getResources().getStringArray(R.array.difficulty_level);

        int i = 0;
        while(i < levels.length) {
            if(difficultyLevel.equals(levels[i])) {
                mDifficulty = i + 1;
                i = levels.length; // to stop loop
            }
            i++;
        }

        mSounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        // 2 = maximum sounds ot play at the same time,
        // AudioManager.STREAM_MUSIC is the stream type typically used for games
        // 0 is the "the sample-rate converter quality. Currently has no effect. Use 0 for the default."
        dismissAdID1 = mSounds.load(this, R.raw.synth1, 1);
        // Context, id of resource, priority (currently no effect)
        dismissAdID2 = mSounds.load(this, R.raw.synth2, 1);
        gameOverID = mSounds.load(this, R.raw.wicked_laugh, 1);
        startGame();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSounds != null) {
            mSounds.release();
            mSounds = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGameLoop != null)
            mGameLoop.cancel(true);
        else
            mTimerFinish = false;
    }

    private class GameLoop extends AsyncTask<Integer, Void, Void> {

        public GameLoop() {
        }

        @Override
        protected Void doInBackground(Integer... args) {
            while (!mGame.isGameOver()) {
                if (mGameLoop.isCancelled() || !mTimerFinish)
                    return null;

                mElapsedTime = (System.nanoTime() / 1000000) - mStartTime;
                int fps = args[0];

                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException ie) {
                }

                updateGame();
                publishProgress();
            }

            return null;
        }

        protected void onProgressUpdate(Void... Progress) {
            mTimeTextView.setText(String.format("%04d", (int) (mElapsedTime / 1000)));
//            mScoreTextView.setText(String.format("%07d", mScore));
            mScoreTextView.setText("" + mScore);
            mGameView.invalidate();
        }

        protected void onPostExecute(Void result) {
            SharedPreferences.Editor ed = mPrefs.edit();
            long highscore = mPrefs.getLong("pref_high_score", 0);
            if (highscore < mScore) {
                ed.putLong("pref_high_score", mScore);
                ed.apply();
            }

            if (!mGameLoop.isCancelled() && mTimerFinish) {
                showDialog(DIALOG_REPLAY_ID);
                if (mSoundEffectsOn) {
                    mSounds.play(gameOverID, 1, 1, 1, 0, 1);
                }
            }
        }
    }

    public void updateGame() {
        // AD GENERATION ALGORITHM
        if (mAdTime - (mElapsedTime / (300 / mDifficulty)) < (System.nanoTime() / 1000000) - mTimeOfLastAd) {
            ArrayList<Integer> imageID = getImageIDs();
            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inScaled = false;

            int index = rand.nextInt(imageID.size());
            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), imageID.get(index), dimensions);

            while (true) {
                float scalingFactor = rand.nextFloat() + rand.nextInt(1) + (float) 0.6;
                int width = (int) (mBitmap.getWidth() * scalingFactor);
                int height = (int) (mBitmap.getHeight() * scalingFactor);
                int x = rand.nextInt(Math.abs(screenWidth - width) + 1);
                int y = rand.nextInt(Math.abs(screenHeight - height) + 1);
                if (x + width < screenWidth && y + height < screenHeight) {
                    Point boxLeftTop = new Point((rand.nextInt(Math.abs(width - 50)) + x), (rand.nextInt(Math.abs(height - 50)) + y));
//                    Log.d(TAG, "X; " + (width + x - boxLeftTop.x) + "  Y: " + (height + y - boxLeftTop.y));
                    Point boxBottomRight = new Point((rand.nextInt(Math.abs(width + x - boxLeftTop.x - 50)) + boxLeftTop.x + 50), (rand.nextInt(Math.abs(height + y - boxLeftTop.y - 50)) + boxLeftTop.y + 50));

                    // AD POINTAGE ALGORITHM
                    long points = (long) (mBase * mDifficulty * (mConstant - (4 * (width + height) * scalingFactor)));
                    Ad ad = new Ad(imageID.get(index), mBitmap, width, height, new Point(x, y), boxLeftTop, boxBottomRight, points);
//                    Log.d("Ad Infinitum", "points: " + ad.getPointage());
                    mGame.addAd(ad);
                    break;
                }
            }
            mTimeOfLastAd = System.nanoTime() / 1000000;
        }
    }

    private ArrayList<Integer> getImageIDs() {
        String[] adNames = getResources().getStringArray(R.array.advertisements);
        ArrayList<Integer> imageIDs = new ArrayList<>();
        // Strings for spinner are upper case with spaces.
        // Corresponding drawable is all lower case with _ for spaces.
        for (String name : adNames) {
            name = name.toLowerCase();
            name = name.replace(" ", "_");
            imageIDs.add(getResources().getIdentifier(name, "drawable", getPackageName()));
        }

        return imageIDs;
    }

    // Listen for touches on the game
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (!mGame.isGameOver()) {
                Log.d("Ad Infinitum", "Score: " + mScore);

                ArrayList<Ad> activeAds = mGame.getActiveAds();
                int arraySize = activeAds.size();

                // Determine which cell was touched
                int x = (int) event.getX();
                int y = (int) event.getY();

                for (int i = arraySize - 1; i >= 0; i--) {
                    Ad currentAd = activeAds.get(i);

                    if (currentAd.isPointInAd(x, y)) {
                        activeAds.remove(i);
                        if (mSoundEffectsOn) {
                            mSounds.play(dismissAdID2, 1, 1, 1, 0, 1);
                        }
                        mScore += currentAd.getPointage();
                        break;
                    }
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage( getResources().getString(R.string.give_up) + " " + mPlayerName).setCancelable(false)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AdInfinitumActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startGame();
                    }
                });

        return builder.create();
    }

    public void startGame() {

        mGame = new AdInfinitumGame();
        mGameView.setGame(mGame);
        mCountdownTextView.setEnabled(false);
        mScore = 0;
        mElapsedTime = 0;
        mTimeOfLastAd = 0;
        mTimerFinish = true;
        mGameLoop = null;
        mTimeTextView.setText(R.string.time_elapsed);
        mScoreTextView.setText(R.string.default_score);
        mGameView.invalidate();

        mCountdownTextView.setVisibility(View.VISIBLE);
        new CountDownTimer(3200, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountdownTextView.setText(" " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mCountdownTextView.setVisibility(View.GONE);
                mGameView.setOnTouchListener(mTouchListener);
                mStartTime = System.nanoTime() / 1000000;
                mGameLoop = new GameLoop();
                mGameLoop.execute(30);
            }
        }.start();

    }
}