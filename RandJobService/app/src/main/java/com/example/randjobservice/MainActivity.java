package com.example.randjobservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.randjobservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    private Button buttonStart, buttonStop;

    private final int serviceId = 100;

    JobScheduler jobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        buttonStart = binding.button1;
        buttonStop = binding.button2;
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == buttonStart.getId()) {
            ComponentName componentName = new ComponentName(this, RandJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(serviceId, componentName);
            builder.setRequiresCharging(true);
            builder.setPersisted(true);
            JobInfo jobInfo = builder.build();

            if (jobScheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS){
                Log.i("RandJobService", "job schedule succeeded");
            } else {
                Log.i("RandJobService", "job schedule failed");
            }
        } else if (view.getId() == buttonStop.getId()){
            jobScheduler.cancel(serviceId);
        }
    }
}