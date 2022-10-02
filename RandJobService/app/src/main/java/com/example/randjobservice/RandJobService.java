package com.example.randjobservice;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class RandJobService extends JobService {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn = false;
    private final int MIN = 0;
    private final int MAX = 100;

    public RandJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        doBackgroundWork();
        return true;
    }

    private void doBackgroundWork(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIsRandomGeneratorOn = true;
                while (mIsRandomGeneratorOn){
                    try {
                        Thread.sleep(1000);
                        mRandomNumber = new Random().nextInt(MAX)+MIN;
                        Log.i("RandJobService", "Random number :" + mRandomNumber);

                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("RandJobService", "onStopJob");
        mIsRandomGeneratorOn = false;
        return true;
    }

    private void startRandomNumberGenerator(){

    }
}