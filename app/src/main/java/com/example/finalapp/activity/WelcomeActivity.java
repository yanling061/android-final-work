package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

;import com.example.finalapp.MainActivity;
import com.example.finalapp.R;

public class WelcomeActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button registerBtn;
    private TextView tvForgerPassword;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_yl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();
    }

    private void initView() {
        loginBtn=findViewById(R.id.login_btn);
        registerBtn=findViewById(R.id.register_btn);
        tvForgerPassword=findViewById(R.id.tv_forget_password);
        tvSkip=findViewById(R.id.tv_skip);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgerPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, FindpwdActivity.class);
                startActivity(intent);
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}