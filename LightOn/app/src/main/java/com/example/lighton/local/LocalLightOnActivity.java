package com.example.lighton.local;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.lighton.R;
import com.example.lighton.databinding.ActivityLocalLightOnBinding;

public class LocalLightOnActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean bound = false;
    private LocalLightOnService service;
    private LocalLightOnActivityViewModel viewModel;
    private ActivityLocalLightOnBinding binding;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocalLightOnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LocalLightOnActivityViewModel.class);
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
        Intent intent = new Intent(this, LocalLightOnService.class);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    public void onStop(){
        super.onStop();
        if (bound) unbindService(mConn);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalLightOnService.LightOnBinder binder = (LocalLightOnService.LightOnBinder) iBinder;
            service = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // if the service is unexpectedly disconnected
            service = null;
            bound = false;
        }
    };

    @Override
    public void onClick(View view) {
        if (bound) {
            viewModel.set(service.swap());
        }

    }
}