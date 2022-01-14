package com.example.finalapp.bean;

public class MovieInfo {
    public Integer id;
    public String movieImage;
    public String movieName;
    public String cast;
    public Integer releaseYear;
    public String country;
    public String type;
    public String movieUrl;
    public String movieCover;
    public String movieIntro;
    public String director;

    public MovieInfo(Integer id, String movieImage, String movieName, String cast, Integer releaseYear, String country, String type, String movieUrl, String movieCover, String movieIntro, String director) {
        this.id = id;
        this.movieImage = movieImage;
        this.movieName = movieName;
        this.cast = cast;
        this.releaseYear = releaseYear;
        this.country = country;
        this.type = type;
        this.movieUrl = movieUrl;
        this.movieCover = movieCover;
        this.movieIntro = movieIntro;
        this.director = director;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public String getMovieCover() {
        return movieCover;
    }

    public void setMovieCover(String movieCover) {
        this.movieCover = movieCover;
    }

    public String getMovieIntro() {
        return movieIntro;
    }

    public void setMovieIntro(String movieIntro) {
        this.movieIntro = movieIntro;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
                "id=" + id +
                ", movieImage='" + movieImage + '\'' +
                ", movieName='" + movieName + '\'' +
                ", cast='" + cast + '\'' +
                ", releaseYear=" + releaseYear +
                ", country='" + country + '\'' +
                ", type='" + type + '\'' +
                ", movieUrl='" + movieUrl + '\'' +
                ", movieCover='" + movieCover + '\'' +
                ", movieIntro='" + movieIntro + '\'' +
                ", director='" + director + '\'' +
                '}';
    }
}