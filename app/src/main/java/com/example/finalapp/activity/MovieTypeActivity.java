package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.R;
import com.example.finalapp.adapter.MovieTypeAdapter;
import com.example.finalapp.bean.ApiUrlBean;
import com.example.finalapp.bean.MovieInfo;
import com.example.finalapp.bean.MovieType;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MovieTypeActivity extends AppCompatActivity {
    private TextView title;
    private ImageView backArrow;
    private int type;
    private String typeStr;
    private GridView gridView;
    private BaseAdapter adapter;
    private JsonArray data;
    private Integer status;
    private String username;
    private Integer movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_type);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        type=intent.getIntExtra("type",0);
        status=intent.getIntExtra("status",0);
        username=intent.getStringExtra("username");

        Log.i("status",status+"");
        Log.i("username",username+"");



        initData();
        initView();
    }

    private void initData() {
        data=new JsonArray();
        gridView=findViewById(R.id.grid_view);
        switch (type) {
            case 1:
                typeStr="??????";
                break;
            case 2:
                typeStr="??????";
                break;
            case 3:
                typeStr="??????";
                break;
            case 4:
                typeStr="??????";
                break;
            case 5:
                typeStr="??????";
                break;
            case 6:
                typeStr="??????";
                break;
            default:
                typeStr="";
                break;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", typeStr);
        OkHttpUtils.sendOkHttp(false, "movie/getMovieByType", jsonObject, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element =parser.parse(response.body().string());
                data = element.getAsJsonObject().get("data").getAsJsonArray();

                adapter=new MovieTypeAdapter(MovieTypeActivity.this, data, status, username);
                MovieTypeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void initView() {
        title = findViewById(R.id.tv_title);
        switch (type) {
            case 1:
                title.setText("????????????");
                break;
            case 2:
                title.setText("????????????");
                break;
            case 3:
                title.setText("????????????");
                break;
            case 4:
                title.setText("????????????");
                break;
            case 5:
                title.setText("????????????");
                break;
            default:
                title.setText("????????????");
                break;
        }

        backArrow=findViewById(R.id.iv_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieTypeActivity.this.finish();
            }
        });

    }
}