package com.example.cbueno01.adinfinitum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by cbueno01 on 4/1/16.
 */
public class GameView extends View {

    private Paint mPaint;
    private AdInfinitumGame mGame;

    public GameView(Context context) {
        super(context);
        initialize();
    }


    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void initialize() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    public void setGame(AdInfinitumGame game) {
        mGame = game;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setARGB(100, 0,0,0);
        ArrayList<Ad> adList = mGame.getActiveAds();

        for(Ad ad : adList) {
            Bitmap img = ad.getBitmap();
            Point topLeft = ad.getTopLeft();
            Point bottomRight = ad.getBottomRight();
            Rect mImageRect = new Rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);

            Point boxTopLeft = ad.getBoxTopLeft();
            Point boxBottomRight = ad.getBoxBottomRight();

            if (mGame != null) {
                canvas.drawBitmap(img, null, mImageRect, null);
                canvas.drawRect(boxTopLeft.x, boxTopLeft.y, boxBottomRight.x, boxBottomRight.y, mPaint);
            }
        }

    }
}
