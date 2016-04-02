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
    private float scaleFactor;
    private int imageID;
    private Bitmap bmp;


    //CONSTRUCTOR
    public Ad(int ID, Bitmap b, int w, int h, int _x, int _y, float scale) {
        width = w;
        height = h;
        x = _x;
        y = _y;
        imageID = ID;
        bmp = b;
        scaleFactor = scale;
        pointage = 1;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() { return Math.round(width * scaleFactor); }

    public int getHeight() {
        return Math.round(height * scaleFactor);
    }

    public long getPointage() { return pointage; }

    public int getImageID() { return imageID; }

    public Bitmap getBitmap() { return bmp; }
}