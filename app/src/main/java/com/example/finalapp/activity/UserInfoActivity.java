package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.finalapp.bean.ApiUrlBean;
import com.example.finalapp.bean.ResultBean;
import com.example.finalapp.bean.UserBean;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity {
    private String apiUrl = new ApiUrlBean().getApiUrl();
    private TextView title;
    private ImageView backArrow;
    private TextView username, email, gender, intro;
    private String usernameStr,genderStr, emailStr, introStr;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent =getIntent();
        usernameStr = intent.getStringExtra("username");

        email=findViewById(R.id.tv_email);
        gender=findViewById(R.id.tv_gender);
        intro=findViewById(R.id.tv_intro);

        initData();
        initView();
    }

    /**
     * 初始化个人信息
     */
    private void initData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",usernameStr);

        OkHttpUtils.sendOkHttp(false, "user/getUserInfoByName", jsonObject, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserInfoActivity.this,"数据请求失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject res = jsonObject.getJSONObject("data");
                    UserInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Log.i("data",res+"");
                            id=res.optInt("id");
                            emailStr=res.optString("email");
                            genderStr=res.optString("gender");
                            introStr=res.optString("intro");
                            email.setText(emailStr);
                            gender.setText(genderStr);
                            intro.setText(introStr);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        title=findViewById(R.id.tv_title);
        title.setText("个人资料");

        backArrow=findViewById(R.id.iv_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
            }
        });

        username=findViewById(R.id.tv_username);
        username.setText(usernameStr);

        /**
         * Email点击事件
         */
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEmail();
            }
        });

        /**
         * 性别点击事件
         */
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("gender",genderStr);
                handleGender();

            }
        });

        /**
         * 修改点击事件
         */
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleIntro();
            }
        });
    }

    /**
     * 处理简介修改
     */
    private void handleIntro() {
        dialogBox("简介");
    }

    /**
     * 处理邮箱修改
     */
    private void handleEmail() {
        dialogBox("邮箱");
    }

    /**
     * 处理性别修改
     */
    private void handleGender() {
        int flag = 0;
        switch (genderStr){
            case "男":
                flag = 0;
                break;
            case "女":
                flag = 1;
                break;
            case "保密":
                flag = 2;
                break;
            default:
                break;
        }
        final String items[] = {"男","女","保密"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle("性别");
        builder.setSingleChoiceItems(items, flag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", id);
                jsonObject.addProperty("gender",items[which]);
                OkHttpUtils.sendOkHttp(false, "user/updateGender", jsonObject, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        UserInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserInfoActivity.this,"数据请求失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        ResultBean<Integer> res = gson.fromJson(response.body().string(),ResultBean.class);
//                        Log.i("res", res.getCode()+"");
                        UserInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gender.setText(items[which]);
                                initData();
                            }
                        });

                    }
                });
//
            }
        });
        builder.create().show();
    }

    /**
     * 修改邮箱和简介
     * @param type
     */
    private void dialogBox(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);

        // 获取布局
        View view = View.inflate(UserInfoActivity.this, R.layout.update_info, null);
        TextView infoType = view.findViewById(R.id.info_type);
        infoType.setText(type);
        EditText infoContent = view.findViewById(R.id.info_content);


        // 设置参数
        builder.setTitle(type).setIcon(R.drawable.ninja).setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id",id);
                if(type.equals("邮箱")){
                    emailStr=infoContent.getText().toString().trim();
                    if (emailStr.isEmpty()) return;
//                    Log.i("isEmail",!isEmail(emailStr)+"");
                    else if (!(isEmail(emailStr))){
                        UserInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserInfoActivity.this,"请输入正确的邮箱地址",Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    jsonObject.addProperty("email",emailStr);
                    OkHttpUtils.sendOkHttp(false, "user/updateEmail", jsonObject, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Gson gson = new Gson();
                            ResultBean<Integer> res = gson.fromJson(response.body().string(),ResultBean.class);
                            Log.i("res", res.getCode()+"");
                            UserInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    email.setText(emailStr);
                                    initData();
                                }
                            });
                        }
                    });
                } else if(type.equals("简介")){
                    introStr=infoContent.getText().toString().trim();
                    if (introStr.isEmpty()) return;
                    jsonObject.addProperty("intro",introStr);
                    OkHttpUtils.sendOkHttp(false, "user/updateEmail", jsonObject, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Gson gson = new Gson();
                            ResultBean<Integer> res = gson.fromJson(response.body().string(),ResultBean.class);
                            Log.i("res", res.getCode()+"");
                            UserInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    intro.setText(introStr);
                                    initData();
                                }
                            });
                        }
                    });
                }
            }
        }).create().show();
    }

    /**
     * 判断邮箱正则
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String emailPattern = "[a-zA-Z0-9][a-zA-Z0-9._-]{1,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
        boolean result = Pattern.matches(emailPattern, email);
        return result;
    }
}