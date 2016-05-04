package com.example.cbueno01.adinfinitum;

import android.graphics.Point;

import java.util.*;

/**
 * Created by Squash on 4/27/2016.
 */
public class StarList {

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
        int radius = 175;
        boolean notFound = true;
        int x = 0;
        int y = 0;
        while(notFound)
        {
            x = mRand.nextInt(mScreenWidth);
            y = mRand.nextInt(mScreenHeight);
            int distance = (int)Math.sqrt((x-center.x)*(x-center.x) + (y-center.y)*(y-center.y));

            if (distance > radius)
                notFound = false;
        }


        return new Point(x, y);
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
