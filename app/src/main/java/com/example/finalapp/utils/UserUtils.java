package com.example.finalapp.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserUtils {
    public static Integer userId;
    public static Integer getUserId (String username, Callback callback){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        OkHttpUtils.sendOkHttp(false, "/user/getUserInfoByName", jsonObject, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response.body().string());
                userId = element.getAsJsonObject().getAsJsonObject("data").get("id").getAsInt();

            }
        });
        return userId;
    }
}
