package com.example.cbueno01.adinfinitum;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;

import java.util.Random;

public class GradientGenerator {
    private Random mRandom = new Random();
    private Context mContext;
    private Point mGradientSize;

    GradientGenerator(Context context, Point gradientSize) {
        // Set the context of application
        this.mContext = context;
        // Set the gradient size (width and height)
        this.mGradientSize = gradientSize;
    }

    // Custom method to generate random GradientDrawable array
    protected GradientDrawable[] getRandomGradientDrawableArray(int length) {
        GradientDrawable[] gradients = new GradientDrawable[length];
        for (int i = 0; i < length; i++) {
            gradients[i] = getRandomGradientDrawable();
        }
        return gradients;
    }

    // Custom method to generate random GradientDrawable
    protected GradientDrawable getRandomGradientDrawable() {
        GradientDrawable gradient = new GradientDrawable();

        /*
            public void setOrientation (GradientDrawable.Orientation orientation)
                Changes the orientation of the gradient defined in this drawable.

                Note: changing orientation will affect all instances of a drawable loaded from a
                    resource. It is recommended to invoke mutate() before changing the orientation.

                Parameters
                    orientation : The desired orientation (angle) of the gradient
        */
        // Set the gradient orientation
        gradient.setOrientation(getRandomOrientation());

        /*
            public void setColors (int[] colors)
                Sets the colors used to draw the gradient.
                Each color is specified as an ARGB integer and the array must contain at least 2 colors.

                Parameters
                    colors : an array containing 2 or more ARGB colors
        */
        // Set the gradient colors
        gradient.setColors(getRandomColorArray());

        // The type of the gradient: LINEAR_GRADIENT, RADIAL_GRADIENT or SWEEP_GRADIENT
        gradient = setRandomGradientType(gradient);

        /*
            public void setSize (int width, int height)
                Sets the size of the shape drawn by this drawable.

                Parameters
                width : The width of the shape used by this drawable
                height : The height of the shape used by this drawable
        */
        // Set the gradient size (width and height)
        gradient.setSize(mGradientSize.x, mGradientSize.y);

        // Return the GradientDrawable
        return gradient;
    }

    // Custom method to get a random GradientDrawable GradientType
    protected GradientDrawable setRandomGradientType(GradientDrawable gradient) {
        /*
            public void setGradientType (int gradient)
                Sets the type of gradient used by this drawable.

                Parameters
                    gradient : The type of the gradient: LINEAR_GRADIENT, RADIAL_GRADIENT or SWEEP_GRADIENT
        */
        /*
            Get a random number from 0,1, and 2
            We Uses the random numbers as this way
            0 = LINEAR_GRADIENT
            1 = RADIAL_GRADIENT
            2 = SWEEP_GRADIENT
         */
        int randomNumber = mRandom.nextInt(3);
        if (randomNumber == 0) {
            gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        } else if (randomNumber == 1) {
            gradient.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        } else {
            gradient.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        }

        // Set the gradient center for GradientDrawable
        if (randomNumber != 0) {
            /*
                Sets the center location of the gradient. The radius is honored only when the
                gradient type is set to RADIAL_GRADIENT or SWEEP_GRADIENT.

                Gradient center honored a float value between 0 to 1
             */
            //Random.nextFloat() * (x - y) + x
            gradient.setGradientCenter(
                    mRandom.nextFloat() * (1 - 0) + 0,
                    mRandom.nextFloat() * (1 - 0) + 0)
            ;
        }

        // Sets the radius of the gradient.
        if (randomNumber == 1) {
            /*
                public void setGradientRadius (float gradientRadius)
                    Sets the radius of the gradient. The radius is honored only when the gradient type is set to RADIAL_GRADIENT.

                    Parameters
                        gradientRadius : The radius of the gradient in pixels
            */
            gradient.setGradientRadius(mRandom.nextInt(mGradientSize.x));
        }

        // Return the GradientDrawable where gradient type assigned
        return gradient;
    }

    // Custom method to generate a color array of random colors
    protected int[] getRandomColorArray() {
        // Get random number between minimum (inclusive) to maximum (exclusive)
        int numberOfColors = mRandom.nextInt(16 - 3) + 3;
        int[] colors = new int[numberOfColors];
        for (int i = 0; i < numberOfColors; i++) {
            colors[i] = getRandomColor();
        }
        // Return the color array, number of elements between 3 to 15
        return colors;
    }

    // Get a random gradient orientation
    protected GradientDrawable.Orientation getRandomOrientation() {
        // Initialize a new array of GradientDrawable Orientation
        GradientDrawable.Orientation[] orientations = new GradientDrawable.Orientation[]{
                // BL_TR : draw the gradient from the bottom-left to the top-right
                GradientDrawable.Orientation.BL_TR,
                // BOTTOM_TOP : draw the gradient from the bottom to the top
                GradientDrawable.Orientation.BOTTOM_TOP,
                // BR_TL : draw the gradient from the bottom-right to the top-left
                GradientDrawable.Orientation.BR_TL,
                // LEFT_RIGHT : draw the gradient from the left to the right
                GradientDrawable.Orientation.LEFT_RIGHT,
                // RIGHT_LEFT : draw the gradient from the right to the left
                GradientDrawable.Orientation.RIGHT_LEFT,
                // TL_BR : draw the gradient from the top-left to the bottom-right
                GradientDrawable.Orientation.TL_BR,
                // TOP_BOTTOM : draw the gradient from the top to the bottom
                GradientDrawable.Orientation.TOP_BOTTOM,
                // TR_BL : draw the gradient from the top-right to the bottom-left
                GradientDrawable.Orientation.TR_BL
        };
        // Return a random Orientation
        GradientDrawable.Orientation orientation = orientations[mRandom.nextInt(orientations.length)];
        return orientation;
    }

    // Custom method to generate a random color
    protected int getRandomColor() {
        // 256 is excluded, so random number is between 0 to 255
        int red = mRandom.nextInt(150);
//        int green = red;
//        int blue = red;
        int green = mRandom.nextInt(150);
        int blue = mRandom.nextInt(150);
        int color = Color.argb(255, red, green, blue);
        // Return the random argb color
        return color;
    }
}