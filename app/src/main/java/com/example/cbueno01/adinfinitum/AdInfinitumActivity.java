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
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private static final double mBase = .1;
    private static final double mConstant = 10000;
    private static final int DIALOG_REPLAY_ID = 1;

    // for preferences
    //static final int DIALOG_DIFFICULTY_ID = 0;
    //static final int DIALOG_QUIT_ID = 1;
    //static final int DIALOG_ABOUT_ID = 2;
    //static final int DIALOG_CLEAR_SCORES = 3;

    private int screenWidth;
    private int screenHeight;

    // time reference
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

    private boolean mGameOver;

    // for all the sounds  we play
    private SoundPool mSounds;
    private HashMap<Integer, Integer> mSoundIDMap;
    private boolean mSoundOn;

    // canceling the game loop
    private boolean mIsCancelled;

    // to restore scores
    private SharedPreferences mPrefs;

    /** Called when the activity is first created. */
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

//        mGame = new AdInfinitumGame();

        mTimeTextView = (TextView) findViewById(R.id.time_elapsed);
        mScoreTextView = (TextView) findViewById(R.id.player_score);
        mGameView = (GameView) findViewById(R.id.game);
//        mGameView.setGame(mGame);
        mGameView.setOnTouchListener(mTouchListener);

        rand = new Random();
        mAdTime = 500;
        mPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        startGame();
//        mScore = 0;
//        mTime = new GregorianCalendar();


//        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);

//        startGame();


        // Listen for touches on the board
        //mBoardView.setOnTouchListener(mTouchListener);

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
        startGame();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Ad Infinitum", "Got to onStop.");
        mGameLoop.cancel(true);
    }

    private class GameLoop extends AsyncTask<Integer, Void, Void> {


//        private Bitmap image;

//        private ColorShader shader;

        public GameLoop() {
//            shader = useColors ? colors : grays;
        }

        @Override
        protected Void doInBackground(Integer... args) {
            Log.d("Ad Infinitum", "isOver: " + mGame.isGameOver() + " fps: " + args[0]);
            while (!mGame.isGameOver()) {
                if (mGameLoop.isCancelled())
                    return null;

                mElapsedTime = (System.nanoTime() / 1000000) - mStartTime;
                int fps = args[0];

                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException ie) {}

                updateGame();
                publishProgress();
            }

            return null;
        }

        protected void onProgressUpdate(Void ...Progress) {
            mTimeTextView.setText(String.format("%04d", (int) (mElapsedTime / 1000)));
            mScoreTextView.setText(String.format("%07d", mScore));
            mGameView.invalidate();
        }

        protected void onPostExecute(Void result) {
            Log.d("Ad Infinitum", "is onPostExecute");
            SharedPreferences.Editor ed = mPrefs.edit();
            long highscore = mPrefs.getLong("pref_high_score", 0);
            if(highscore < mScore) {
                ed.putLong("pref_high_score", mScore);
                ed.apply();
            }

            if(!mGameLoop.isCancelled())
                showDialog(DIALOG_REPLAY_ID);
        }
    }

    public void updateGame() {
//        Log.d("Ad Infinitum", "mAdTime: " + mAdTime + " mTimeDifference: " + mTimeDifference + " mElapsedTime: " + mElapsedTime);
        if(mAdTime < (System.nanoTime() / 1000000) - mTimeOfLastAd)
        {
            ArrayList<Integer> imageID = getImageIDs();
    //        Log.d("Ad Infinitum", "Resource2: " + imageID.get(0));

            BitmapFactory.Options dimensions = new BitmapFactory.Options();
    //        dimensions.inJustDecodeBounds = true;
            dimensions.inScaled = false;
//            int i = 0;
    //        Log.d("Ad Infinitum", "screenwidth: " + screenWidth + " screenheight: " + screenHeight);

            int index = rand.nextInt(imageID.size());
            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), imageID.get(index), dimensions);

            while (true) {
                float scalingFactor = rand.nextFloat() + rand.nextInt(1) + (float)0.3333;
                int width = (int)(mBitmap.getWidth() * scalingFactor);
                int height = (int)(mBitmap.getHeight() * scalingFactor);
                int x = rand.nextInt(Math.abs(screenWidth - width) + 1);
                int y = rand.nextInt(Math.abs(screenHeight - height) + 1);
                //        Log.d("Ad Infinitum", "x: " + x + " y: " + y);
                if (x + width < screenWidth && y + height < screenHeight) {
                    long points = (long)(mBase * (mConstant - (4 * (width + height) * scalingFactor)));
                    Ad ad = new Ad(imageID.get(index), mBitmap, width, height, x, y, points);
                    Log.d("Ad Infinitum", "points: " + ad.getPointage());
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
//            Log.d("Ad Infinitum", "Image name1: " + getResources().getIdentifier(name, "drawable", getPackageName()));
//            Log.d("Ad Infinitum", "Image name2: " + getResources().getIdentifier("soccer_add", "drawable", getPackageName()));
            imageIDs.add(getResources().getIdentifier(name, "drawable", getPackageName()));
        }

//        Log.d("Ad Infinitum", "Resource: " + imageIDs.get(0));
        return imageIDs;
    }

    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if(!mGame.isGameOver())
            {
                Log.d("Ad Infinitum", "Score: " + mScore);

                ArrayList<Ad> activeAds = mGame.getActiveAds();
                int arraySize = activeAds.size();

                // Determine which cell was touched
                int x = (int) event.getX();
                int y = (int) event.getY();
//            int pos = row * 3 + col;

                for(int i = arraySize - 1; i >= 0; i--) {
                    Ad currentAd = activeAds.get(i);

                    if(currentAd.isPointInAd(x, y)) {
                        activeAds.remove(i);
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
//        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.give_up).setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
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

    public void startGame()
    {
        mGame = new AdInfinitumGame();
        mGameView.setGame(mGame);
        mGameView.setOnTouchListener(mTouchListener);
        mScore = 0;
        mElapsedTime = 0;
        mTimeOfLastAd = 0;
        mTimeTextView.setText(R.string.time_elapsed);
        mScoreTextView.setText(R.string.player_score);
        mStartTime = System.nanoTime() / 1000000;
        mGameLoop = new GameLoop();
        mGameLoop.execute(30);

    }


//    private void readScores() {
//        // Restore the scores
//        mScore = mPrefs.getInt("mHumanWins", 0);
//    }
//
//    private void setTextViewInfo() {
//        // get the TextViews
//        mInfoTextView = (TextView) findViewById(R.id.information);
//        mScoreTextView = (TextView) findViewById(R.id.player_score);
//    }
//
//
//    private void startFromScratch() {
//        mScore = 0;
//
//        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.values()[mPrefs.getInt("mDifficulty", TicTacToeGame.DifficultyLevel.EXPERT.ordinal())]);
//        startNewGame(true);
//    }
//
//
//    private void restoreGame(Bundle savedInstanceState) {
//        mGame.setBoardState(savedInstanceState.getCharArray("board"));
//        mGameOver = savedInstanceState.getBoolean("mGameOver");
//        mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
//
//    }
//
//    private void displayScore() {
//
//        mScoreTextView.setText(Integer.toString(mHumanWins));
//    }
//
//    private void displayScores() {
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        createSoundPool();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putCharArray("board", mGame.getBoardState());
//        outState.putBoolean("mGameOver", mGameOver);
//        outState.putChar("mTurn", mTurn);
//        outState.putChar("mGoesFirst", mGoesFirst);
//        outState.putInt("mGoesFirstPattern", mGoesFirstPattern.ordinal());
//        outState.putCharSequence("info", mInfoTextView.getText());
//
//
//    }
//
//    private void createSoundPool() {
//        int[] soundIds = {R.raw.human_move, R.raw.computer_move, R.raw.human_win,
//                R.raw.computer_win, R.raw.tie_game};
//        mSoundIDMap = new HashMap<Integer, Integer>();
//        mSounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
//        for(int id : soundIds)
//            mSoundIDMap.put(id, mSounds.load(this, id, 1));
//        mSoundOn = mPrefs.getBoolean("sound", true);
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        Log.d(TAG, "in onPause");
//
//        if(mSounds != null) {
//            mSounds.release();
//            mSounds = null;
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        // Save the current scores
//        SharedPreferences mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
//        SharedPreferences.Editor ed = mPrefs.edit();
//        ed.putInt("mHumanWins", mHumanWins);
//        ed.putInt("mComputerWins", mComputerWins);
//        ed.putInt("mTies", mTies);
//        ed.putInt("mDifficulty", mGame.getDifficultyLevel().ordinal());
//        Log.d(TAG, "in onStop: difficulty: " + mGame.getDifficultyLevel());
//        ed.apply();
//    }
//
//
//    // Set up the game baord.
//    private void startNewGame(boolean first) {
//        // check if new game after a complete game and if so swap who goes first
//
//        mGame.clearBoard();
//        mBoardView.invalidate();   // Redraw the board
//
//
//    }
//
//
//    private Runnable createRunnable(final int location) {
//        return new Runnable() {
//            public void run() {
//
//                mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, location);
//                // soundID, leftVolume, rightVolume, priority, loop, rate
//                if (mSoundOn) {
//                    mSounds.play(mSoundIDMap.get(R.raw.computer_move), 1, 1, 1, 0, 1);
//                }
//
//                mBoardView.invalidate();   // Redraw the board
//
//                int winner = mGame.checkForWinner();
//                if (winner == 0) {
//                    mTurn = TicTacToeGame.HUMAN_PLAYER;
//                    mInfoTextView.setText(R.string.turn_human);
//                }
//                else
//                    endGame(winner);
//            }
//        };
//    }
//
//    // Game is over logic
//    private void endGame(int winner) {
//        mGameOver = true;
//
//        else if (winner == 2) {
//            mHumanWins++;
//            mHumanScoreTextView.setText(Integer.toString(mHumanWins));
//            String defaultMessage = getResources().getString(R.string.result_human_wins);
//            mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
//            if (mSoundOn) {
//                mSounds.play(mSoundIDMap.get(R.raw.human_win), 1, 1, 1, 0, 1);
//            }
//            //String defaultMsg = getResources().getString(R.string.result_human_wins);
//            String victory_message = mPrefs.getString("victory_message", defaultMessage);
//            prepDownloadImageActivity(2, victory_message);
//        }
//    }
//
//    private void prepDownloadImageActivity(int winner, String message) {
//        Intent intent = new Intent(this, DownloadImage.class);
//        intent.putExtra("winner", winner);
//        intent.putExtra("message", message);
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == RESULT_CANCELED) {
//            // Apply potentially new settings
//            mSoundOn = mPrefs.getBoolean("sound", true);
//            String[] levels = getResources().getStringArray(R.array.List_difficulty_level);
//
//            // set difficulty, or use hardest if not present,
//            String difficultyLevel = mPrefs.getString("difficulty_level", levels[levels.length - 1]);
//            int i = 0;
//            while(i < levels.length) {
//                if(difficultyLevel.equals(levels[i])) {
//                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.values()[i]);
//                    i = levels.length;
//                }
//                ++i;
//            }
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
//        return true;
//    }


//    private void resetScore() {
//        this.mScore = 0;
//        displayScores();
//    }


//    // Listen for touches on the screen
//    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
//        public boolean onTouch(View v, MotionEvent event) {
//
//            Log.d(TAG, "in onTouch. Board status: " + Arrays.toString(mGame.getBoardState()));
//
//            // Determine which cell was touched
//            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
//            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
//            int pos = row * 3 + col;
//
//            if (!mGameOver && mTurn == TicTacToeGame.HUMAN_PLAYER &&
//                    setMove(TicTacToeGame.HUMAN_PLAYER, pos))	{
//
//                // If no winner yet, let the computer make a move
//                int winner = mGame.checkForWinner();
//                if (winner == 0) {
//                    mInfoTextView.setText(R.string.turn_computer);
//                    int move = mGame.getComputerMove();
//                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
//                }
//                else
//                    endGame(winner);
//            }
//
//            // So we aren't notified of continued events when finger is moved
//            return false;
//        }
//    };
//
//    public void pickRandom(View v) {
//        Spinner spinner = (Spinner) findViewById(R.id.football_club_spinner);
//        int oldIndex = spinner.getSelectedItemPosition();
//        Log.d(TAG, "old index  = " + oldIndex);
//        // don't want to pick the BPL symbol itself, so index 1 - 20
//        int newIndex = randNumGen.nextInt(imageIDs.size() - 1) + 1;
//        // don't let the new one be the old one
//        // are we worried this will result in infinite loop with just 1 team??
//        while (oldIndex == newIndex) {
//            newIndex = randNumGen.nextInt(imageIDs.size() - 1) + 1;
//        }
//        Log.d(TAG, "new index  = " + newIndex);
//        ImageView iv = (ImageView) findViewById(R.id.imageView);
//        iv.setImageResource(imageIDs.get(newIndex));
//        spinner.setSelection(newIndex);
//    }

}

