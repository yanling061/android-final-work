package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.finalapp.R;
import com.example.finalapp.adapter.ListAdapter;
import com.example.finalapp.adapter.PlayHistoryAdapter;
import com.example.finalapp.bean.PlayHistoryBean;
import com.example.finalapp.utils.OkHttpUtils;
import com.example.finalapp.utils.UserUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.finalapp.utils.UserUtils.userId;

public class PlayHistoryActivity extends AppCompatActivity {
    private TextView title;
    private ImageView backArrow;
    private String username;
    private ListView listView;
    private PlayHistoryAdapter adapter;
    private JsonArray data;
    private int status;
    private Integer movieId;
    private ImageView favIcon, deleteIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        status = intent.getIntExtra("status", 0);
        movieId = intent.getIntExtra("movieId", 0);

        listView = findViewById(R.id.lv_list);
        deleteIcon = findViewById(R.id.delete_icon);


        initView();
        initData();
    }

    private void initData() {
        Log.i("status", status + "");
        Log.i("username", username);
        data = new JsonArray();
//        根据用户名查询用户id
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        OkHttpUtils.sendOkHttp(false, "/user/getUserInfoByName", jsonObject, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response.body().string());
                userId = element.getAsJsonObject().getAsJsonObject("data").get("id").getAsInt();

//                Log.i("userId",userId+"");
                JsonObject object = new JsonObject();
                object.addProperty("userId", userId);
                OkHttpUtils.sendOkHttp(false, "/playHistory/getPlayHistoryByUserId", object, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(response.body().string());
                        JsonArray res = element.getAsJsonObject().get("data").getAsJsonArray();
//                        Log.i("res",res+"");


                        JsonArray data = new JsonArray();
//                        Log.i("res",res+"");
                        for (int i = 0; i < res.size(); i++) {
                            JsonObject obj = new JsonObject();
                            obj.addProperty("movieId", res.get(i).getAsJsonObject().get("movieId").getAsInt());
                            OkHttpUtils.sendOkHttp(false, "/movie/getMovieById", obj, new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    JsonParser parser = new JsonParser();
                                    JsonElement element = parser.parse(response.body().string());
                                    element.getAsJsonObject().get("data").getAsJsonObject();
                                    data.add(element.getAsJsonObject().get("data").getAsJsonObject());
//                                    Log.d("data",data+"");
                                    adapter = new PlayHistoryAdapter(PlayHistoryActivity.this, data, status, username, userId);
                                    adapter.notifyDataSetChanged();
                                    PlayHistoryActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listView.setAdapter(adapter);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void initView() {
//        Log.i("data",data+"");
        title = findViewById(R.id.tv_title);
        backArrow = findViewById(R.id.iv_back);
        deleteIcon = findViewById(R.id.delete_icon);

        title.setText("播放记录");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHistoryActivity.this.finish();
            }
        });
    }
}