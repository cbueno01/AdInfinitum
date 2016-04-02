package com.example.cbueno01.adinfinitum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by cbueno01 on 4/1/16.
 */
public class GameView extends View {

//    private Paint mPaint;
    private AdInfinitumGame mGame;

    public GameView(Context context) {
        super(context);
//        initialize();
    }


    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        initialize();
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initialize();
    }

//    public void initialize() {
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

//    }

    public void setGame(AdInfinitumGame game) {
        mGame = game;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ArrayList<Ad> adList = mGame.getActiveAds();

        for(Ad ad : adList) {
//            int picId = getResources().getIdentifier(ad.getFileName(), "drawable", getContext().getPackageName());
            Bitmap img = ad.getBitmap();
//            if(img == null)
//            Log.d("Ad Infinitum", "x: " + ad.getX() + " y: " + ad.getY() + " width: " + ad.getWidth() + " height: " + ad.getHeight());

            Rect mImageRect = new Rect(ad.getX(), ad.getY(), (ad.getX() + ad.getWidth()), (ad.getY() + ad.getHeight()));

            if (mGame != null)
            {
                canvas.drawBitmap(img, null, mImageRect, null);
            }
        }

    }
}
