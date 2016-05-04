package com.example.cbueno01.adinfinitum;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Squash on 4/27/2016.
 */
public class StarFieldView extends View {

    Paint starPaint;
//    Thread ticker;
    Rect viewBounds;

    StarList mSL;
//    int screenWidth;
//    int screenHeight;
//    boolean ready = true;
//    boolean closing = false;
//    int numberOfStars = 25;
//    int varianceDiameter;
//    int initialSkip;
//    long refreshTime;
//    List<StarList> stars = new ArrayList<StarList>();

//    public StarFieldView(Context context, AttributeSet attrs) {
    public StarFieldView(Context context) {
        super(context);
    }

    public StarFieldView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }


    public StarFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void initialize() {
        starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    public void setDataMembers (StarList s) {
//        screenWidth = sW;
//        screenHeight = sH;
        mSL = s;
    }

//    public void init() {
//        starPaint = new Paint();
//        starPaint.setColor(Color.BLACK);
//
//
//        ticker = new Thread(new Runnable() {
//            public void run() {
//                synchronized (viewBounds) {
//                    try {
//                        while (viewBounds.isEmpty())
//                            viewBounds.wait();
//                    } catch (InterruptedException ie) {
//                    }
//                }
//
//                for (int i = 0; i < numberOfStars; ++i) {
//                    float x = (float) ((Math.random() - .5) * varianceDiameter), y = (float) ((Math.random() - .5) * varianceDiameter);
//                    int height = 2;
//                    stars.add(new StarList(x + viewBounds.centerX(), y + viewBounds.centerY()));
//                }
//
//                //stars are ready to be drawn now
//                ready = true;
//
//                //this is just to avoid the initial "explosions"
//                for (int i = 0; i < initialSkip; ++i) {
//                    tick();
//                }
//
//                while (!closing) {
//                    long time = System.nanoTime();
//                    //tick
//                    tick();
//                    postInvalidate();
//                    //wait for remaining amount of time
//                    try {
//                        long sleepTime = refreshTime - (System.nanoTime() - time) / 1000000;
//                        if (sleepTime > 0) {
//                            Thread.sleep(sleepTime);
//                        }
//                    } catch (InterruptedException ie) {
//                    }
//                }
//            }
//        });
//        ticker.start();
//    }

//    public void tick() {
//        for (int i = 0; i < numberOfStars; ++i) {
//            StarList current = stars.get(i);
//
//            // divides each half of the screen into eighths
//            float newX = viewBounds.centerX() - current.getX() + viewBounds.width() / 16;
//            float newY = viewBounds.centerY() - current.getY() + viewBounds.height() / 16;
//            if (newX < 0 || newX > viewBounds.width() || newY < 0 || newY > viewBounds.width()) {
//                current.setX(newX);
//                current.setY(newY);
//            } else {
//                float resetX = (float) ((Math.random() - .5) * varianceDiameter), resetY = (float) ((Math.random() - .5) * varianceDiameter);
//                current.setX(resetX);
//                current.setY(resetY);
//            }
//        }
//    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        starPaint.setARGB(200, 255, 255, 255);
//        canvas.drawCircle(mSL.mScreenWidth / 2, mSL.mScreenHeight / 2, 200, starPaint);
//
//        starPaint.setARGB(150, 0, 0, 0);
//        canvas.drawCircle(mSL.mScreenWidth / 2, mSL.mScreenHeight / 2, 90, starPaint);
//
//        starPaint.setARGB(255, 0, 0, 0);
//        canvas.drawCircle(mSL.mScreenWidth / 2, mSL.mScreenHeight / 2, 50, starPaint);

        starPaint.setARGB(200, 200, 200, 200);
        for (StarList.Star s : mSL.mStarList) {
            Point p = s.getPoint();
            int radius = s.getRadius();

            canvas.drawCircle(p.x, p.y, radius, starPaint);

        }

    }
}
