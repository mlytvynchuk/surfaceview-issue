package com.maxlytvynchuk.dinogame;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private GameActivity activity;
    SurfaceHolder surfaceHolder;
    Thread thread = null;
    volatile boolean running = false;

    public GameView(Context context) {
        super(context);
        activity = (GameActivity) context;
        surfaceHolder = getHolder();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        // All the time have not valid message
        if(surfaceHolder.getSurface().isValid()) {
            Log.v("MESSAGE", "VALID");
        }else{
            Log.v("MESSAGE", "NOT VALID");
        }
    }

}
