package com.example.cbueno01.adinfinitum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AD INFINITUM", "In Settings");
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("preferences");
        addPreferencesFromResource(R.xml.preferences);


        final SharedPreferences prefs =
                getSharedPreferences("preferences", MODE_PRIVATE);

        //show game mode setting summary
        Log.d("AD INFINITUM", "Game mode pref");
        final ListPreference gameModePref = (ListPreference) findPreference("pref_modes");
        String mode = prefs.getString("pref_modes",
                getResources().getString(R.string.mode_continuous));
        gameModePref.setSummary((CharSequence) mode);
        gameModePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                gameModePref.setSummary((CharSequence) newValue);
                // Since we are handling the pref, we must save it
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("pref_modes", newValue.toString());
                ed.apply();
                return true;
            }
        });


        //show difficulty levels setting summary
        Log.d("AD INFINITUM", "Difficulty pref");
        final ListPreference difficultyLevelPref = (ListPreference) findPreference("pref_difficulty_level");
        String difficulty = prefs.getString("pref_difficulty_level",
                getResources().getString(R.string.difficulty_level_medium));
        difficultyLevelPref.setSummary((CharSequence) difficulty);
        difficultyLevelPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                difficultyLevelPref.setSummary((CharSequence) newValue);
                // Since we are handling the pref, we must save it
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("difficulty_level", newValue.toString());
                ed.apply();
                return true;
            }
        });

        //show the current profile name summary
        Log.d("AD INFINITUM", "Profile Pref");
        final Preference profileNamePref = (Preference) findPreference("pref_reset_profile");
        String profileName = prefs.getString("pref_reset_profile",
                getResources().getString(R.string.default_profile_name));
        profileNamePref.setSummary(profileName);
        profileNamePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                profileNamePref.setSummary((CharSequence) newValue);

                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("pref_reset_profile", newValue.toString());
                ed.apply();
                return true;
            }
        });

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
                soundfxPref.setChecked((Boolean) newValue);
                return true;
            }
        });

            //show the soundtrack volume
        Log.d("AD INFINITUM", "Soundtrack Volume Settings");
        final SwitchPreference soundtrackPref = (SwitchPreference) findPreference("pref_soundtrack");
        Boolean soundtrackState = prefs.getBoolean("pref_soundtrack",
                getResources().getBoolean(R.bool.default_soundtrack_state));
        soundtrackPref.setChecked(soundtrackState);
        soundtrackPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                soundtrackPref.setChecked((Boolean) newValue);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
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


}
