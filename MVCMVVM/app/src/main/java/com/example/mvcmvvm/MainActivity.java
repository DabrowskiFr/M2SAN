package com.example.mvcmvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mvcmvvm.mvc.controller.MvcLoginActivity;
import com.example.mvcmvvm.mvvm.view.MvvmLoginActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mvcBtn = findViewById(R.id.mvc_btn);
        Button mvvmBtn = findViewById(R.id.mvvm_btn);
        mvcBtn.setOnClickListener(this);
        mvvmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mvc_btn:
                Intent mvcIntent = new Intent(this, MvcLoginActivity.class);
                startActivity(mvcIntent);
                break;
            case R.id.mvvm_btn:
                Intent mvvmIntent = new Intent(this, MvvmLoginActivity.class);
                startActivity(mvvmIntent);
                break;
            default:
                break;
        }
    }
}