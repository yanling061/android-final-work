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
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SetSecurityActivity extends AppCompatActivity {
    private TextView title;
    private ImageView backArrow;
    private EditText etEmail;
    private String strEmail;
    private Button saveBtn;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_security);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        initView();
    }

    private void initView() {
        title=findViewById(R.id.tv_title);
        backArrow=findViewById(R.id.iv_back);
        title.setText("");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetSecurityActivity.this.finish();
            }
        });

        etEmail=findViewById(R.id.et_email);

        //点击设置密保邮箱
        saveBtn=findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSave();
            }
        });
    }

    private void handleSave() {
        getEditStr();
        //根据用户名获取用户id
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        OkHttpUtils.sendOkHttp(false, "/user/getUserInfoByName", jsonObject, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response.body().string());
                int id = element.getAsJsonObject().getAsJsonObject("data").get("id").getAsInt();

                if (strEmail.isEmpty()){
                    SetSecurityActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AestheticDialog.Builder(SetSecurityActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("设置失败")
                                    .setMessage("邮箱地址不能为空")
                                    .show();
                        }
                    });
                } else if(!isEmail(strEmail)){
                    SetSecurityActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SetSecurityActivity.this,"请输入正确的邮箱地址",Toast.LENGTH_SHORT).show();
                            new AestheticDialog.Builder(SetSecurityActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("设置失败")
                                    .setMessage("请输入正确的邮箱地址")
                                    .show();
                        }
                    });
                } else {
                    //拿到id后,添加邮箱地址
                    JsonObject object = new JsonObject();
                    object.addProperty("id",id);
                    object.addProperty("email",strEmail);

                    OkHttpUtils.sendOkHttp(false, "user/updateEmail", object, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            JsonParser jsonParser = new JsonParser();
                            JsonElement jsonElement = jsonParser.parse(response.body().string());
                            int data = jsonElement.getAsJsonObject().get("data").getAsInt();
                            Log.i("data",data+"");
                            if (data==1){
                                SetSecurityActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SetSecurityActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                                        new AestheticDialog.Builder(SetSecurityActivity.this, DialogStyle.EMOTION, DialogType.SUCCESS)
                                                .setTitle("设置成功")
                                                .setMessage("请牢记邮箱地址哦~")
                                                .setCancelable(true)
                                                .setAnimation(DialogAnimation.SLIDE_DOWN)
                                                .show();
                                        TimerTask task = new TimerTask() {
                                            @Override
                                            public void run() {
                                                SetSecurityActivity.this.finish();
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
        });
    }

    //正则判断邮箱格式
    public static boolean isEmail(String email) {
        String emailPattern = "[a-zA-Z0-9][a-zA-Z0-9._-]{2,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
        boolean result = Pattern.matches(emailPattern, email);
        return result;
    }

    private void getEditStr() {
        strEmail=etEmail.getText().toString().trim();
    }
}