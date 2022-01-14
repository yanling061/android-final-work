package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalapp.R;
import com.example.finalapp.bean.ApiUrlBean;
import com.example.finalapp.bean.ResultBean;
import com.example.finalapp.utils.OkHttpUtils;
import com.example.finalapp.utils.UserUtils;
import com.example.finalapp.view.CollectionView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.finalapp.utils.UserUtils.userId;

public class MovieInfoActivity extends AppCompatActivity {
    private Integer movieId;
    private TextView title;
    private ImageView backArrow,movieCover, favIcon;
    private TextView movieName, movieCountry, movieType, movieCast, movieIntro;
    private boolean flag;
    private Integer status;
    private String username;
    private Integer userId;
    private Button playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        status=intent.getIntExtra("status", 0);
        username=intent.getStringExtra("username");

//        Log.i("status",status+"");
//        Log.i("username",username+"");

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        movieId = intent.getIntExtra("movieId",0);
//        Log.i("movieId",movieId+"");

        movieCover=findViewById(R.id.movie_cover);
        movieName=findViewById(R.id.tv_movie_name);
        movieCountry=findViewById(R.id.tv_movie_country);
        movieType=findViewById(R.id.tv_movie_type);
        movieCast=findViewById(R.id.tv_movie_cast);
        movieIntro=findViewById(R.id.movie_intro);

        //获取电影信息
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("movieId",movieId);
        OkHttpUtils.sendOkHttp(false, "movie/getMovieById", jsonObject, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MovieInfoActivity.this,"数据请求失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element =parser.parse(response.body().string());
                JsonObject data = element.getAsJsonObject().get("data").getAsJsonObject();
                MovieInfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MovieInfoActivity.this)
                                .load(data.getAsJsonObject().get("movieCover").getAsString())
                                .circleCrop()
                                .into(movieCover);
                        movieName.setText(data.get("movieName").getAsString());
                        movieCountry.setText(data.get("country").getAsString());
                        movieType.setText(data.get("type").getAsString());
                        movieCast.setText(data.get("cast").getAsString());
                        movieIntro.setText(data.get("movieIntro").getAsString());

                    }
                });
            }
        });
    }

    private void initView() {
        Log.i("status",status+"");
        Log.i("username",username+"");
        Log.i("movieId", movieId+"");

        title=findViewById(R.id.tv_title);
        title.setText("电影详情");

        backArrow=findViewById(R.id.iv_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieInfoActivity.this.finish();
            }
        });

        favIcon=findViewById(R.id.fav_icon);

        playBtn=findViewById(R.id.play_btn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieInfoActivity.this, VideoPlayActivity.class);
                intent.putExtra("status",status);
                intent.putExtra("username", username);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });

        if (status==0){
            favIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MovieInfoActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MovieInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        } else if(status==1 && username!=null ) {
//            Log.i("username",username);
            isFavorite(username);
            Log.i("flag",flag+"");
            if (flag==true){
                favIcon.setImageResource(R.drawable.collection);
            } else {
                favIcon.setImageResource(R.drawable.collection_2);
            }
            favIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag==true){
                        cancelFavorite();
                    } else if( flag==false) {
                        addFavorite();
                    }
                }
            });
        }
    }

    //添加收藏
    private void addFavorite() {
        JsonObject obj = new JsonObject();
        obj.addProperty("userId",userId);
        obj.addProperty("movieId", movieId);
        OkHttpUtils.sendOkHttp(false, "/favorite/addFavorite", obj, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response.body().string());
                int code = element.getAsJsonObject().get("code").getAsInt();
                Log.i("code",code+"");
                MovieInfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        favIcon.setImageResource(R.drawable.collection);
                    }
                });
                flag=true;
            }
        });
    }

    //取消收藏
    private void cancelFavorite() {
        JsonObject obj = new JsonObject();
        obj.addProperty("userId",userId);
        obj.addProperty("movieId", movieId);
        OkHttpUtils.sendOkHttp(false, "/favorite/cancelFavorite", obj, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response.body().string());
                int code = element.getAsJsonObject().get("code").getAsInt();
                Log.i("code",code+"");
                MovieInfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        favIcon.setImageResource(R.drawable.collection_2);
                    }
                });
                flag=false;
            }
        });
    }

    //查看收藏列表
    private void isFavorite(String username) {
        //根据用户名查询用户Id
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
                userId = element.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsInt();
//                Log.i("userId",userId+"");

                JsonObject object = new JsonObject();
                object.addProperty("userId", userId);

                OkHttpUtils.sendOkHttp(false, "favorite/getFavoriteByUserId", object, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(response.body().string());
                        JsonArray data = element.getAsJsonObject().get("data").getAsJsonArray();
//                        Log.i("data",data+"");
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getAsJsonObject().get("movieId").getAsInt()==movieId){
                                Log.i("data",data.get(i).getAsJsonObject().get("movieId")+"");
                                Log.i("movieId",movieId+"");
                                MovieInfoActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        favIcon.setImageResource(R.drawable.collection);
                                    }
                                });
                                flag=true;
                            }
                            else {
                                flag=false;
                            }
                        }
                    }
                });
            }
        });
    }
}