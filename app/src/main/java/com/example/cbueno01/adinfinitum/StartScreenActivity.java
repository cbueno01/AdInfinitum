package com.example.cbueno01.adinfinitum;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
//
//    // whether service is bounded or not
    private boolean mIsBound;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_screen);

        animScaleC = AnimationUtils.loadAnimation(this, R.anim.anim_scale_fromc);
        title = (TextView)findViewById(R.id.title);

        animScaleBR = AnimationUtils.loadAnimation(this, R.anim.anim_scale_frombr);
        btnPlay = (Button)findViewById(R.id.play);

        animScaleBL = AnimationUtils.loadAnimation(this, R.anim.anim_scale_frombl);
        btnProfile = (Button)findViewById(R.id.profile);

        animScaleTR = AnimationUtils.loadAnimation(this, R.anim.anim_scale_fromtr);
        btnSettings = (Button)findViewById(R.id.settings);

        animScaleTL = AnimationUtils.loadAnimation(this, R.anim.anim_scale_fromtl);
        btnAbout = (Button)findViewById(R.id.about);

        mPrefs = getSharedPreferences("preferences", MODE_PRIVATE);

//        btnScale.startAnimation(animScale);
//        btnScale.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View arg0) {
//                arg0.startAnimation(animScale);
//            }});
//        Intent svc=new Intent(this, MusicService.class);
//        startService(svc);
//        doBindService();


        // HOW TO WORK WITH THE SERVICE:
        // call the following methods whenever
        // you want to interact with you
        // music player
        // ===================================

        // call this e.g. in onPause() of your Activities
        //StartScreenActivity.getService().musicPause();

        // call this e.g. in onStop() of your Activities
        //StartScreenActivity.getService().musicStop();

        // call this e.g. in onResume() of your Activities
        //StartScreenActivity.getService().musicStart();

        // remove title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
            title.startAnimation(animScaleC);
            btnPlay.startAnimation(animScaleBR);
            btnProfile.startAnimation(animScaleBL);
            btnSettings.startAnimation(animScaleTR);
            btnAbout.startAnimation(animScaleTL);
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
        if(mIsSoundOn)
        {
            Intent svc=new Intent(this, MusicService.class);
            stopService(svc); //OR stopService(svc);
        }
//        doUnbindService();
    }

    public void onResume() {
        super.onResume();

        mIsSoundOn = mPrefs.getBoolean("pref_soundtrack", true);

        if(mIsSoundOn)
        {
            Intent svc=new Intent(this, MusicService.class);
            startService(svc); //OR stopService(svc);
        }
//        StartScreenActivity.getService().musicStart();
//        if (mBackgroundSound.isCancelled())
//        if (mBackgroundSound.getStatus() == AsyncTask.Status.FINISHED)
//            mBackgroundSound.doInBackground(null);
    }

    public void onPause() {
        super.onPause();
//        StartScreenActivity.getService().musicPause();
//        mBackgroundSound.cancel(true);
    }

    public void goToGame(View v){
        Log.d("AD INFINITUM", "Create Settings Activity");
        Intent intent = new Intent(this, AdInfinitumActivity.class);
        startActivity(intent);
    }

    public void goToPlayerProfile(View v)
    {
        Log.d("AD INFINITUM", "Trying to create new activity");
        Intent intent = new Intent(this, PlayerProfileActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToSettings(View v)
    {
        Log.d("AD INFINITUM", "Create Settings Activity");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goToAbout (View v)
    {
        Log.d("AD INFINITUM", "Attempt to create About Activity");
        Intent intent = new Intent(this, AboutScreenActivity.class);
        startActivity(intent);

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
//    private ServiceConnection sCon =new ServiceConnection(){
//
//        public void onServiceConnected(ComponentName name, IBinder
//                binder) {
//            mBoundService = (MusicService.ServiceBinder).getService();
//        }
//
//        public void onServiceDisconnected(ComponentName name) {
//            mBoundService = null;
//        }
//    };
//
//    void doBindService(){
//        bindService(new Intent(this,MusicService.class),
//                sCon,Context.BIND_AUTO_CREATE);
//        mIsBound = true;
//    }
//
//    void doUnbindService()
//    {
//        if(mIsBound)
//        {
//            unbindService(sCon);
//            mIsBound = false;
//        }
//    }
//
//    public static MusicService getService() {
//        return mBoundService;
//    }
//
//    private static void setService(MusicService mBoundService) {
//        StartScreenActivity.mBoundService = mBoundService;
//    }
}
