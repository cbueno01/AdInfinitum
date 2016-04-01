package com.example.cbueno01.adinfinitum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cbueno01 on 3/29/16.
 */
public class PlayerProfileActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;

    private String mPlayerName;
    private int mHighScore;

    // to restore scores
    private SharedPreferences mPrefs;

    private TextView mNameTextView;
    private TextView mHighScoreTextView;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      Log.d("Ad Infinitum", "In player profile activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile_screen);

         mPrefs = getSharedPreferences("preferences", MODE_PRIVATE);

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

    public void setTextViews() {
        mNameTextView = (TextView) findViewById(R.id.player_name);
        mHighScoreTextView = (TextView) findViewById(R.id.high_score);
        mImageView = (ImageView) findViewById(R.id.player_image);
    }

    public void readData() {
        mPlayerName =  mPrefs.getString("pref_reset_profile", "Anonymous");
        mHighScore = mPrefs.getInt("mHighScore", 0);
    }

    public void displayViews() {
        mNameTextView.setText("Name: " + mPlayerName);
        mHighScoreTextView.setText("High Score: " + mHighScore);
    }

    public void getPicture(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
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
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
