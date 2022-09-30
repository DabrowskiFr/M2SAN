package com.example.lighton.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class RemoteLightOnService extends Service {

    static final int MSG_SWAP = 0;
    static final int MSG_RETURN_STATE = 1;
    public boolean state = false;


    final Messenger messenger = new Messenger(new InHandler());

    public RemoteLightOnService() {
    }

    class InHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_SWAP :
                    try {
                        state = !state;
                        Message rep = Message.obtain(null, MSG_RETURN_STATE, new Boolean(state));
                        msg.replyTo.send(rep);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default :
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}