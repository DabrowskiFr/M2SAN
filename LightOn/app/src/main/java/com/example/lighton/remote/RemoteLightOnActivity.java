package com.example.lighton.remote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.lighton.R;
import com.example.lighton.databinding.ActivityRemoteLightOnBinding;
import com.example.lighton.local.LocalLightOnActivityViewModel;
import com.example.lighton.local.LocalLightOnService;


public class RemoteLightOnActivity extends AppCompatActivity implements View.OnClickListener{

    private RemoteLightOnActivityViewModel viewModel;
    private ActivityRemoteLightOnBinding binding;
    private ImageView imageView;
    private Boolean bound = false;
    private Messenger messenger = null;
    private final Messenger recvMessenger = new Messenger(new InHandler());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRemoteLightOnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RemoteLightOnActivityViewModel.class);
        imageView = binding.imageView;
        viewModel.init();
        viewModel.getState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    imageView.setImageResource(R.drawable.green_light_icon);
                } else {
                    imageView.setImageResource(R.drawable.red_light_icon);
                }
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Intent intent = new Intent(this, RemoteLightOnService.class);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    public void onStop(){
        super.onStop();
        if (bound) unbindService(mConn);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // if the service is unexpectedly disconnected
            messenger = null;
            bound = false;
        }
    };

    class InHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case RemoteLightOnService.MSG_RETURN_STATE :
                    Boolean b = (Boolean) msg.obj;
                    viewModel.set(b);
                    break;
                default :
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (bound) {
            try {
                Message msg = Message.obtain(null, RemoteLightOnService.MSG_SWAP);
                msg.replyTo = recvMessenger;
                messenger.send(msg);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }
}