package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalapp.bean.IMGBean;
import com.example.finalapp.view.CollectionView;
import com.example.finalapp.view.HomeView;
import com.example.finalapp.view.MyView;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout bodyLayout;
    private LinearLayout tabBar;
    private View homeBtn, collectBtn,myBtn;
    private ImageView home, collection, my;
    private HomeView homeView;
    private CollectionView collectionView;
    private MyView myView;
    private int status;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        initBodyLayout();
        initTabBar();
        setInitStatus();
        setListener();
    }



    private void setListener() {
        for (int i = 0;i<tabBar.getChildCount();i++){
            tabBar.getChildAt(i).setOnClickListener(this);
        }
    }


    private void setInitStatus() {

        //登录成功,获取登录页面传过来的用户名和登录状态参数
        Intent intent =getIntent();
        status=intent.getIntExtra("status",0);
        username=intent.getStringExtra("username");

        clearBottomImageState();
        setSelectedStatus(0);
        createView(0);
    }

    //创建页面
    private void createView(int i) {
        switch (i) {
            case 0:
                if(homeView==null){
                    homeView=new HomeView(this);
                    bodyLayout.addView(homeView.getView());
                } else {
                    homeView.getView();
                }
                //将获取的参数传给首页页面
                homeView.showView(status,username);
                break;
            case 1:
                if(collectionView==null){
                    collectionView=new CollectionView(this);
                    bodyLayout.addView(collectionView.getView());
                } else {
                    collectionView.getView();
                }
                //将获取的参数传给首页页面
                collectionView.showView(status,username);
                break;
            case 2:
                if(myView==null){
                    myView=new MyView(this);
                    bodyLayout.addView(myView.getView());
                } else {
                    myView.getView();
                }
                //将获取的参数传给首页页面
                myView.showView(status,username);
                break;
        }
    }

    private void setSelectedStatus(int i) {
        switch (i){
            case 0:
                homeBtn.setSelected(true);
                home.setImageResource(R.drawable.home);
                break;
            case 1:
                collectBtn.setSelected(true);
                collection.setImageResource(R.drawable.collection);
                break;
            case 2:
                myBtn.setSelected(true);
                my.setImageResource(R.drawable.my);
        }
    }

    private void clearBottomImageState() {
        home.setImageResource(R.drawable.home_2);
        collection.setImageResource(R.drawable.collection_2);
        my.setImageResource(R.drawable.my_2);
        for (int i = 0; i < tabBar.getChildCount(); i++) {
            tabBar.getChildAt(i).setSelected(false);
        }
    }

    private void initTabBar() {
        tabBar=findViewById(R.id.tab_bar);
        homeBtn=findViewById(R.id.home_btn);
        collectBtn=findViewById(R.id.collect_btn);
        myBtn=findViewById(R.id.my_btn);
        home=findViewById(R.id.tab_bar_home);
        collection=findViewById(R.id.tab_bar_collection);
        my=findViewById(R.id.tab_bar_my);
    }

    private void initBodyLayout() {
        bodyLayout=findViewById(R.id.main_body);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_btn:
                clearBottomImageState();
                selectDisplayView(0);
                break;

            case R.id.collect_btn:
                clearBottomImageState();
                selectDisplayView(1);
                break;
            //我的点击事件
            case R.id.my_btn:
                clearBottomImageState();
                selectDisplayView(2);
                break;
            default:
                break;
        }
    }

    private void selectDisplayView(int i) {
        removeAllView();
        createView(i);
        setSelectedStatus(i);
    }

    private void removeAllView() {
        for (int i =0; i<bodyLayout.getChildCount();i++){
            bodyLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    private boolean isLogin() {
        if (status==1) return true;
        else return false;
    }

    protected long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(MainActivity.this,"再按一次确认退出",Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }
            else {
                if(isLogin()) {
                    status=0;
                }
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}