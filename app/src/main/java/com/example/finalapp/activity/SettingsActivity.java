package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingsActivity extends AppCompatActivity {
    private TextView title;
    private ImageView backArrow;
    private LinearLayout modifyPassword, findPassword, setSecurity;
    private String username;
    private EditText oldPassword, newPassword, newPasswordAgain;
    private String oldPasswordStr, newPasswordStr, newPasswordAgainStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        username=intent.getStringExtra("username");
        title= findViewById(R.id.tv_title);
        title.setText("设置");
        backArrow=findViewById(R.id.iv_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
        findPassword=findViewById(R.id.ll_find_pwd);
        modifyPassword=findViewById(R.id.ll_modify_pwd);
        setSecurity=findViewById(R.id.ll_set_security);

        //点击进入找回密码页面
        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFindPassword();
            }
        });

        //点击修改密码
        modifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleModifyPassword();
            }
        });

        //点击进去设置密保页面
        setSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSetsecurity(username);
            }
        });

    }

    private void handleSetsecurity(String username) {
        Intent intent = new Intent(SettingsActivity.this,SetSecurityActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    private void handleFindPassword() {
        Intent intent = new Intent(SettingsActivity.this, FindpwdActivity.class);
        startActivity(intent);
    }

    private void handleModifyPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        // 获取布局
        View view = View.inflate(SettingsActivity.this, R.layout.update_pwd, null);
        oldPassword = view.findViewById(R.id.old_pwd);
        newPassword = view.findViewById(R.id.new_pwd);
        newPasswordAgain = view.findViewById(R.id.new_pwd_again);

        // 设置参数
        builder.setTitle("修改密码").setIcon(R.drawable.cool_boy).setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getEditString();

                if (oldPasswordStr.isEmpty()||newPasswordStr.isEmpty()||newPasswordAgainStr.isEmpty()) {
                    SettingsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AestheticDialog.Builder(SettingsActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("修改失败")
                                    .setMessage("所填项不能为空")
                                    .show();
                        }
                    });

                } else if (!newPasswordStr.equals(newPasswordAgainStr)){
                    SettingsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AestheticDialog.Builder(SettingsActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("修改失败")
                                    .setMessage("两次输入的密码不一致")
                                    .show();
                        }
                    });
                } else {
                    // 发送修改密码接口
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("username", username);
                    jsonObject.addProperty("password",newPasswordStr);
                    OkHttpUtils.sendOkHttp(false, "/user/updatePassword", jsonObject, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Gson gson = new Gson();
                            final ResultBean<String> res = gson.fromJson(response.body().string(),ResultBean.class);
                            if (res.getCode()==200){
                                SettingsActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AestheticDialog.Builder(SettingsActivity.this, DialogStyle.EMOTION, DialogType.SUCCESS)
                                                .setTitle("修改成功")
                                                .setMessage("密码修改成功~")
                                                .setCancelable(true)
                                                .setAnimation(DialogAnimation.SLIDE_DOWN)
                                                .show();
                                        TimerTask task = new TimerTask() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        };
                                        Timer timer = new Timer();
                                        timer.schedule(task, 2500);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }).create().show();
    }

    private void getEditString() {
        oldPasswordStr=oldPassword.getText().toString().trim();
        newPasswordStr=newPassword.getText().toString().trim();
        newPasswordAgainStr=newPasswordAgain.getText().toString().trim();
    }
}