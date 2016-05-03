package com.example.cbueno01.adinfinitum;

/**
 * Created by Squash on 4/27/2016.
 */
public class Star {

    private float x;
    private float y;
//    int width;
    int height;

    public Star (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

//    public int getWidth() {
//        return this.width;
//    }
//
//    public void setWidth(int w) {
//        this.width = w;
//    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int h) {
        this.height = h;
    }
}
