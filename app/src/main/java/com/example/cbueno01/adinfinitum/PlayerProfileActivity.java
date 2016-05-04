package com.example.cbueno01.adinfinitum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

/**
 * Created by cbueno01 on 3/29/16.
 */
public class PlayerProfileActivity extends Activity {

    private static final String TAG = "Player Profile";
    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_REQUEST = 1313;
    private static final int DIALOG = 0;

    private String mPlayerName;
    private long mHighScore;
    private List<Long> mHighScores;
    private String mImagePath;

    private String mLongestGame;
    private String mMostRounds;
    private String mTotalTime;

    // to restore scores
    private SharedPreferences mPrefs;

    private String [] highScores;
    private Dialog mDialog;

    private TextView mNameTextView;
    private TextView mHighScoreTextView;
    private ImageView mImageView;
    private ImageView mCameraImageView;
    private TextView mTitleTextView;
    private ListView mListView;
    private EditText mEditText;

    private Button mResetButton;
    private TextView mTotalTimeTV;
    private TextView mLongestGameTV;
    private TextView mMostRoundsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      Log.d("Ad Infinitum", "In player profile activity");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_player_profile_screen);

//        getPreferenceManager().setSharedPreferencesName("preferences");
//        addPreferencesFromResource(R.xml.preferences);
        mPrefs = getSharedPreferences("profile", MODE_PRIVATE);
//        mProfs = getSharedPreferences("")

//        highScores = getResources().getStringArray(R.array.high_scores);

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

    @Override
    protected void onPrepareDialog(int id, Dialog dialog)
    {
        switch (id)
        {
            case DIALOG:
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                dialog = createHighScoresDialog(builder);
//                dialog.setTitle("Custom dialog 1");
//
//                EditText cdField1Edit = (EditText)dialog.findViewById(R.id.cdField1Edit);
//                EditText cdField2Edit = (EditText)dialog.findViewById(R.id.cdField2Edit);
//
//                // Set the default values for Field 1 and 2
//                cdField1Edit.setText("");
//                cdField2Edit.setText(cdField2DefaultValue != null ? cdField2DefaultValue : "");
            }
            break;
        }
    }

    private Dialog createHighScoresDialog(AlertDialog.Builder builder) {
        String hs = mPrefs.getString("pref_high_scores", getResources().getString(R.string.default_high_scores));

        Log.d("Profile", "*^&*%%($(%)#(%)#$#%*)$*)&@$(@&#)#$%)(&%(#)@$               " + hs);
        String[] scores = hs.split(",");
//        ListView lv = (ListView) findViewById(R.id.high_scores_dialog_list);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.listview_item, scores);



//        ListView listView = (ListView) findViewById(R.id.high_scores_dialog_list);
//        mListView.setAdapter(itemsAdapter);
        Context context = getApplicationContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.highscores_dialog, null);
//        mListView = inflater.inflate(R.layout.highscores_dialog, null);

        mListView = (ListView) layout.findViewById(R.id.high_scores_dialog_list);
        mListView.setAdapter(itemsAdapter);

        builder.setView(layout);
        builder.setTitle(R.string.high_scores_title);


        builder.setPositiveButton("Okay", null);
        return builder.create();
    }


    public void setTextViews() {
//        mEditText = (EditText) findViewById(R.id.player_name);
        mHighScoreTextView = (TextView) findViewById(R.id.high_score_list);
        mImageView = (ImageView) findViewById(R.id.player_image);
        mTitleTextView = (TextView) findViewById(R.id.profile_title);

        mTotalTimeTV = (TextView) findViewById(R.id.default_total_time_played);
        mLongestGameTV = (TextView) findViewById(R.id.default_longest_game_played);
        mMostRoundsTV = (TextView) findViewById(R.id.default_most_rounds_beaten);

        mResetButton = (Button) findViewById(R.id.reset_button);

        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        Animation blinkAnimation2 = AnimationUtils.loadAnimation(this, R.anim.blink2);
        mTitleTextView.startAnimation(blinkAnimation);
//        mNameTextView.startAnimation(blinkAnimation2);
//        mHighScoreTextView.startAnimation(blinkAnimation2);



        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
        mHighScore = mPrefs.getLong("pref_high_score", 0);

        mImagePath = mPrefs.getString("pref_reset_picture", null);


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
                mImageView.setImageBitmap(bmp);
        }

    }

    public void resetScore(View v) {
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString("pref_high_scores", getString(R.string.default_high_scores));
//        ed.putString("pref_total_time", getString(R.string.default_playtime));
        ed.putString("pref_longest_game", getString(R.string.default_longest_game));
        ed.putString("pref_most_rounds", getString(R.string.default_most_rounds));

        ed.apply();

//        mTotalTimeTV.setText(mPrefs.getString("pref_total_time", "0"));
        mLongestGameTV.setText(mPrefs.getString("pref_longest_game", "0"));
        mMostRoundsTV.setText(mPrefs.getString("pref_most_rounds", "0"));

//        removeDialog(mDialog);

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        createHighScoresDialog(builder);

//        String hs = mPrefs.getString("pref_high_scores", getResources().getString(R.string.default_high_scores));
//        String[] scores = hs.split(",");
//
//        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.listview_item, scores);
//
//        Context context = getApplicationContext();
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.highscores_dialog, null);
//
//        mListView = (ListView) layout.findViewById(R.id.high_scores_dialog_list);
//        mListView.setAdapter(itemsAdapter);

    }

    public void getPicture(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void takePicture(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
//                mImageView.setImageURI(selectedImageUri);
                String selectedImagePath = getPath(selectedImageUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                Bitmap bmp = BitmapFactory.decodeFile(selectedImagePath, options);
                mImageView.setImageBitmap(bmp);

                SharedPreferences.Editor ed = mPrefs.edit();
                ed.putString("pref_reset_picture", selectedImagePath);
                ed.apply();
            } else if (requestCode == CAMERA_REQUEST) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                mImageView.setImageBitmap(bmp);

                SharedPreferences.Editor ed = mPrefs.edit();
                ed.putString("pref_reset_picture", getPath(data.getData()));
                ed.apply();
            }
        }
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
}
