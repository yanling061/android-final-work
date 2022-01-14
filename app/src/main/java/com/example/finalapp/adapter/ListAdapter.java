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
import com.example.finalapp.activity.PlayHistoryActivity;
import com.example.finalapp.bean.MovieInfo;
import com.example.finalapp.bean.PlayHistoryBean;
import com.google.gson.JsonArray;

import java.util.List;

public class ListAdapter extends BaseAdapter{
    private Context context;
    private JsonArray data;
    private Integer movieId;
    private Integer status;
    private String username;


    public ListAdapter(Context context, JsonArray data, Integer status, String username) {
        this.context = context;
        this.data = data;
        this.status = status;
        this.username = username;
    }


    class ViewHolder{
        public TextView movieName, cast;
        public ImageView movieImage, icon;
        public boolean flag;
        public LinearLayout searchResultArea;
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
//        Log.i("data", data+"");
       final ViewHolder vh;
        if (convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.list_view_item,parent,false);
            vh.movieName=convertView.findViewById(R.id.movie_name);
            vh.cast=convertView.findViewById(R.id.movie_info);
            vh.movieImage=convertView.findViewById(R.id.movie_pic);
            vh.icon=convertView.findViewById(R.id.fav_icon);
            vh.searchResultArea=convertView.findViewById(R.id.ll_search_result);
            convertView.setTag(vh);
        } else {
            vh= (ViewHolder) convertView.getTag();
        }
            Glide.with(context)
                    .load(data.get(position).getAsJsonObject().get("movieImage").getAsString())
                    .override(150,100)
                    .centerCrop()
                    .into(vh.movieImage);
            vh.movieName.setText(data.get(position).getAsJsonObject().get("movieName").getAsString());
            vh.cast.setText(data.get(position).getAsJsonObject().get("cast").getAsString());

            vh.searchResultArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieId=data.get(position).getAsJsonObject().get("movieId").getAsInt();
                    Intent intent = new Intent(context, MovieInfoActivity.class);
                    intent.putExtra("movieId", movieId);
                    intent.putExtra("status", status);
                    intent.putExtra("username", username);
                    context.startActivity(intent);
                }
            });
        return convertView;
    }
}
