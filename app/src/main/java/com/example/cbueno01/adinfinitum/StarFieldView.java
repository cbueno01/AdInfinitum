package com.example.cbueno01.adinfinitum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Squash on 4/27/2016.
 */
public class StarFieldView extends View{

    Paint starPaint;
    Thread ticker;
    Rect viewBounds;
    boolean ready = true;
    boolean closing = false;
    int numberOfStars = 25;
    int varianceDiameter;
    int initialSkip;
    long refreshTime;
    List<Star> stars = new ArrayList<Star>();

//    public StarFieldView(Context context, AttributeSet attrs) {
    public StarFieldView(Context context, Rect r) {
        super(context);
        viewBounds = new Rect(r);
        init();
    }

    public void init() {
        starPaint = new Paint();
        starPaint.setColor(Color.BLACK);


        ticker = new Thread(new Runnable() {
            public void run() {
                synchronized (viewBounds) {
                    try {
                        while (viewBounds.isEmpty())
                            viewBounds.wait();
                    } catch (InterruptedException ie) {
                    }
                }

                for (int i = 0; i < numberOfStars; ++i) {
                    float x = (float) ((Math.random() - .5) * varianceDiameter), y = (float) ((Math.random() - .5) * varianceDiameter);
                    int height = 2;
                    stars.add(new Star(x + viewBounds.centerX(), y + viewBounds.centerY()));
                }

                //stars are ready to be drawn now
                ready = true;

                //this is just to avoid the initial "explosions"
                for (int i = 0; i < initialSkip; ++i) {
                    tick();
                }

                while (!closing) {
                    long time = System.nanoTime();
                    //tick
                    tick();
                    postInvalidate();
                    //wait for remaining amount of time
                    try {
                        long sleepTime = refreshTime - (System.nanoTime() - time) / 1000000;
                        if (sleepTime > 0) {
                            Thread.sleep(sleepTime);
                        }
                    } catch (InterruptedException ie) {
                    }
                }
            }
        });
        ticker.start();
    }

    public void tick() {
        for (int i = 0; i < numberOfStars; ++i) {
            Star current = stars.get(i);

            // divides each half of the screen into eighths
            float newX = viewBounds.centerX() - current.getX() + viewBounds.width() / 16;
            float newY = viewBounds.centerY() - current.getY() + viewBounds.height() / 16;
            if (newX < 0 || newX > viewBounds.width() || newY < 0 || newY > viewBounds.width()) {
                current.setX(newX);
                current.setY(newY);
            } else {
                float resetX = (float) ((Math.random() - .5) * varianceDiameter), resetY = (float) ((Math.random() - .5) * varianceDiameter);
                current.setX(resetX);
                current.setY(resetY);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        starPaint.setARGB(100, 0, 0, 0);
//        ArrayList<Star> StarPaint = ;


        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inScaled = false;

        Random rand = new Random();
//        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), imageID.get(index), dimensions);


        for (Star s : stars) {
//            Bitmap img = s.getBitmap();
            float centerX = s.getX();
            float centerY = s.getY();
            Point mImageRect = new Point((int) s.getX(),(int) s.getY());

//            Point boxTopLeft = ad.getBoxTopLeft();
//            Point boxBottomRight = ad.getBoxBottomRight();

            int radius = s.getHeight();


//            canvas.dra
//            canvas.drawBitmap(img, null, mImageRect, null);
            canvas.drawPoint(centerX, centerY, starPaint);

        }

    }
}
