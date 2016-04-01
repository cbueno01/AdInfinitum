package com.example.cbueno01.adinfinitum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

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

        int screenWidth = getWidth();
        int screenHight = getHeight();

//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int screenHeight = displaymetrics.heightPixels;
//        int screenWidth = displaymetrics.widthPixels;


    }
}
