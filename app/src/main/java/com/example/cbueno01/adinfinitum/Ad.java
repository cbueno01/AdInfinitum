package com.example.cbueno01.adinfinitum;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

import java.util.Random;

/**
 * Created by Squash on 4/1/2016.
 */
public class Ad {

    // field for associated image
    private long pointage;
    private Point topLeft;
    private Point bottomRight;
    private Point boxTopLeft;
    private Point boxBottomRight;
    private int width;
    private int height;
//    private float scaleFactor;
    private int imageID;
    private Bitmap bmp;



    //CONSTRUCTOR
    public Ad(int ID, Bitmap b, int w, int h, Point p, Point b1, Point b2, long points) {
        width = w;
        height = h;
        topLeft = p;
        bottomRight = new Point(topLeft.x + width, topLeft.y + height);
        boxTopLeft = b1;
        boxBottomRight = b2;
        imageID = ID;
        bmp = b;
//        scaleFactor = scale;
        pointage = 1;
        pointage = points;
    }


    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public Point getBoxTopLeft() {
        return boxTopLeft;
    }

    public Point getBoxBottomRight() {
        return boxBottomRight;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public long getPointage() { return pointage; }

    public int getImageID() { return imageID; }

    public Bitmap getBitmap() { return bmp; }

    public boolean isPointInAd(int _x, int _y) { return (_x >= boxTopLeft.x && _x <= boxBottomRight.x) && (_y >= boxTopLeft.y && _y <= boxBottomRight.y); }
}