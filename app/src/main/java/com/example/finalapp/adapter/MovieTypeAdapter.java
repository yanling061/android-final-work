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
import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.example.finalapp.activity.MovieInfoActivity;
import com.example.finalapp.bean.MovieInfo;
import com.google.gson.JsonArray;

import java.util.List;

public class MovieTypeAdapter extends BaseAdapter {
    private Context context;
    private JsonArray data;
    private Integer status;
    private String username;
    private Integer movieId;


    public MovieTypeAdapter(Context context, JsonArray data, Integer status, String username) {
        this.context = context;
        this.data = data;
        this.status = status;
        this.username = username;
    }

    class ViewHolder{
        public TextView movieName, releaseYear;
        public ImageView movieCover;
        public LinearLayout movietArea;
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
//        Log.i("tag",data.toString());
        final ViewHolder vh;
        if (convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.movie_type_item,parent,false);
            vh.movieName=convertView.findViewById(R.id.movie_name);
            vh.releaseYear=convertView.findViewById(R.id.release_year);
            vh.movieCover=convertView.findViewById(R.id.movie_cover);
            vh.movietArea=convertView.findViewById(R.id.ll_movie_area);
            convertView.setTag(vh);
        } else {
            vh= (ViewHolder) convertView.getTag();
        }
        Glide.with(vh.movieCover)
                .load(data.get(position).getAsJsonObject().get("movieCover").getAsString())
                .override(140,210)
                .centerCrop()
                .into(vh.movieCover);
        vh.movieName.setText(data.get(position).getAsJsonObject().get("movieName").getAsString());
        vh.releaseYear.setText(data.get(position).getAsJsonObject().get("releaseYear").getAsString()+"年");
        vh.movietArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieId=data.get(position).getAsJsonObject().get("movieId").getAsInt();
                Intent intent = new Intent(context, MovieInfoActivity.class);
                intent.putExtra("status",status);
                intent.putExtra("username", username);
                intent.putExtra("movieId", movieId);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
