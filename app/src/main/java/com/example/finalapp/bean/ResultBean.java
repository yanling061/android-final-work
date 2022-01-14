package com.example.finalapp.bean;

public class ResultBean<T> extends BaseResultBean{
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "data=" + data +
                '}';
    }
}
