package com.example.cbueno01.adinfinitum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.*;
import java.io.*;

/**
 * Created by cbueno01 on 3/29/16.
 */
public class PlayerProfileActivity extends Activity {

    private static final String TAG = "Player Profile";

    private static final int DIALOG = 0;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 1313;

    private String mPlayerName;
    private long mHighScore;
    private List<Long> mHighScores;
    private String mImagePath;

    private String mLongestGame;
    private String mMostRounds;
    private String mTotalTime;

    // to restore scores
    private SharedPreferences mPrefs;
    private SharedPreferences mSettingsPref;

    private String [] highScores;
    private Dialog mDialog;

    private TextView mNameTextView;
    private TextView mHighScoreTextView;
    private ImageView mImageView;
    private ImageView mCameraImageView;
    private TextView mTitleTextView;
    private ListView mListViewC;
    private ListView mListViewR;
    private EditText mEditText;

    private Button mResetButton;
    private boolean mIsBound;
    private MusicService mMusicService;
    private boolean mIsSoundOn;
    private TextView mTotalTimeTV;
    private TextView mLongestGameTV;
    private TextView mMostRoundsTV;

    private Vibrator mVibe;

    private boolean mIsButtonSoundOn;
    private boolean inApp = false;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      Log.d("Ad Infinitum", "In player profile activity");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_player_profile_screen);
        ActivityHelper.initialize(this);
//        getPreferenceManager().setSharedPreferencesName("preferences");
//        addPreferencesFromResource(R.xml.preferences);
        mPrefs = getSharedPreferences("profile", MODE_PRIVATE);
        mSettingsPref = getSharedPreferences("preferences", MODE_PRIVATE);

//        highScores = getResources().getStringArray(R.array.high_scores);

        mIsSoundOn = mSettingsPref.getBoolean("pref_soundtrack_sound", true);

        mIsButtonSoundOn = mSettingsPref.getBoolean("prefs_sound_button", true);
        if (mIsButtonSoundOn) {
            mp = MediaPlayer.create(this, R.raw.button_click);
        }

        mEditText = (EditText) findViewById(R.id.player_name);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                    imm.showSoftInputFromInputMethod(mEditText.getWindowToken(), 0);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
                }
            }
        });

        setTextViews();
        readData();
        displayViews();
        doBindService();
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


    public void onClick(View view) {

        if (view.equals(mHighScoreTextView)) {
            showDialog(DIALOG);
//            Context context = getApplicationContext();
//            Dialog dlg = new Dialog(context);
//            LayoutInflater li = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//            View v = li.inflate(R.layout.highscores_dialog, null, false);
//            dlg.setContentView(v);
//            dlg.show();
        }

//        v.setVisibility(View.GONE);
//        v.setClickable(false);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {

            case DIALOG: {
                Dialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                dialog = createHighScoresDialog(builder);



                if (dialog == null)
                    Log.d(TAG, "Uh oh! Dialog is null");
                else
                    Log.d(TAG, "Dialog created: " + id + ", dialog: " + dialog);
                return dialog;
            }
            default:
                return null;

        }
    }

//    @Override
//    protected void onPrepareDialog(int id, Dialog dialog)
//    {
//        switch (id)
//        {
//            case DIALOG:
//            {
//
//
//            }
//            break;
//        }
//    }

    private Dialog createHighScoresDialog(AlertDialog.Builder builder) {
        String chs = mPrefs.getString("pref_high_scores", getResources().getString(R.string.default_high_scores));
//        String rhs = mPrefs.getString("pref_rounds_high_scores", getResources().getString(R.string.default_high_scores));

//        Log.d(TAG, "");
//        Log.d(TAG, "");
//        Log.d("Profile", "*^&*%%($(%)#(%)#$#%*)$*)&@$(@&#)#$%)(&%(#)@$               " + rhs);
//        Log.d(TAG, "");
//        Log.d(TAG, "");

        String[] cScores = chs.split(",");
//        String[] rScores = rhs.split(",");
//        ListView lv = (ListView) findViewById(R.id.high_scores_dialog_list);

        ArrayAdapter<String> itemsAdapterC = new ArrayAdapter<>(this, R.layout.listview_item, cScores);
//        ArrayAdapter<String> itemsAdapterR = new ArrayAdapter<>(this, R.layout.listview_item_two, rScores);


//        ListView listView = (ListView) findViewById(R.id.high_scores_dialog_list);
//        mListViewC.setAdapter(itemsAdapter);
        Context context = getApplicationContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.highscores_dialog, null);
//        mListViewC = inflater.inflate(R.layout.highscores_dialog, null);

        mListViewC = (ListView) layout.findViewById(R.id.high_scores_dialog_list);
        mListViewC.setAdapter(itemsAdapterC);

//        mListViewR = (ListView) layout.findViewById(R.id.rounds_high_scores_dialog_list);
//        mListViewR.setAdapter(itemsAdapterR);

        builder.setView(layout);
        builder.setTitle(R.string.high_scores_title);

        builder.setPositiveButton("Okay", null);
        return builder.create();
    }


    public void setTextViews() {
//        mEditText = (EditText) findViewById(R.id.player_name);
        mHighScoreTextView = (TextView) findViewById(R.id.continuous_high_score_list);
        mImageView = (ImageView) findViewById(R.id.player_image);
        mTitleTextView = (TextView) findViewById(R.id.profile_title);

        mTotalTimeTV = (TextView) findViewById(R.id.default_total_time_played);
        mLongestGameTV = (TextView) findViewById(R.id.default_longest_game_played);
        mMostRoundsTV = (TextView) findViewById(R.id.default_most_rounds_beaten);

        mResetButton = (Button) findViewById(R.id.reset_button);

        mVibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        Animation blinkAnimation2 = AnimationUtils.loadAnimation(this, R.anim.blink2);
        mTitleTextView.startAnimation(blinkAnimation);
//        mNameTextView.startAnimation(blinkAnimation2);
//        mHighScoreTextView.startAnimation(blinkAnimation2);



        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.charAt(s.length() - 1) == '\n') {
                    Log.d("TEST RESPONSE", "Enter was pressed");
                }

                SharedPreferences.Editor ed = mPrefs.edit();
                String name = mPrefs.getString("pref_profile_name", "");
                ed.putString("pref_profile_name", mEditText.getText().toString());
                ed.apply();
            }
        });

//        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                    SharedPreferences.Editor ed = mPrefs.edit();
//                    String name = mPrefs.getString("pref_profile_name", "");
//                    ed.putString("pref_profile_name", mEditText.getText().toString());
//                    ed.apply();
//
//                    return true;
//                }
//                return false;
//            }
//
//
//        });
    }

    public void readData() {
        mPlayerName = mPrefs.getString("pref_profile_name", null);
//        mHighScore = mPrefs.getLong("pref_high_score", 0);

        mImagePath = mSettingsPref.getString("pref_reset_picture", null);


        mTotalTime = mPrefs.getString("pref_total_time", "0");
        mLongestGame = mPrefs.getString("pref_longest_game", "0");
        mMostRounds = mPrefs.getString("pref_most_rounds", "0");
    }

    public void displayViews() {
//        mNameTextView.setText("Name: " + mPlayerName);
//        mHighScoreTextView.setText("High Score: " + mHighScore);

        mEditText.setText(mPlayerName);
        mTotalTimeTV.setText(mTotalTime);
        mLongestGameTV.setText(mLongestGame);
        mMostRoundsTV.setText(mMostRounds);

        if (mImagePath == null) {
            mImageView.setImageResource(R.drawable.sheeple);
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bmp = BitmapFactory.decodeFile(mImagePath, options);
            if (bmp == null)
                mImageView.setImageResource(R.drawable.sheeple);
            else
            {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp,bmp.getWidth(),bmp.getHeight(),true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

                mImageView.setImageBitmap(rotatedBitmap);
            }
        }

    }

    public void resetScore(View v) {
        SharedPreferences.Editor ed = mPrefs.edit();

        mVibe.vibrate(200);
//        if (mIsButtonSoundOn) {
//            mp.start();
//        }

        ed.putString("pref_high_scores", getString(R.string.default_high_scores));
        ed.putString("pref_rounds_high_scores", getString(R.string.default_high_scores));
//        ed.putString("pref_total_time", getString(R.string.default_playtime));
        ed.putString("pref_longest_game", getString(R.string.default_longest_game));
        ed.putString("pref_most_rounds", getString(R.string.default_most_rounds));

        ed.apply();

//        mTotalTimeTV.setText(mPrefs.getString("pref_total_time", "0"));
        mLongestGameTV.setText(mPrefs.getString("pref_longest_game", "0"));
        mMostRoundsTV.setText(mPrefs.getString("pref_most_rounds", "0"));

        removeDialog(DIALOG);
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public void onResume() {
        super.onResume();
        if (mIsBound && mIsSoundOn)
            mMusicService.resumeMusic();
    }

    public void onBackPressed() {
        Log.d("AD INFINITUM", "In onBackPressed");
        inApp = true;
        finish();
    }

    public void onPause() {
        super.onPause();
        if (mIsBound && mIsSoundOn && !inApp)
            mMusicService.pauseMusic();
    }

    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mMusicService = ((MusicService.ServiceBinder) binder).getService();
            if (mIsSoundOn)
                mMusicService.resumeMusic();
            mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
//            mMusicService.pauseMusic();
            mMusicService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    public void selectImage(View v) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                String filePath = destination.getAbsolutePath();
                SharedPreferences.Editor ed = mSettingsPref.edit();
                ed.putString("pref_reset_picture", getPath(data.getData()));
                ed.apply();

                mImageView.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                SharedPreferences.Editor ed = mSettingsPref.edit();
                ed.putString("pref_reset_picture", getPath(data.getData()));
                ed.apply();

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm,bm.getWidth(),bm.getHeight(),true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);


                mImageView.setImageBitmap(rotatedBitmap);
            }
        }
    }
}
