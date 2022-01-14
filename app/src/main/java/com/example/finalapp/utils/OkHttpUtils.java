package com.example.finalapp.utils;

import android.util.Log;

import com.example.finalapp.bean.ApiUrlBean;

import com.google.gson.JsonObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {
    public static MediaType JSON= MediaType.parse("application/json;charset=utf-8");
    public static void sendOkHttp(boolean isGet, String uri, JsonObject jsonObject, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String apiUrl = new ApiUrlBean().getApiUrl();
        String url = apiUrl + uri;
//        Log.d("OkHttp", "sendOkHttp: "+ url);
        final Request request;
        if(isGet) {
//            Log.d("OkHttp", "sendgetOkHttp: "+url);
            request = builder
                    .url(url)
                    .get()
                    .build();
        } else {
//            Log.d("OkHttp", "sendgetOkHttp: "+url);
            RequestBody requestBody = RequestBody.create(JSON,String.valueOf(jsonObject));
            request = builder
                    .url(url)
                    .post(requestBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
