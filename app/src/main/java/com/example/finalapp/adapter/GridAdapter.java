package com.example.finalapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.finalapp.R;
import com.example.finalapp.activity.MovieTypeActivity;
import com.example.finalapp.bean.MovieType;

import java.util.List;
import java.util.Set;
import java.util.zip.Inflater;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<MovieType> data;
    private String username;
    private Integer status;

    public GridAdapter(Context context, List<MovieType> data,  Integer status ,String username) {
        this.context = context;
        this.data = data;
        this.username = username;
        this.status = status;
    }

    class ViewHolder{
        public TextView type, count;
        public ImageView icon;
        public LinearLayout typeCard;
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


//    设置页面
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("name", username+"");
        Log.i("status", status+"");
        final ViewHolder vh;
        if (convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.grid_view_item,parent,false);
            vh.type=convertView.findViewById(R.id.type);
            vh.count=convertView.findViewById(R.id.count);
            vh.icon=convertView.findViewById(R.id.icon);
            vh.typeCard=convertView.findViewById(R.id.type_card);
            convertView.setTag(vh);
        } else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.type.setText(data.get(position).getType());
        vh.count.setText(data.get(position).getCount());
        vh.icon.setImageResource(data.get(position).getIconSrc());


        vh.typeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieTypeActivity.class);
                intent.putExtra("type", position+1);
                intent.putExtra("username", username);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}

