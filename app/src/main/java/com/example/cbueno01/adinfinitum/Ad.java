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
        this.width = w;
        this.height = h;
        this.x = _x;
        this.y = _y;
        this.imageID = ID;
        this.bmp = b;
        this.scaleFactor = scale;
        this.pointage = 1;
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() { return Math.round(this.width * scaleFactor); }

    public int getHeight() {
        return Math.round(this.height * scaleFactor);
    }

    public long getPointage() { return this.pointage; }

    public int getImageID() { return this.imageID; }

    public Bitmap getBitmap() { return this.bmp; }
}