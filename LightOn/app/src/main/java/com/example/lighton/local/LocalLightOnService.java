package com.example.lighton.local;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocalLightOnService extends Service {

    private boolean state = false;

    public class LightOnBinder extends Binder {
        public LocalLightOnService getService(){
            return LocalLightOnService.this;
        }
    }

    private final IBinder binder = new LightOnBinder();

    public LocalLightOnService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public boolean swap(){
        state = ! state;
        return state;
    }
}