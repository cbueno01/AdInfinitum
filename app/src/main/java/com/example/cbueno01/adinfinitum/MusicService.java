package com.example.cbueno01.adinfinitum;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service  implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    int length = 0;

    public MusicService() {
    }

    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences mPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        String soundtrack = mPrefs.getString("pref_soundtrack", getResources().getString(R.string.default_soundtrack));
        Log.d("AD INFINITUM", soundtrack);
        int sound;
        switch (soundtrack)
        {
            case "Rave":
                sound = R.raw.aviator;
                break;
            case "Chill":
                sound = R.raw.pretty_lights;
                break;
            case "Pulsate":
                sound = R.raw.tickclock;
                break;
            default:
                sound = R.raw.aviator;
        }

        mPlayer = MediaPlayer.create(this, sound);
        mPlayer.setOnErrorListener(this);

        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(100, 100);
        }


        mPlayer.setOnErrorListener(new OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {

                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        return START_STICKY;
    }

    public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();
        }
    }

    public void resumeMusic() {

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (mPlayer.isPlaying() == false && pm.isScreenOn()) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public void changeSong(String soundtrack) {
        int sound;
        switch (soundtrack)
        {
            case "Rave":
                sound = R.raw.aviator;
                break;
            case "Chill":
                sound = R.raw.pretty_lights;
                break;
            case "Pulsate":
                sound = R.raw.tickclock;
                break;
            default:
                sound = R.raw.aviator;
        }

        mPlayer = MediaPlayer.create(this, sound);
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }
}
