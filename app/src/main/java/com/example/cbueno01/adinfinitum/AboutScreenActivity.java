package com.example.cbueno01.adinfinitum;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Squash on 3/30/2016.
 */
public class AboutScreenActivity extends Activity {
    private Context mContext;
    private RelativeLayout mRL;
    private LinearLayout mLL;

    private Handler mHandler;
    private Runnable mRunnable;
    private int mInterval = 4000;
    private Point mScreenSize;

    private TransitionDrawable drawable;;
    private GradientGenerator mGradientGenerator;
    private GradientDrawable mStartGradient;
    private GradientDrawable mEndGradient;

    private TextView tvAboutTitle;
    private TextView tvHowTo;
    private TextView tvCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AD INFINITUM", "In About activity");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_screen);

        tvAboutTitle = (TextView) findViewById(R.id.about_title);
        tvHowTo = (TextView) findViewById(R.id.how_to_play);
        tvCredits = (TextView) findViewById(R.id.credits);


        // Get the application context
        mContext = getApplicationContext();

        // Get the widgets reference from XML layout
        mRL = (RelativeLayout) findViewById(R.id.about_rl);
//        mLL = (LinearLayout) findViewById(R.id.about_linear_layout);
        //ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle);

        // Get the screen size in pixels
        mScreenSize = getScreenSize();


        // Initialize a new Handler
        mHandler = new Handler();

        // Initialize a new instance of GradientGenerator class
        mGradientGenerator = new GradientGenerator(mContext, mScreenSize);

        // Generate a random GradientDrawable
        mStartGradient = mGradientGenerator.getRandomGradientDrawable();

        // Set the RelativeLayout background
        mRL.setBackground(mStartGradient);

        // Initialize a new Runnable
        mRunnable = new Runnable() {
            /*
                public abstract void run ()
                    Starts executing the active part of the class' code. This method is
                    called when a thread is started that has been created with a class which
                    implements Runnable.
            */
            @Override
            public void run() {
                // Do a task after an interval
                doTask();
            }
        };
        // Play animation immediately after button click
        mHandler.postDelayed(mRunnable, 100);

    }

    public void onClick(View v) {
        v.setVisibility(View.GONE);
        v.setClickable(false);
    }

    // Custom method to do a task
    protected void doTask(){
        // If the animation already running
        if(mEndGradient != null){
            /*
                To make a continuous animation we get the last animation ending GradientDrawable
                and set it as next animation starting GradientDrawable
            */
            mStartGradient = mEndGradient;
        }

        /*
            An extension of LayerDrawables that is intended to cross-fade between the first
            and second layer. So, TransitionDrawable support only two layers.
        */
        // Generate an array of GradientDrawable objects
        GradientDrawable[] mGradientDrawableArray = new GradientDrawable[]{
                mStartGradient,
                mGradientGenerator.getRandomGradientDrawable()
        };

        // Get and store the last element from GradientDrawable array
        mEndGradient = mGradientDrawableArray[mGradientDrawableArray.length-1];

        // Initialize a new TransitionDrawable using ColorDrawable array
        drawable = new TransitionDrawable(mGradientDrawableArray);

        // Set the animation duration
        drawable.startTransition(mInterval);

        // Set the TransitionDrawable as RelativeLayout background drawable
        mRL.setBackground(drawable);

        /*
            public final boolean postDelayed (Runnable r, long delayMillis)
                Causes the Runnable r to be added to the message queue, to be run after the specified
                amount of time elapses. The runnable will be run on the thread to which this handler
                is attached. The time-base is uptimeMillis(). Time spent in deep sleep will add an
                additional delay to execution.
        */
        // Schedule the next animation
        mHandler.postDelayed(mRunnable,mInterval);
    }

    // Custom method to get screen size in pixels
    protected Point getScreenSize(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        Point size = new Point(dm.widthPixels,dm.heightPixels);
        return size;
    }
}
