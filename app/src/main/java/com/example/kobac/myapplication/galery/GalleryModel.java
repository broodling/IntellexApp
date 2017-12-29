package com.example.kobac.myapplication.galery;

/**
 * Created by kobac on 20.7.17..
 */

public class GalleryModel {

    private String title;
    private String url;


    public GalleryModel(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
