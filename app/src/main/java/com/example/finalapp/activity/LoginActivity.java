package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.example.finalapp.bean.ApiUrlBean;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.finalapp.bean.ResultBean;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private TextView barTitle;
    private ImageView backArrow;
    private EditText et_username;
    private EditText et_password;
    private String username;
    private String password;
    private TextView findPassword, register;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_yl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();
        Intent intent=getIntent();
        et_username.setText(intent.getStringExtra("username"));
        et_password.setText(intent.getStringExtra("password"));
    }

    private void initView() {
        barTitle=findViewById(R.id.tv_title);
        barTitle.setText("");
        backArrow=findViewById(R.id.iv_back);
        findPassword=findViewById(R.id.tv_find_password);
        register=findViewById(R.id.tv_register);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        loginBtn=findViewById(R.id.login_btn);

        /**
         * ?????????????????????
         */
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

        /**
         * ??????????????????
         */
        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindpwdActivity.class);
                startActivity(intent);
            }
        });

        /**
         * ????????????????????????
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /**
         * ????????????
         */
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    /**
     *  ????????????
     */
    private void handleLogin() {
        //???????????????????????????
        getEditString();
//        Log.i("username", username);
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
//            Toast.makeText(LoginActivity.this,"??????????????????????????????",Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        OkHttpUtils.sendOkHttp(false, "user/login", jsonObject, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
                        }
                    });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                final ResultBean<String> res = gson.fromJson(response.body().string(),ResultBean.class);
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.getCode()==200){
                            new AestheticDialog.Builder(LoginActivity.this, DialogStyle.EMOTION, DialogType.SUCCESS)
                                    .setTitle("????????????")
                                    .setMessage("????????????????????????~")
                                    .setCancelable(true)
                                    .setAnimation(DialogAnimation.SLIDE_DOWN)
                                    .show();
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    /**
                                     * ???????????? ?????????????????????????????????????????????
                                     */
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("status",1);
                                    intent.putExtra("username",username);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                            };
                            Timer timer = new Timer();
                            timer.schedule(task, 2500);
                        } else if (res.getCode()==202) {
                            new AestheticDialog.Builder(LoginActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("????????????")
                                    .setMessage("???????????????????????????????????????")
                                    .setCancelable(true)
                                    .setAnimation(DialogAnimation.SLIDE_DOWN)
                                    .show();
                        } else {
                            new AestheticDialog.Builder(LoginActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("????????????")
                                    .setMessage("????????????")
                                    .show();
                        }
                    }
                });
            }
        });
    }

    /**
     * ?????????????????????????????????
     */
    private void getEditString() {
        username=et_username.getText().toString().trim();
        password=et_password.getText().toString().trim();
    }
}