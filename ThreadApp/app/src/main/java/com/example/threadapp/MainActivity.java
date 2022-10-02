package com.example.threadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.threadapp.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.lang.reflect.Executable;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        textView = binding.textView;

    }

    class MyThread extends Thread {
        @Override
        public void run(){
            for (int i = 0; i < 1000; i++){
                Log.d("MyThread", "" + i);
                try{
                    textView.setText(""+i);
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < 1000; i++){
                Log.d("MyThread", "" + i);
                try{
                    publishProgress(i);
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... integers){
            textView.setText(""+integers[0]);
        }
    }
    int v;
    @Override
    public void onClick(View view) {
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    class MyJobService extends JobService {

        @Override
        public boolean onStartJob(JobParameters jobParameters) {
            // Some Work to do
            return true;
        }

        @Override
        public boolean onStopJob(JobParameters jobParameters) {
            return false;
        }
    }
    public void M() {
        ComponentName serviceComponent = new ComponentName(this, MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(1234, serviceComponent);
        builder.setMinimumLatency(1000);
        builder.setOverrideDeadline(1000);
        JobInfo jobInfo = builder.build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }
}

