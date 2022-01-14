package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.example.finalapp.bean.ApiUrlBean;
import com.example.finalapp.bean.ResultBean;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextView barTitle;
    private ImageView backArrow;
    private Button registerBtn;
    private TextView loginText;
    private EditText etUsername, etPassword, etPasswordAgain;
    private String username,password, passwordAagin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_yl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        intiView();
    }


    private void intiView() {
        barTitle=findViewById(R.id.tv_title);
        barTitle.setText("");
        backArrow=findViewById(R.id.iv_back);
        registerBtn=findViewById(R.id.register_btn);
        loginText=findViewById(R.id.tv_login);
        etPassword=findViewById(R.id.et_password);
        etPasswordAgain=findViewById(R.id.et_password_again);
        etUsername=findViewById(R.id.et_username);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });
    }

    private void handleRegister() {
        getEditString();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(passwordAagin)){
            new AestheticDialog.Builder(RegisterActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                    .setTitle("注册失败")
                    .setMessage("所填项不能为空")
                    .setCancelable(true)
                    .show();
            return;
        }
        if(!passwordAagin.equals(password)) {
            new AestheticDialog.Builder(RegisterActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                    .setTitle("注册失败")
                    .setMessage("输入密码不一致")
                    .setCancelable(true)
                    .show();
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        OkHttpUtils.sendOkHttp(false, "user/register", jsonObject, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                final ResultBean<String> res = gson.fromJson(response.body().string(),ResultBean.class);
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.getCode()==200){
                            new AestheticDialog.Builder(RegisterActivity.this, DialogStyle.EMOTION, DialogType.SUCCESS)
                                    .setTitle("注册成功")
                                    .setMessage("首次登录记得设置邮箱密保哦~")
                                    .setCancelable(true)
                                    .setAnimation(DialogAnimation.SLIDE_DOWN)
                                    .show();
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("username",username);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                }
                            };
                            Timer timer = new Timer();
                            timer.schedule(task, 2500);
                        } else if (res.getCode()==201) {
                            new AestheticDialog.Builder(RegisterActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("注册失败")
                                    .setMessage("用户名已注册")
                                    .setCancelable(true)
                                    .show();
                        } else {
                            new AestheticDialog.Builder(RegisterActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("注册失败")
                                    .setMessage("连接异常")
                                    .setCancelable(true)
                                    .show();
                        }
                    }
                });
            }
        });
    }

    private void getEditString() {
        username=etUsername.getText().toString().trim();
        password=etPassword.getText().toString().trim();
        passwordAagin=etPasswordAgain.getText().toString().trim();
    }
}