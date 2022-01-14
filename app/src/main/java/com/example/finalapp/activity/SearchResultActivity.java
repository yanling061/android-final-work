package com.example.finalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.example.finalapp.adapter.ListAdapter;

import com.example.finalapp.bean.ApiUrlBean;
import com.example.finalapp.bean.MovieInfo;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

public class SearchResultActivity extends AppCompatActivity {
    private TextView title;
    private ImageView backArrow;
    private String searchKey;
    private ListView listView;
    private ListAdapter adapter;
    private JsonArray data;
    private TextView searchResAccount;
    private Integer status;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent =getIntent();
        searchKey = intent.getStringExtra("searchKey");
        status =intent.getIntExtra("status",0);
        username=intent.getStringExtra("username");
        Log.i("status", status+"");
        Log.i("username", username+"");

        initData();
        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        /**
         * 设置页面标题
         */
        title=findViewById(R.id.tv_title);
        title.setText("搜索结果");

        /**
         * 设置返回按键点击事件
         */
        backArrow=findViewById(R.id.iv_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化电影数据
     */
    private void initData() {
//        Log.i("searchKey", searchKey);
        listView=findViewById(R.id.lv_list);
        data=new JsonArray();
        searchResAccount=findViewById(R.id.search_res_acount);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("movieName", searchKey);
        jsonObject.addProperty("type", searchKey);
        jsonObject.addProperty("cast",searchKey);

        OkHttpUtils.sendOkHttp(false, "movie/getMovies", jsonObject, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SearchResultActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchResultActivity.this,"电影数据地址请求失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element =parser.parse(response.body().string());
                data = element.getAsJsonObject().get("data").getAsJsonArray();

                adapter=new ListAdapter(SearchResultActivity.this, data,status,username);
                SearchResultActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                        searchResAccount.setText("搜索结果共 : " + data.size() + " 条");
                    }
                });
            }
        });
    }
}