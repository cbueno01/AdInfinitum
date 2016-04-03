package com.example.cbueno01.adinfinitum;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.util.Random;

/**
 * Created by Squash on 4/1/2016.
 */
public class Ad {

    // field for associated image
    private long pointage;
    private int x;
    private int y;
    private int width;
    private int height;
//    private float scaleFactor;
    private int imageID;
    private Bitmap bmp;
    private int rightX;
    private int bottomY;


    //CONSTRUCTOR
    public Ad(int ID, Bitmap b, int w, int h, int _x, int _y, long points) {
        width = w;
        height = h;
        x = _x;
        y = _y;
        imageID = ID;
        bmp = b;
//        scaleFactor = scale;
        pointage = 1;
        rightX = x + width;
        bottomY = y + height;
        pointage = points;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public long getPointage() { return pointage; }

    public int getImageID() { return imageID; }

    public Bitmap getBitmap() { return bmp; }

    public boolean isPointInAd(int _x, int _y) { return (_x >= x && _x <= rightX) && (_y >= y && _y <= bottomY); }
}