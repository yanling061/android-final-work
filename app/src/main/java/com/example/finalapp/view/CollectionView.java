package com.example.finalapp.view;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.finalapp.R;
import com.example.finalapp.activity.LoginActivity;
import com.example.finalapp.activity.MovieInfoActivity;
import com.example.finalapp.adapter.CollectionAdapter;
import com.example.finalapp.adapter.ListAdapter;
import com.example.finalapp.adapter.PlayHistoryAdapter;
import com.example.finalapp.bean.MovieInfo;
import com.example.finalapp.utils.OkHttpUtils;
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

public class CollectionView {
    private FragmentActivity Context;
    private LayoutInflater inflater;
    private View currentView;
    private TextView title;
    private ImageView backArrow;
    private LinearLayout unloginView;
    private RelativeLayout movieInfoArea;
    private Button loginBtn;
    private ListView listView;
    private BaseAdapter adapter;
    private Integer statusInt;
    private String usernameStr;

    public CollectionView(FragmentActivity context) {
        Context = context;
        inflater = LayoutInflater.from(context);
    }

    private void initView(){
//        关联页面样式
        currentView=inflater.inflate(R.layout.collection_view,null);
        title=currentView.findViewById(R.id.tv_title);
        backArrow=currentView.findViewById(R.id.iv_back);
        backArrow.setVisibility(View.GONE);
        title.setText("收藏夹");

        unloginView=currentView.findViewById(R.id.ll_unlogin);
        loginBtn=currentView.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Context, LoginActivity.class);
                Context.startActivity(intent);
            }
        });

        movieInfoArea=currentView.findViewById(R.id.rl_movie_info);
        listView=currentView.findViewById(R.id.lv_list);
    }

    public View getView(){
        if (currentView == null){
            createView();
        }
        return currentView;
    }

    private void createView() {
        initView();
    }

    private void initData() {
        //根据用户名查询用户Id
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",usernameStr);
        OkHttpUtils.sendOkHttp(false, "/user/getUserInfoByName", jsonObject, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response.body().string());
                Integer userId = element.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsInt();

                JsonObject object = new JsonObject();
                object.addProperty("userId", userId);
//                Log.i("userId",userId+"");

                //根据id查询收藏列表
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", userId);
//                Log.i("userId",userId+"");
                OkHttpUtils.sendOkHttp(false, "favorite/getFavoriteByUserId", object, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(response.body().string());
                        JsonArray res = element.getAsJsonObject().get("data").getAsJsonArray();
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
                                    adapter=new CollectionAdapter(Context, data, statusInt, usernameStr,userId);
                                    Context.runOnUiThread(new Runnable() {
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

    public void showView(int status, String username){
        statusInt = status;
        usernameStr = username;
        Log.d("status",status+"");
        if (currentView == null){
            createView();
        }
        currentView.setVisibility(View.VISIBLE);
        if (status==1){
            initData();
            unloginView.setVisibility(View.GONE);
            movieInfoArea.setVisibility(View.VISIBLE);
        }
    }
}
