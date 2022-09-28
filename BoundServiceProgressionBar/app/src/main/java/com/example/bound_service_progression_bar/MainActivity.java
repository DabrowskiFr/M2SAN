package com.example.bound_service_progression_bar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    // UI Components
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private Button mButton;


    // Vars
    //private MyService mService;
    private MainActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progresss_bar);
        mTextView = findViewById(R.id.text_view);
        mButton = findViewById(R.id.toggle_updates);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

//        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setObservers();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mViewModel.toggleUpdates();
            }
        });
    }

    private void setObservers(){

        mViewModel.getIsProgressBarUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean aBoolean) {
                final Handler handler = new Handler(Looper.getMainLooper());
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(mViewModel.getIsProgressBarUpdating().getValue()){
                                mProgressBar.setProgress(mViewModel.getProgress());
                                mProgressBar.setMax(mViewModel.getMaxValue());
                                String progress =
                                        String.valueOf(100 * mViewModel.getProgress() / mViewModel.getMaxValue()) + "%";
                                mTextView.setText(progress);

                            handler.postDelayed(this, 100);
                        }
                        else{
                            handler.removeCallbacks(this);
                        }
                    }
                };

                if(aBoolean){
                    mButton.setText("Pause");
                    handler.postDelayed(runnable, 100);

                }
                else{
                    if(mViewModel.getProgress() == mViewModel.getMaxValue()){
                        mButton.setText("Restart");
                    }
                    else{
                        mButton.setText("Start");
                    }
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //if(mViewModel.getBinder() != null){
        //    unbindService(mViewModel.getServiceConnection());
        //}
    }

    private void startService(){
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

        bindService();
    }

    private void bindService(){
        Intent serviceBindIntent =  new Intent(this, MyService.class);
        bindService(serviceBindIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

}