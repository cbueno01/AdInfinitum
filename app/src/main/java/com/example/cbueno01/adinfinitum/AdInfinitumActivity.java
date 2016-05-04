package com.example.cbueno01.adinfinitum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
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
import android.os.IBinder;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Squash on 4/1/2016.
 */
public class AdInfinitumActivity extends Activity {

    private static final String TAG = "AdInfinitumActivity";
    private static final double mBase = .27;
    private static final double mConstant = 10000;
    private static final int DIALOG_REPLAY_ID = 1;
    private static final int DIALOG_CONTINUE_ID = 2;

    private static final int numAdsAllowed = 3;

    // Preference variables
    private int mDifficulty;
    private String mPlayerName;
    private boolean mSoundEffectsOn;

    private long mTotalTimePlayed;
    private int mMostRoundsBeaten;
    private int mLongestGame;

    // Variable to see if it was cancelled during countdown
    private boolean mTimerFinish;

    private int screenWidth;
    private int screenHeight;

    // time references in milliseconds
    private long mStartTime;
    private long mElapsedTime;
    private long mAdTime;
    //    private long mTimeDifference;
    private long mTimeOfLastAd;

    private int mCurrentInterval;

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
    private boolean mIsBound = false;
    private MusicService mMusicService;
    private boolean mIsSoundOn;

    private String [] stringScores;
    private long [] highScores;

    // Game mode
    private String mGameMode;

    // Time amount of round if round mode
    private int mTimeOfRound;
    private int mRoundNumber;
    private boolean mLostRound;

    private Map<Long, String> mPlayerScores;
    // to restore scores
    private SharedPreferences mPrefs;
    private SharedPreferences mProfs;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_screen);
        ActivityHelper.initialize(this);

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
        mAdTime = 800;
        mRoundNumber = 0;
        mPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        doBindService();
        mProfs = getSharedPreferences("profile", MODE_PRIVATE);

        mTotalTimePlayed = Long.parseLong(mProfs.getString("pref_total_time", "0"));
        mLongestGame = Integer.parseInt(mProfs.getString("pref_longest_game", "0"));
        mMostRoundsBeaten = Integer.parseInt(mProfs.getString("pref_most_rounds", "0"));

        String temp = mProfs.getString("pref_high_scores", getString(R.string.default_high_scores));
        stringScores = temp.split(",");
        mPlayerScores = new HashMap<>();

        long[] tempArray = new long[stringScores.length];
        for (int i = stringScores.length - 1; i >= 0; --i) {
            String [] cur = stringScores[i].split("\t");
            tempArray[i] = Long.parseLong(cur[cur.length - 1]);
            mPlayerScores.put(tempArray[i], cur[0]);
        }
        highScores = tempArray;
//        startGame();


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
        mPlayerName = mProfs.getString("pref_profile_name", "<Player>");
        mSoundEffectsOn = mPrefs.getBoolean("pref_soundfx", true);
        mIsSoundOn = mPrefs.getBoolean("pref_soundtrack_sound", true);
        mGameMode = mPrefs.getString("pref_modes", getResources().getString(R.string.mode_continuous));
        String difficultyLevel = mPrefs.getString("pref_difficulty_level", getResources().getString(R.string.difficulty_level_easy));
        String[] levels = getResources().getStringArray(R.array.difficulty_level);

        if (mIsBound && mIsSoundOn)
            mMusicService.resumeMusic();

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
        if (mGameMode.equals(getResources().getString(R.string.mode_continuous)))
            startGame(0, 0);
        else
            startGame(0, 15000);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSounds != null) {
            mSounds.release();
            mSounds = null;
        }

        if(mIsBound && mIsSoundOn)
            mMusicService.pauseMusic();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGameLoop != null)
            mGameLoop.cancel(true);
        else
            mTimerFinish = false;
    }

    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private abstract class GameLoop extends AsyncTask<Integer, Void, Void> {}

    private class ContinuousGameLoop extends GameLoop {

        public ContinuousGameLoop() {}

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

                updateContinuousGame();
                publishProgress();
            }

            return null;
        }

        protected void onProgressUpdate(Void... Progress) {
            mTimeTextView.setText(String.format("Time Elapsed: %04d", (int) (mElapsedTime / 1000)));
//            mScoreTextView.setText(String.format("%07d", mScore));
            mScoreTextView.setText("Score: "  + mScore);
            mGameView.invalidate();
        }

        protected void onPostExecute(Void result) {
            SharedPreferences.Editor ed = mProfs.edit();
//            long highscore = mProfs.getLong("pref_high_scores", 0);
//            String lowest = highScores[highScores.length - 1];
//            String [] info = lowest.split("\t");
//            Long lowestHighScore = Long.parseLong(info[info.length - 1]);

            updateHighScores();

            long elapsedSeconds = (mElapsedTime / 1000);

            if (mLongestGame < elapsedSeconds) {
                mLongestGame = (int) elapsedSeconds;
                ed.putString("pref_longest_game", "" + mLongestGame);
                ed.apply();
            }

            mTotalTimePlayed += elapsedSeconds;
            ed.putString("pref_total_time", "" + mTotalTimePlayed);
            ed.apply();

            if (!mGameLoop.isCancelled() && mTimerFinish) {
                if (mSoundEffectsOn) {
                    mSounds.play(gameOverID, 1, 1, 1, 0, 1);
                }
                showDialog(DIALOG_REPLAY_ID);
            }
        }
    }

    private class RoundGameLoop extends GameLoop {

        public RoundGameLoop() {}

        @Override
        protected Void doInBackground(Integer... args) {
            int fps = args[0];
            while (mTimeOfRound - mElapsedTime > 0) {
//                if (mGameLoop.isCancelled() || !mTimerFinish) {
//                    return null;
//                }

                mElapsedTime = (System.nanoTime() / 1000000) - mStartTime;

                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException ie) {}

                if ((mTimeOfRound - mElapsedTime) > 3000) {
                    updateRoundsGame();
                }
                publishProgress();
            }

            if (mGame.getNumActiveAds() > numAdsAllowed) {
                mLostRound = true;
            }
//            Log.d(TAG, "isGameOver: " + !mGame.isGameOver() + " Enough Time: " + (mTimeOfRound - mElapsedTime > 0));
            return null;
        }

        protected void onProgressUpdate(Void... Progress) {
            mTimeTextView.setText(String.format("Time Remaining: %04d", (mTimeOfRound - (int) mElapsedTime) / 1000));
//            mScoreTextView.setText(String.format("%07d", mScore));
            mScoreTextView.setText("Score: " + mScore);
            mGameView.invalidate();
        }

        protected void onPostExecute(Void result) {
            SharedPreferences.Editor ed = mProfs.edit();

            if (mLostRound) {
                updateHighScores();
            }

            if (mMostRoundsBeaten < mRoundNumber) {
                mMostRoundsBeaten = mRoundNumber;
                ed.putString("pref_most_rounds", "" + mMostRoundsBeaten);
                ed.apply();
            }

            long elapsedSeconds = (mElapsedTime / 1000);
            mTotalTimePlayed += elapsedSeconds;
            ed.putString("pref_total_time", "" + mTotalTimePlayed);
            ed.apply();

            if (!mGameLoop.isCancelled() && mTimerFinish) {
                if (mLostRound) {
                    mRoundNumber = 0;
                    if (mSoundEffectsOn) {
                        mSounds.play(gameOverID, 1, 1, 1, 0, 1);
                    }
                    showDialog(DIALOG_REPLAY_ID);
                }
                else {
                    showDialog(DIALOG_CONTINUE_ID);
                }
            }
        }
    }

    private void updateHighScores() {
        SharedPreferences.Editor ed = mProfs.edit();
        int length = highScores.length;

        if (highScores[length - 1] < mScore) {
            mPlayerScores.remove(highScores[length -1]);
            highScores[length - 1] = mScore;
            mPlayerScores.put(mScore, mPlayerName);
            Arrays.sort(highScores);
            StringBuilder sb = new StringBuilder();

            for (int i = length - 1; i > 0; --i) {
                sb.append(mPlayerScores.remove(highScores[i]) + "\t\t");
                sb.append(highScores[i] + ",");
            }
            sb.append(mPlayerScores.remove(highScores[0]) + "\t\t");
            sb.append(highScores[0]);

            ed.putString("pref_high_scores", sb.toString());
            ed.apply();
        }
    }

    public void updateContinuousGame() {
        // AD GENERATION FREQUENCY ALGORITHM

        // calculate how many discrete difficulty intervals have passed in current game
        // Easy     6 sec
        // Medium   4 sec
        // Hard     3 sec
        mCurrentInterval = (int) (mElapsedTime  / (12000 * (mDifficulty + 1)));

        //  time to pass before next Ad is made   <   time that has passed since last Ad creation
        if ((mAdTime - (mCurrentInterval * 10)) < ((System.nanoTime() / 1000000) - mTimeOfLastAd)) {
            generateAd();
        }
    }


    public void updateRoundsGame() {
        // AD GENERATION FREQUENCY ALGORITHM

        //  time to pass before next Ad is made   <   time that has passed since last Ad creation
        if ((mAdTime - ((mRoundNumber + 10) * mDifficulty)) < ((System.nanoTime() / 1000000) - mTimeOfLastAd)) {
            generateAd();
        }
    }

    private void generateAd() {
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
                long points = (long) (mBase * (mDifficulty + 1) / 2 * (mConstant - (4 * (width + height) * scalingFactor)));
                Ad ad = new Ad(imageID.get(index), mBitmap, width, height, new Point(x, y), boxLeftTop, boxBottomRight, points);
//                    Log.d("Ad Infinitum", "points: " + ad.getPointage());
                mGame.addAd(ad);
                break;
            }
        }
        mTimeOfLastAd = System.nanoTime() / 1000000;
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
//                Log.d("Ad Infinitum", "Score: " + mScore);

                ArrayList<Ad> activeAds = mGame.getActiveAds();
                int arraySize = activeAds.size();
                boolean missClicked = true;

                // Determine which cell was touched
                int x = (int) event.getX();
                int y = (int) event.getY();

                for (int i = arraySize - 1; i >= 0; i--) {
                    Ad currentAd = activeAds.get(i);

                    if (currentAd.isPointInBlock(x, y)) {
                        missClicked = false;
                        activeAds.remove(i);
                        if (mSoundEffectsOn) {
                            mSounds.play(dismissAdID2, 1, 1, 1, 0, 1);
                        }
                        mScore += currentAd.getPointage();
                        break;
                    }

                    else if (currentAd.isPointInAd(x, y)) {
                        break;
                    }
                }

                // Lose points from missing an ad.
                if (missClicked) {
                    if (mScore < 1000)
                        mScore = 0;
                    else
                        mScore -= 1000;
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case DIALOG_REPLAY_ID: {
                if (mGameMode.equals(getResources().getString(R.string.mode_continuous))) {
                    builder.setMessage(getResources().getString(R.string.give_up) + " " + mPlayerName).setCancelable(false)
                            .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AdInfinitumActivity.this.finish();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startGame(0, 0);
                                }
                            });
                } else {
                    builder.setMessage(getResources().getString(R.string.rounds_lose_message) + " " + mPlayerName + "?").setCancelable(false)
                            .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startGame(0, 15000);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AdInfinitumActivity.this.finish();
                                }
                            });
                }

                break;
            }
            case DIALOG_CONTINUE_ID:
            {
                builder.setMessage(getResources().getString(R.string.keep_going)).setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startGame(mScore, 15000);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateHighScores();
                                AdInfinitumActivity.this.finish();
                            }
                        });
                break;
            }
        }

        return builder.create();
        }

    public void startGame(long startingScore, int time) {

        mGame = new AdInfinitumGame();
        mGameView.setGame(mGame);
        mCountdownTextView.setEnabled(false);
        mScore = startingScore;
        mElapsedTime = 0;
        mTimeOfLastAd = 0;
        mCurrentInterval = 0;
        mTimeOfRound = time;
        mTimerFinish = true;
        mLostRound = false;
        mGameLoop = null;
        mTimeTextView.setText(String.format("Time: %04d", (time) / 1000));
        mScoreTextView.setText("Score: " + startingScore);
        mGameView.invalidate();

        mCountdownTextView.setVisibility(View.VISIBLE);
        new CountDownTimer(3200, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountdownTextView.setText(" " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mCountdownTextView.setVisibility(View.GONE);
                mGameView.setOnTouchListener(mTouchListener);
                if (mGameMode.equals(getResources().getString(R.string.mode_continuous))) {
                    mGameLoop = new ContinuousGameLoop();
                } else {
                    ++mRoundNumber;
                    mGameLoop = new RoundGameLoop();
                    for (int i = (int) (mRoundNumber / 2) * 2; i > 0; --i) {
                        generateAd();
                    }
                    mGameView.invalidate();
                }
                mStartTime = System.nanoTime() / 1000000;
                mGameLoop.execute(30);
            }
        }.start();

    }

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mMusicService = ((MusicService.ServiceBinder)binder).getService();
            if(mIsSoundOn)
                mMusicService.resumeMusic();
            mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }
}