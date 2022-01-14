package com.example.finalapp.view;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.example.finalapp.GlideApp;
import com.example.finalapp.R;
import com.example.finalapp.activity.SearchResultActivity;
import com.example.finalapp.activity.UserInfoActivity;
import com.example.finalapp.adapter.GridAdapter;
import com.example.finalapp.adapter.ImageAdapter;
import com.example.finalapp.bean.ApiUrlBean;
import com.example.finalapp.bean.IMGBean;
import com.example.finalapp.bean.MovieType;
import com.example.finalapp.bean.ResultBean;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.transformer.AlphaPageTransformer;
import com.youth.banner.transformer.RotateYTransformer;
import com.youth.banner.transformer.ScaleInTransformer;
import com.youth.banner.util.BannerUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HomeView {
    private FragmentActivity Context;
    private LayoutInflater inflater;
    private View currentView;
    private Banner banner;
    private LinearLayout userInfo;
    private TextView tvUsername;
    private EditText searchInput;
    private String searchKey;
    private ImageView toMyBtn;
    private GridView gridView;
    private BaseAdapter adapter;
    private List<MovieType> data;
    private Integer statusInt;
    private String usernameStr;
    private ImageAdapter imageAdapter;

    public HomeView(FragmentActivity context){
        /**
         * 为之后将Layout转化为view时用
         */
        Context = context;
        inflater = LayoutInflater.from(context);
    }

    private void initView() {
        Log.i("status", statusInt+"");
        Log.i("username", usernameStr+"");
        /**
         * 关联当前页面样式
         */
        currentView=inflater.inflate(R.layout.home_view,null);

        /**
         * 用户信息条
         */
        userInfo=currentView.findViewById(R.id.user_info);

        searchInput=currentView.findViewById(R.id.et_search);


        /**
         * 欢迎标语控件绑定
         */
        tvUsername=currentView.findViewById(R.id.tv_username);
        toMyBtn=currentView.findViewById(R.id.iv_my_btn);

        /**
         * 轮播图数据及页面绑定
         */
        banner=currentView.findViewById(R.id.banner);
        /**
         * 电影分类样式及数据绑定
         *
         */
        gridView=currentView.findViewById(R.id.grid_view);
        adapter=new GridAdapter(Context,data,0,null);
        gridView.setAdapter(adapter);

        /**
         * 搜索控件监听回车事件
         */
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Log.i("actionId", actionId+"");
                if (actionId == 0 || actionId == 4
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    handleSearch(statusInt, usernameStr);
                }
                return false;
            }
        });
    }

    /**
     * 创建视图
     */
    private void createView(){
        initBannerData();
        initMovieData();
        initView();
    }

    /**
     * 初始化电影信息数据
     */
    private void initMovieData() {
        data = new ArrayList<>();
        data.add(new MovieType(R.drawable.cool,"剧情","6"));
        data.add(new MovieType(R.drawable.i_2,"喜剧","6"));
        data.add(new MovieType(R.drawable.romantic,"浪漫","3"));
        data.add(new MovieType(R.drawable.i_4,"科幻","2"));
        data.add(new MovieType(R.drawable.ninja,"动画","3"));
        data.add(new MovieType(R.drawable.sweating,"悬疑","1"));
        data.add(new MovieType(R.drawable.sweating,"悬疑","15"));
        data.add(new MovieType(R.drawable.sweating,"悬疑","15"));
        data.add(new MovieType(R.drawable.sweating,"悬疑","15"));
    }

    /**
     * 获取当前在导航栏上方显示对于的View
     */
    public View getView(){
        if (currentView == null){
            createView();
        }
        return currentView;
    }

    /**
     * 显示当前导航栏上方所对应的view界面
     */
    public void showView(int status, String username){
        Log.d("status",status+"");
        statusInt = status;
        usernameStr = username;
        if (currentView == null){
            createView();
        }
        currentView.setVisibility(View.VISIBLE);
        /**
         * 如果登录状态,显示欢迎标语
         */
        if (status==1){
            userInfo.setVisibility(View.VISIBLE);
            tvUsername.setText(username+" , 你好呀~");
            /**
             * 点击头像进入个人信息页面
             */
            toMyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Context, UserInfoActivity.class);
                    intent.putExtra("username", username);
                    Context.startActivity(intent);
                }
            });
            adapter=new GridAdapter(Context,data,status,username);
            gridView.setAdapter(adapter);
        }
    }

    /**
     * 初始化轮播图数据
     */
    private void initBannerData() {
        List<IMGBean> list = new ArrayList<>();
        OkHttpUtils.sendOkHttp(true, "banner/getImage", null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Context,"轮播图网络地址请求失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element =parser.parse(response.body().string());
//                Log.i("element",element+"");
                if (element.getAsJsonObject().get("code").getAsInt()==200) {
                    JsonArray res = element.getAsJsonObject().get("data").getAsJsonArray();
                    for (int i = 0; i < res.size(); i++) {
                        list.add(new IMGBean(res.get(i).getAsJsonObject().get("bannerUrl").getAsString()));
                    }
                    imageAdapter=new ImageAdapter(list);
                    Context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            banner.setAdapter(imageAdapter)
                                    .isAutoLoop(true)
                                    .setIndicator(new CircleIndicator(Context))
                                    .setBannerRound(BannerUtils.dp2px(25))//圆角
                                    .addPageTransformer(new RotateYTransformer())//添加切换效果
                                    .setIndicatorSelectedColor(Color.parseColor("#fb7f40"));
                        }
                    });
                }
            }
        });
    }

    /**
     * 搜索功能
     */
       public void handleSearch(Integer status, String username ) {
           searchKey=searchInput.getText().toString().trim();
//           Log.i("searchKey", searchKey+"");
           Intent intent = new Intent(Context, SearchResultActivity.class);
           intent.putExtra("searchKey", searchKey);
           intent.putExtra("username", username);
           intent.putExtra("status",status);
           Context.startActivity(intent);
       }
}