package com.example.finalapp.bean;

public class MovieType {
    private int iconSrc;
    private String type;
    private String count;

    public MovieType(int iconSrc, String type, String count) {
        this.iconSrc = iconSrc;
        this.type = type;
        this.count = count;
    }

    public int getIconSrc() {
        return iconSrc;
    }

    public void setIconSrc(int iconSrc) {
        this.iconSrc = iconSrc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "MovieType{" +
                "iconSrc=" + iconSrc +
                ", type='" + type + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
