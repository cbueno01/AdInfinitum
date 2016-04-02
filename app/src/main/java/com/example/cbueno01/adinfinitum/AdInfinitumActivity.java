package com.example.cbueno01.adinfinitum;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Squash on 4/1/2016.
 */
public class AdInfinitumActivity extends Activity {

    private static final String TAG = "AdInfinitumActivity";

    // for preferences
    //static final int DIALOG_DIFFICULTY_ID = 0;
    //static final int DIALOG_QUIT_ID = 1;
    //static final int DIALOG_ABOUT_ID = 2;
    //static final int DIALOG_CLEAR_SCORES = 3;

    private int screenWidth;
    private int screenHeight;

    // for pausing game
    private Handler mPauseHandler;
    private Runnable myRunnable;

    // Keep track of score
    private long mScore;

    // game logic
    private AdInfinitumGame mGame;
    private GameView mGameView;

    // random Gen
    private Random rand;

    // Various text displayed
    private TextView mInfoTextView;
    private TextView mTimeTextView;
    private TextView mScoreTextView;

    private boolean mGameOver;

    // for all the sounds  we play
    private SoundPool mSounds;
    private HashMap<Integer, Integer> mSoundIDMap;
    private boolean mSoundOn;

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

        mGame = new AdInfinitumGame();

        mGameView = (GameView) findViewById(R.id.game);
        mGameView.setGame(mGame);

        rand = new Random();

//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

//        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);

//        setTextViewInfo();
//        readScores();
//        displayScores();

        startGame();


        // Listen for touches on the board
        //mBoardView.setOnTouchListener(mTouchListener);

//        mPauseHandler = new Handler();

//        Log.d(TAG, "value of savedInstanceState: " + savedInstanceState);
//        if(savedInstanceState == null)
//            startFromScratch();
//        else
//            restoreGame(savedInstanceState);


    }

    public void startGame() {
        ArrayList<Integer> imageID = getImageIDs();
//        Log.d("Ad Infinitum", "Resource2: " + imageID.get(0));

        BitmapFactory.Options dimensions = new BitmapFactory.Options();
//        dimensions.inJustDecodeBounds = true;
        dimensions.inScaled = false;
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), imageID.get(0), dimensions);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
//        Log.d("Ad Infinitum", "width: " + width + " height: " + height);
        int i = 0;
//        Log.d("Ad Infinitum", "screenwidth: " + screenWidth + " screenheight: " + screenHeight);
        while(i < 10) {

            int x = rand.nextInt(screenWidth);
            int y = rand.nextInt(screenHeight);
//            Log.d("Ad Infinitum", "x: " + x + " y: " + y);
            if(x + width < screenWidth && y + height < screenHeight) {
                Ad ad = new Ad(imageID.get(0), mBitmap, width, height, x, y, 1);
                mGame.addAd(ad);
                i++;
            }
        }

        mGameView.invalidate();
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

