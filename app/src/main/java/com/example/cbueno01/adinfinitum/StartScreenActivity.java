package com.example.cbueno01.adinfinitum;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class StartScreenActivity extends Activity {

//    BackgroundSound mBackgroundSound = new BackgroundSound();
//    private boolean startedSoundOnce = false;
    private SharedPreferences mPrefs;

    // bounded service
    private static MusicService mMusicService;
    private int mMusicPosition;
//
//    // whether service is bounded or not
    private boolean mIsBound = false;

    private Context mContext;
    private CoordinatorLayout mCL;
    private StarFieldView mSFV;

    private TextView title;
    private Animation animScaleC;

    private Button btnPlay;
    private Animation animScaleBR;

    private Button btnProfile;
    private Animation animScaleBL;

    private Button btnSettings;
    private Animation animScaleTR;

    private Button btnAbout;
    private Animation animScaleTL;

    private boolean mIsSoundOn;

    private MediaPlayer mp;
    private boolean mIsButtonSoundOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_screen);

//        animScaleC = AnimationUtils.loadAnimation(this, R.anim.anim_scale_fromc);
//        title = (TextView)findViewById(R.id.title);
//
//        animScaleBR = AnimationUtils.loadAnimation(this, R.anim.anim_scale_frombr);
//        btnPlay = (Button)findViewById(R.id.play);
//
//        animScaleBL = AnimationUtils.loadAnimation(this, R.anim.anim_scale_frombl);
//        btnProfile = (Button)findViewById(R.id.profile);
//
//        animScaleTR = AnimationUtils.loadAnimation(this, R.anim.anim_scale_fromtr);
//        btnSettings = (Button)findViewById(R.id.settings);
//
//        animScaleTL = AnimationUtils.loadAnimation(this, R.anim.anim_scale_fromtl);
//        btnAbout = (Button)findViewById(R.id.about);

        mPrefs = getSharedPreferences("preferences", MODE_PRIVATE);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Rect viewBounds = new Rect(0, 0, displaymetrics.heightPixels, displaymetrics.widthPixels);

        mContext = getApplicationContext();
        mSFV= new StarFieldView(mContext, viewBounds);
        mCL = (CoordinatorLayout) findViewById(R.id.start_screen_layout);

        mContext = getApplicationContext();
        mCL = (CoordinatorLayout) findViewById(R.id.start_screen_layout);
        mSFV.init();

        mIsSoundOn = mPrefs.getBoolean("pref_soundtrack_sound", true);

        doBindService();


//        AttributeSet as = new AttributeSet() {
//            @Override
//            public int getAttributeCount() {
//                return 0;
//            }
//
//            @Override
//            public String getAttributeName(int index) {
//                return null;
//            }
//
//            @Override
//            public String getAttributeValue(int index) {
//                return null;
//            }
//
//            @Override
//            public String getAttributeValue(String namespace, String name) {
//                return null;
//            }
//
//            @Override
//            public String getPositionDescription() {
//                return null;
//            }
//
//            @Override
//            public int getAttributeNameResource(int index) {
//                return 0;
//            }
//
//            @Override
//            public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
//                return false;
//            }
//
//            @Override
//            public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public int getAttributeListValue(int index, String[] options, int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
//                return false;
//            }
//
//            @Override
//            public int getAttributeResourceValue(int index, int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public int getAttributeIntValue(int index, int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public int getAttributeUnsignedIntValue(int index, int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public float getAttributeFloatValue(int index, float defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public String getIdAttribute() {
//                return null;
//            }
//
//            @Override
//            public String getClassAttribute() {
//                return null;
//            }
//
//            @Override
//            public int getIdAttributeResourceValue(int defaultValue) {
//                return 0;
//            }
//
//            @Override
//            public int getStyleAttribute() {
//                return 0;
//            }
//        }

//        mCL.setBackground(mSFV);
//        mCL.setBackground();

        //button sound


//        btnScale.startAnimation(animScale);
//        btnScale.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View arg0) {
//                arg0.startAnimation(animScale);
//            }});



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
//            title.startAnimation(animScaleC);
//            btnPlay.startAnimation(animScaleBR);
//            btnProfile.startAnimation(animScaleBL);
//            btnSettings.startAnimation(animScaleTR);
//            btnAbout.startAnimation(animScaleTL);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
//        Intent music = new Intent(this, MusicService.class);
//        stopService(music);
    }


    public void onStop() {
        super.onStop();
        Log.d("Ad Infinitum", "StartScreen onstop");
//        doUnbindService();
//        if(mIsBound)
//            mMusicService.pauseMusic();
    }

    public void onResume() {
        super.onResume();
        mIsSoundOn = mPrefs.getBoolean("pref_soundtrack_sound", true);
        mIsButtonSoundOn = mPrefs.getBoolean("prefs_sound_button", true);
        if(mIsButtonSoundOn) {
            mp = MediaPlayer.create(this, R.raw.button_click);
        }
        else
        {
            mp = null;
        }

        if (mIsBound && mIsSoundOn)
            mMusicService.resumeMusic();
    }
    public void onPause() {
        super.onPause();
        if (mIsBound && mIsSoundOn)
            mMusicService.pauseMusic();
    }

//    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mMusicService = ((MusicService.ServiceBinder)binder).getService();
            if (mIsSoundOn)
                mMusicService.resumeMusic();
            mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            if(mIsSoundOn)
                mMusicService.stopMusic();
            mMusicService = null;
        }
};

    void doBindService() {
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }


//    private class StarFieldLoop extends AsyncTask<Integer, Void, Void> {
//
//        public StarFieldLoop() {
//        }
//
//        @Override
//        protected Void doInBackground(Integer... args) {
//            while (true) {
//
//                mElapsedTime = (System.nanoTime() / 1000000) - mStartTime;
//                int fps = args[0];
//
//                try {
//                    Thread.sleep(1000 / fps);
//                } catch (InterruptedException ie) {
//                }
//
//                updateGame();
//                publishProgress();
//            }
//
//            return null;
//        }
//
//        protected void onProgressUpdate(Void... Progress) {
//            mTimeTextView.setText(String.format("%04d", (int) (mElapsedTime / 1000)));
////            mScoreTextView.setText(String.format("%07d", mScore));
//            mScoreTextView.setText("" + mScore);
//            mGameView.invalidate();
//        }
//
//        protected void onPostExecute(Void result) {
//            SharedPreferences.Editor ed = mPrefs.edit();
//            long highscore = mPrefs.getLong("pref_high_score", 0);
//            if (highscore < mScore) {
//                ed.putLong("pref_high_score", mScore);
//                ed.apply();
//            }
//
//            if (!mGameLoop.isCancelled() && mTimerFinish) {
//                showDialog(DIALOG_REPLAY_ID);
//                if (mSoundEffectsOn) {
//                    mSounds.play(gameOverID, 1, 1, 1, 0, 1);
//                }
//            }
//        }
//    }



    public void goToGame(View v){
        Log.d("AD INFINITUM", "Create Settings Activity");
        Intent intent = new Intent(this, AdInfinitumActivity.class);

        playButtonSound();

        startActivity(intent);
    }

    public void goToPlayerProfile(View v)
    {
        Log.d("AD INFINITUM", "Trying to create new activity");
        Intent intent = new Intent(this, PlayerProfileActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);

        playButtonSound();

        startActivity(intent);
    }

    public void goToSettings(View v)
    {
        Log.d("AD INFINITUM", "Create Settings Activity");
        Intent intent = new Intent(this, SettingsActivity.class);

        playButtonSound();

        startActivity(intent);
    }

    public void goToAbout (View v)
    {
        Log.d("AD INFINITUM", "Attempt to create About Activity");
        Intent intent = new Intent(this, AboutScreenActivity.class);

        playButtonSound();

        startActivity(intent);
    }

    private void playButtonSound() {
        Log.d("AD INFINITUM", "In playButtonSound");

        if (mIsButtonSoundOn) {
            //play button sound
            mp.start();
        }
    }

//    public class BackgroundSound extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            MediaPlayer player = MediaPlayer.create(StartScreenActivity.this, R.raw.aviator);
//            player.setLooping(true); // Set looping
//            player.setVolume(100,100);
//            player.start();
//            return null;
//        }
//    }
//
//
}
