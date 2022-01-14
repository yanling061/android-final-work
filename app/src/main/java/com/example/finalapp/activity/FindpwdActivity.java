package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.R;
import com.example.finalapp.bean.ResultBean;
import com.example.finalapp.bean.UserBean;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindpwdActivity extends AppCompatActivity {
    private TextView title;
    private ImageView backArrow;
    private EditText etUsername, etEmail;
    private String usernameStr, emailStr;
    private Button findBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd_yl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();
    }

    private void initView() {
        title=findViewById(R.id.tv_title);
        backArrow=findViewById(R.id.iv_back);
        findBtn=findViewById(R.id.find_btn);
        title.setText("");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindpwdActivity.this.finish();
            }
        });
        etUsername=findViewById(R.id.et_username);
        etEmail=findViewById(R.id.et_email);

        /**
         * 点击找回
         */
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFindPassword();
            }
        });

    }

    private void handleFindPassword() {
        getEditStr();
        if (usernameStr.isEmpty()&&emailStr.isEmpty()) {
            FindpwdActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AestheticDialog.Builder(FindpwdActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                            .setTitle("找回失败")
                            .setMessage("所填项不能为空")
                            .show();
                }
            });
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", usernameStr);
            // 根据用户名查询用户信息
            OkHttpUtils.sendOkHttp(false, "user/getUserInfoByName", jsonObject, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject res = jsonObject.getJSONObject("data");
                        String email= res.getString("email");
                        if(jsonObject.getInt("code")!=200){
                            FindpwdActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AestheticDialog.Builder(FindpwdActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                            .setTitle("找回失败")
                                            .setMessage("用户不存在")
                                            .show();
                                }
                            });
                        } else if (!emailStr.equals(email)) {
                            FindpwdActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AestheticDialog.Builder(FindpwdActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                            .setTitle("找回失败")
                                            .setMessage("邮箱地址不正确")
                                            .show();
                                }
                            });
                        } else {
                            //密码重置为000000
                            JsonObject jobj = new JsonObject();
                            jobj.addProperty("username",usernameStr);
                            jobj.addProperty("password","000000");
                            OkHttpUtils.sendOkHttp(false, "user/updatePassword", jobj, new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    FindpwdActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(FindpwdActivity.this,"",Toast.LENGTH_SHORT).show();
                                            new AestheticDialog.Builder(FindpwdActivity.this, DialogStyle.EMOTION, DialogType.SUCCESS)
                                                    .setTitle("找回密码成功")
                                                    .setMessage("密码已重置为000000,登陆后请尽快修改~")
                                                    .show();
                                            TimerTask task = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(FindpwdActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            };
                                            Timer timer = new Timer();
                                            timer.schedule(task, 3500);
                                        }
                                    });
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void getEditStr() {
        usernameStr=etUsername.getText().toString().trim();
        emailStr=etEmail.getText().toString().trim();
    }
}