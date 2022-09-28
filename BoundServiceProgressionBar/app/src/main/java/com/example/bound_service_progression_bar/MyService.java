package com.example.bound_service_progression_bar;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    private static final String TAG = "MyService";

    private final IBinder mBinder = new MyBinder();
    private ScheduledExecutorService backgroundExecutor;
    private int mProgress, mMaxValue;
    private Boolean mIsPaused;

    @Override
    public void onCreate() {
        super.onCreate();
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor();
        mProgress = 0;
        mIsPaused = true;
        mMaxValue = 5000;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class MyBinder extends Binder {

        MyService getService(){
            return MyService.this;
        }

    }

    public Boolean getIsPaused(){
        return mIsPaused;
    }

    public int getProgress(){
        return mProgress;
    }

    public int getMaxValue(){
        return mMaxValue;
    }

    public void pausePretendLongRunningTask(){
        mIsPaused = true;
    }

    public void unPausePretendLongRunningTask(){
        mIsPaused = false;
        startPretendLongRunningTask();
    }

    public void startPretendLongRunningTask(){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(mProgress >= mMaxValue || mIsPaused){
                    Log.d(TAG, "run: removing callbacks");
                    pausePretendLongRunningTask();
                }
                else{
                    Log.d(TAG, "run: progress: " + mProgress);
                    mProgress += 100; // increment the progress
                    backgroundExecutor.schedule(this, 100, TimeUnit.MILLISECONDS);
                }
            }
        };
        backgroundExecutor.execute(runnable);
    }

    public void resetTask(){
        mProgress = 0;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved: called.");
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called.");
    }

}