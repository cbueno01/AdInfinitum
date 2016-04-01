package com.example.cbueno01.adinfinitum;

import java.util.Random;

/**
 * Created by Squash on 4/1/2016.
 */
public class Ad {

    private int x;
    private int y;
    private int width;
    private int height;

    /*
    close button fields

    private int closeX;
    private int closeY;
    private int closeWidth;
    private int closeHeight;

     */
    private long pointage;

    private Random mRandom;

    // field for associated image

    /*
    //CONSTRUCTOR
    public Ad(file image) {
        mRandom = new Random();
        this.x = mRandom;
        this.y = mRandom;
        this.width = get from image
        this.height = get from image
    }

    //COPY CONSTRUCTOR
    public Ad(Ad a) {


    }
    */

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}