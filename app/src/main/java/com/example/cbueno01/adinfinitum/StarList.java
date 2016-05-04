package com.example.cbueno01.adinfinitum;

import android.graphics.Point;

import java.util.*;

/**
 * Created by Squash on 4/27/2016.
 */
public class StarList {

    private final static int INNER_RADIUS = 150;
    private final static int OUTER_RADIUS = 300;
    private final static int MIDDLE_RADIUS = 200;

    public ArrayList<Star> mStarList = new ArrayList<Star>();
    public Random mRand;
    public int mScreenWidth;
    public int mScreenHeight;

    class Star
    {
        private Point p;
        private int r;

        public Star(Point _p)
        {
            this.p = _p;
            this.r = 5;
        }

        public Point getPoint() {
            return this.p;
        }

        public int getRadius() {
            return this.r;
        }

        public void setPoint(Point _p)
        {
            this.p = _p;
        }

        public void setRadius(int _r) { this.r = _r; }

    }
    public StarList(int numStars, int screenWidth, int screenHeight) {
        mRand = new Random();
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        for(int i = 0; i < numStars; i++)
        {
            Star s = new Star(generateStar());
            mStarList.add(s);
        }

    }

    public Point generateStar()
    {
        Point center = new Point(mScreenWidth / 2, mScreenHeight / 2);
        boolean notFound = true;
        int x = 0;
        int y = 0;

        while(notFound)
        {

            x = mRand.nextInt(OUTER_RADIUS) * (mRand.nextInt(2) * 2 - 1);
            y = mRand.nextInt(MIDDLE_RADIUS) * (mRand.nextInt(2) * 2 - 1);
            int distance = (int)Math.sqrt((x)*(x) + (y)*(y));

            if (distance > INNER_RADIUS) {
                notFound = false;
            }
        }


        return new Point(x + center.x, y + center.y);
    }
//
//    public void setX(float x) {
//        this.x = x;
//    }
//
//    public float getY() {
//        return this.y;
//    }
//
//    public void setY(float y) {
//        this.y = y;
//    }

//    public int getWidth() {
//        return this.width;
//    }
//
//    public void setWidth(int w) {
//        this.width = w;
//    }

//    public int getHeight() {
//        return this.height;
//    }
//
//    public void setHeight(int h) {
//        this.height = h;
//    }
}
