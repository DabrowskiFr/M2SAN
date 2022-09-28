package com.example.bound_service_progression_bar;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "MainActivityViewModel";

    private MutableLiveData<Boolean> mIsProgressBarUpdating = new MutableLiveData<>();

    MyService mService;

    // Keeping this in here because it doesn't require a context
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            Log.d(TAG, "ServiceConnection: connected to service.");
            MyService.MyBinder binder = (MyService.MyBinder) iBinder;
            mService = (MyService) binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "ServiceConnection: disconnected from service.");
            mService = null;
        }
    };


    public ServiceConnection getServiceConnection(){
        return serviceConnection;
    }

    //public LiveData<MyService.MyBinder> getBinder(){
    //    return mBinder;
    //}


    public LiveData<Boolean> getIsProgressBarUpdating(){
        return mIsProgressBarUpdating;
    }

    public void setIsProgressBarUpdating(boolean isUpdating){
        mIsProgressBarUpdating.postValue(isUpdating);
    }

    public void toggleUpdates(){

        if(mService != null){
            if(mService.getProgress() == mService.getMaxValue()){
                mService.resetTask();
                //mButton.setText("Start");
            }
            else{
                if(mService.getIsPaused()){
                    mService.unPausePretendLongRunningTask();
                    mIsProgressBarUpdating.postValue(true);
                }
                else{
                    mService.pausePretendLongRunningTask();
                    mIsProgressBarUpdating.postValue(false);
                }
            }

        }
    }

    public int getProgress(){
        return mService.getProgress();
    }

    public int getMaxValue(){
        return mService.getMaxValue();
    }

}
