package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.finalapp.R;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class VideoPlayActivity extends AppCompatActivity {
    private VideoView videoView;
    private int status;
    private int movieId;
    private String username;
    private String movieUrl;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);
        movieId = intent.getIntExtra("movieId",0);
        username = intent.getStringExtra("username");

        initView();
    }

    private void initView() {
//        Log.i("status",status+"");
        videoView=findViewById(R.id.video_view);
        MediaController controller = new MediaController(this);

        //根据电影id获取电影信息播放地址
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("movieId", movieId);
        OkHttpUtils.sendOkHttp(false, "movie/getMovieById", jsonObject, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response.body().string());
                movieUrl = element.getAsJsonObject().get("data").getAsJsonObject().get("movieUrl").getAsString();
//                Log.i("movieUrl",movieUrl);

                VideoPlayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoView.setVideoURI(Uri.parse(movieUrl));
                        videoView.start();
                    }

                });
                controller.setMediaPlayer(videoView);
                videoView.setMediaController(controller);
            }
        });

        //如果用户登录,则将添加播放记录
        if (status==1 && username!=null) {
            //根据用户名获取用户id
            JsonObject object = new JsonObject();
            object.addProperty("username", username);
            OkHttpUtils.sendOkHttp(false, "/user/getUserInfoByName", object, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(response.body().string());
                    userId = element.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsInt();
                    Log.i("userIddddddd",userId+"");
                    JsonObject obj = new JsonObject();
                    obj.addProperty("userId", userId);
                    obj.addProperty("movieId",movieId);

                    OkHttpUtils.sendOkHttp(false, "playHistory/addPlayHistory", obj, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(response.body().string());
                            int data = element.getAsJsonObject().get("data").getAsInt();
                            if (data==1){
                                Log.i("msg", "添加成功");
                            }
                        }
                    });
                }
            });
        }
    }
}