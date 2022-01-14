package com.example.finalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalapp.R;
import com.example.finalapp.activity.MovieInfoActivity;
import com.example.finalapp.bean.ResultBean;
import com.example.finalapp.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CollectionAdapter extends BaseAdapter {
    private Context mContext;
    private JsonArray data;
    private int status;
    private String username;
    private int userId;
    private int movieId;

    public CollectionAdapter(Context mContext, JsonArray data, int status, String username, int userId) {
        this.mContext = mContext;
        this.data = data;
        this.status=status;
        this.username=username;
        this.userId=userId;
    }


    class ViewHolder{
        public TextView movieName, movieCast;
        public ImageView movieImage, icon;
        public LinearLayout recordArea;
        private ImageView deleteIcon;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.i("data",data+"");
//        Log.i("username",username+"");
        final ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.collection_item,parent,false);
            vh.movieName=convertView.findViewById(R.id.movie_name);
            vh.movieCast=convertView.findViewById(R.id.movie_info);
            vh.movieImage=convertView.findViewById(R.id.movie_pic);
            vh.icon=convertView.findViewById(R.id.fav_icon);
            vh.recordArea=convertView.findViewById(R.id.ll_search_result);
            vh.deleteIcon=convertView.findViewById(R.id.delete_icon);
            convertView.setTag(vh);
        } else {
            vh= (ViewHolder) convertView.getTag();
        }
            Glide.with(mContext).load(data.get(position).getAsJsonObject().get("movieImage").getAsString()).into(vh.movieImage);
            vh.movieName.setText(data.get(position).getAsJsonObject().get("movieName").getAsString());
            vh.movieCast.setText(data.get(position).getAsJsonObject().get("cast").getAsString());
            vh.recordArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int movieId=data.get(position).getAsJsonObject().get("movieId").getAsInt();
                Intent intent = new Intent(mContext, MovieInfoActivity.class);
                intent.putExtra("movieId", movieId);
                intent.putExtra("status", status);
                intent.putExtra("username", username);
                mContext.startActivity(intent);
            }
        });
            vh.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieId=data.get(position).getAsJsonObject().get("movieId").getAsInt();
//                    Log.d("movieId",movieId+"");
                    data.remove(position);
                    notifyDataSetChanged();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("movieId", movieId);
                    jsonObject.addProperty("userId", userId);
                    OkHttpUtils.sendOkHttp(false, "favorite/cancelFavorite", jsonObject, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Gson gson = new Gson();
                            ResultBean<Double> res = gson.fromJson(response.body().string(),ResultBean.class);
                            Log.d("res", res+"");
                            if (res.getCode()==200){
//                                Log.d("res",res.getCode()+"");
                            }
                        }
                    });
                }
            });
        return convertView;
    }
}
