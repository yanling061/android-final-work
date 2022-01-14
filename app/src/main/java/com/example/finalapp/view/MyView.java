package com.example.finalapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.finalapp.R;
import com.example.finalapp.activity.PlayHistoryActivity;
import com.example.finalapp.activity.LoginActivity;
import com.example.finalapp.activity.SettingsActivity;
import com.example.finalapp.activity.UserInfoActivity;
import com.example.finalapp.activity.WelcomeActivity;

public class MyView {
    private FragmentActivity Context;
    private LayoutInflater inflater;
    private View currentView;
    private TextView tvUsername,userType;
    private LinearLayout moreInfo, history, settings, logout;


    public MyView(FragmentActivity context) {
        Context = context;
        inflater = LayoutInflater.from(context);
    }

    private void initView(){
        currentView=inflater.inflate(R.layout.my_view,null);

        tvUsername=currentView.findViewById(R.id.tv_username);
        userType=currentView.findViewById(R.id.tv_user_type);

        /**
         * 点击进入个人信息页面
         */
        moreInfo=currentView.findViewById(R.id.ll_more);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Context, LoginActivity.class);
                Context.startActivity(intent);
            }
        });

        logout=currentView.findViewById(R.id.ll_exit);
        settings=currentView.findViewById(R.id.ll_settings);
        history=currentView.findViewById(R.id.ll_history);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Context,"请先登录",Toast.LENGTH_SHORT).show();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Context,"请先登录",Toast.LENGTH_SHORT).show();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Context,"请先登录",Toast.LENGTH_SHORT).show();
            }
        });
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

    public void showView(int status, String username){
        Log.d("status",status+"");
        if (currentView == null){
            createView();
        }
        currentView.setVisibility(View.VISIBLE);

//        判断是否登录
        if (status==1){
            tvUsername.setText(username);
            userType.setVisibility(View.VISIBLE);

//            点击进入个人信息页面
            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hangleGetProfile(username);
                }
            });

//            点击退出登录
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleLogut();
                }
            });

//            点击进入设置页面
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSettings(username);
                }
            });

//            点击进入历史记录页面
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleHistory(status,username);
                }
            });
        }
    }

    //进入历史记录页面逻辑
    private void handleHistory(int status, String username) {
        Intent intent = new Intent(Context, PlayHistoryActivity.class);
        intent.putExtra("status",status);
        intent.putExtra("username", username);
        Context.startActivity(intent);
    }

    //进入设置页面逻辑
    private void handleSettings(String username) {
        Intent intent = new Intent(Context, SettingsActivity.class);
        intent.putExtra("username",username);
        Context.startActivity(intent);
    }

    //进入个人信息页面逻辑
    private void hangleGetProfile(String username) {
        Intent intent = new Intent(Context, UserInfoActivity.class);
        intent.putExtra("username", username);
        Context.startActivity(intent);
    }

    //退出登录逻辑
    private void  handleLogut() {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Context);
        alertdialogbuilder.setMessage("确认要退出登录吗?");
        alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Context, WelcomeActivity.class);
                Context.startActivity(intent);
                System.exit(0);
            }
        });
        alertdialogbuilder.setNeutralButton("取消", null);
        final AlertDialog alertdialog1 = alertdialogbuilder.create();
        alertdialog1.show();
    }
}
