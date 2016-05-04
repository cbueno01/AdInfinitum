package com.example.cbueno01.adinfinitum;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class SettingsActivity extends PreferenceActivity {

    private SharedPreferences prefs;

    private MediaPlayer mp;
    private boolean mIsButtonSoundOn;
    private boolean mIsSoundtrackOn;
    private MusicService mMusicService;
    private boolean mIsBound;
    private boolean mIsLandscape;

    protected void onCreate(Bundle savedInstanceState) {

        mp = MediaPlayer.create(this, R.raw.button_click);

        Log.d("AD INFINITUM", "In Settings");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getPreferenceManager().setSharedPreferencesName("preferences");
        addPreferencesFromResource(R.xml.preferences);

        prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        mIsSoundtrackOn = prefs.getBoolean("pref_soundtrack_sound", true);
        ActivityHelper.initialize(this);


        //game mode setting
        Log.d("AD INFINITUM", "Game mode pref");
        final ListPreference gameModePref = (ListPreference) findPreference("pref_modes");
        String mode = prefs.getString("pref_modes",
                getResources().getString(R.string.mode_continuous));
        gameModePref.setSummary((CharSequence) mode);
        gameModePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                gameModePref.setSummary((CharSequence) newValue);
                playButtonSound();
                // Since we are handling the pref, we must save it
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("pref_modes", newValue.toString());
                ed.apply();
                return true;
            }
        });


        //difficulty levels setting
        Log.d("AD INFINITUM", "Difficulty pref");
        final ListPreference difficultyLevelPref = (ListPreference) findPreference("pref_difficulty_level");
        String difficulty = prefs.getString("pref_difficulty_level",
                getResources().getString(R.string.difficulty_level_easy));
        difficultyLevelPref.setSummary((CharSequence) difficulty);
        difficultyLevelPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                difficultyLevelPref.setSummary((CharSequence) newValue);
                playButtonSound();
                // Since we are handling the pref, we must save it
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("difficulty_level", newValue.toString());
                ed.apply();
                return true;
            }
        });

//        //soundtrack choice
//        Log.d("AD INFINITUM", "Soundtrack Pref");
//        final ListPreference soundtrackPref = (ListPreference) findPreference("pref_soundtrack");
//        String soundtrack = prefs.getString("pref_soundtrack",
//                getResources().getString(R.string.default_soundtrack));
//        soundtrackPref.setSummary( soundtrack);
//        soundtrackPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                playButtonSound();
//
//                soundtrackPref.setSummary((CharSequence) newValue);
//
//                Log.d("AD INFINITUM", (String) newValue);
//
//                SharedPreferences.Editor ed = prefs.edit();
//                ed.putString("pref_soundtrack", newValue.toString());
//                ed.apply();
//
//                if (mIsSoundtrackOn) {
//                    mMusicService.pauseMusic();
//                }
//
//                mMusicService.changeSong(newValue.toString());
//
//                if(mIsSoundtrackOn) {
//                    mMusicService.resumeMusic();
//                }
//
//
//                return true;
//            }
//        });


        /*//show the current profile name summary
        Log.d("AD INFINITUM", "Profile Pref");
        final Preference profileNamePref = (Preference) findPreference("pref_reset_profile");
        String profileName = prefs.getString("pref_reset_profile",
                getResources().getString(R.string.default_profile_name));
        profileNamePref.setSummary(profileName);
        profileNamePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                profileNamePref.setSummary((CharSequence) newValue);
                playButtonSound();

                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("pref_reset_profile", newValue.toString());
                ed.apply();
                return true;
            }
        });
*/

        //show the sound fx volume
        Log.d("AD INFINITUM", "Sound FX Volume Settings");
        final SwitchPreference soundfxPref = (SwitchPreference) findPreference("pref_sound_fx");
        Boolean soundfxState = prefs.getBoolean("pref_soundfx",
                getResources().getBoolean(R.bool.default_soundfx_state));
        Log.d("AD INFINITUM", soundfxState.toString());
        soundfxPref.setChecked(soundfxState);
        soundfxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                Log.d("Ad Infinitum", "Changing soundfxState to: " + newValue);
                playButtonSound();

                soundfxPref.setChecked((boolean) newValue);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean("pref_soundfx", (boolean) newValue);
                ed.apply();
                return true;
            }
        });

            //show the soundtrack volume
        Log.d("AD INFINITUM", "Soundtrack Volume Settings");
        final SwitchPreference soundtrackSoundPref = (SwitchPreference) findPreference("pref_soundtrack_sound");
        Boolean soundtrackSoundState = prefs.getBoolean("pref_soundtrack_sound",
                getResources().getBoolean(R.bool.default_soundtrack_state));
        soundtrackSoundPref.setChecked(soundtrackSoundState);
        soundtrackSoundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIsSoundtrackOn = (Boolean) newValue;
                playButtonSound();

                soundtrackSoundPref.setChecked(mIsSoundtrackOn);

                if(mIsSoundtrackOn) {
                    mMusicService.resumeMusic();
                }
                else
                {
                    mMusicService.pauseMusic();
                }

                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean("pref_soundtrack_sound", mIsSoundtrackOn);
                ed.apply();

                return true;
            }
        });

        //button sounds
        Log.d("AD INFINITUM", "Button Sounds Settings");
        final SwitchPreference soundButtonPref = (SwitchPreference) findPreference("pref_sound_button");
        mIsButtonSoundOn = prefs.getBoolean("pref_sound_button",
                getResources().getBoolean(R.bool.default_sound_button_state));

        soundButtonPref.setChecked(mIsButtonSoundOn);
        soundButtonPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                playButtonSound();

                soundButtonPref.setChecked((boolean) newValue);

                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean("pref_sound_button", (boolean) newValue);
                ed.apply();

                mIsButtonSoundOn = (boolean) newValue;

                return true;
            }
        });

        //orientation checkbox
        Log.d("AD INFINITUM", "Checkbox settings");
        final CheckBoxPreference orientationPref = (CheckBoxPreference) findPreference("pref_orientation");
        mIsLandscape = prefs.getBoolean("pref_orientation",
                getResources().getBoolean(R.bool.default_orientation));

        orientationPref.setChecked(mIsLandscape);
        orientationPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                mIsLandscape = (boolean) newValue;
                orientationPref.setChecked((boolean) newValue);


                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean("pref_orientation", (boolean) newValue);
                ed.apply();


                if(mIsLandscape) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
                return true;
            }
        });


/*        //show the seek bar sound fx summary
        Log.d("AD INFINITUM", "Sound FX Pref");
        SeekBarPreference soundfxPref = new SeekBarPreference( this );
        soundfxPref.setTitle(R.string.settings_sound_fx_volume);
        soundfxPref.setDefaultValue(R.string.default_sound);
        soundfxPref.setMax(R.string.max_sound);
        //prefs.addPreference(botsPref);
        soundfxPref.setOnPreferenceChangeListener( new PrefsSeekBarListener( soundfxPref) );

        //show the seek bar soundtrack summary
        Log.d("AD INFINITUM", "Soundtrack Pref");
        SeekBarPreference soundtrackPref = new SeekBarPreference( this );
        soundtrackPref.setTitle(R.string.settings_soundtrack_volume);
        soundtrackPref.setDefaultValue(R.string.default_sound);
        soundtrackPref.setMax(R.string.max_sound);
        // prefs.addPreference(botsPref );
        soundtrackPref.setOnPreferenceChangeListener( new PrefsSeekBarListener( soundtrackPref) );*/

//        mIsSoundtrackOn = prefs.getBoolean("pref_soundtrack_sound", true);
//        KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//        if( myKM.inKeyguardRestrictedInputMode()) {
//            //it is locked
//            if(mIsSoundtrackOn) {
//                Intent svc = new Intent(this, MusicService.class);
//                stopService(svc);
//            }
//        } else {
//            //it is not locked
//            if(mIsSoundtrackOn) {
//                Intent svc = new Intent(this, MusicService.class);
//                startService(svc);
//            }
//        }
        doBindService();


    }

    public void onResume() {
        super.onResume();
        if (mIsBound && mIsSoundtrackOn)
            mMusicService.resumeMusic();
    }
    public void onPause() {
        super.onPause();
        if(mIsBound && mIsSoundtrackOn)
            mMusicService.pauseMusic();
    }

    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private void playButtonSound() {
        Log.d("AD INFINITUM", "In playButtonSound");
        if (mIsButtonSoundOn) {
            //play button sound
            mp.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mMusicService = ((MusicService.ServiceBinder)binder).getService();
            if(mIsSoundtrackOn)
                mMusicService.resumeMusic();
            mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
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

}
