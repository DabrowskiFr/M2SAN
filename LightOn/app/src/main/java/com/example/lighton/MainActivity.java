package com.example.lighton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lighton.local.LocalLightOnActivity;
import com.example.lighton.remote.RemoteLightOnActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button1 :
                intent = new Intent(this, LocalLightOnActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                intent = new Intent(this, RemoteLightOnActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}