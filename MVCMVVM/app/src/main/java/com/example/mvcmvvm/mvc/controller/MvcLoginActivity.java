package com.example.mvcmvvm.mvc.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mvcmvvm.R;
import com.example.mvcmvvm.mvc.model.User;

public class MvcLoginActivity extends AppCompatActivity {

    private EditText userNameEt;
    private EditText passwordEt;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc_login);

        userNameEt = findViewById(R.id.user_name_et);
        passwordEt = findViewById(R.id.password_et);
        Button loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // La logique du composant est exposée par cet appel
                login(userNameEt.getText().toString(), passwordEt.getText().toString());
            }
        });
    }
    private void login(String userName, String password) {
        if (userName.equals("toto") && password.equals("toto")) {
            user.setUserName(userName);
            user.setPassword(password);
            Toast.makeText(MvcLoginActivity.this,
                            userName + " Login Successful",
                            Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(MvcLoginActivity.this,
                            "Login Failed",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
}