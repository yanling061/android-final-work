package com.example.finalapp.bean;

public class PlayHistoryBean {
    private Integer id;
    private String movieName;
    private String movieCast;
    private String movieCover;
    private Integer userId;

    public PlayHistoryBean(Integer id, String movieName, String movieCast, String movieCover, Integer userId) {
        this.id = id;
        this.movieName = movieName;
        this.movieCast = movieCast;
        this.movieCover = movieCover;
        this.userId = userId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieCast() {
        return movieCast;
    }

    public void setMovieCast(String movieCast) {
        this.movieCast = movieCast;
    }

    public String getMovieCover() {
        return movieCover;
    }

    public void setMovieCover(String movieCover) {
        this.movieCover = movieCover;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
