package com.example.finalapp.bean;

public class IMGBean<T> {
    public T imgRes;

    public IMGBean(T imgRes) {
        this.imgRes = imgRes;
    }

//    public String getImgRes() {
//        return imgRes;
//    }
//
//    public void setImgRes(String imgRes) {
//        this.imgRes = imgRes;
//    }

    @Override
    public String toString() {
        return "IMGBean{" +
                "imgRes='" + imgRes + '\'' +
                '}';
    }
}
