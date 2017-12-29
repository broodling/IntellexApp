package com.example.kobac.myapplication.details;

import android.os.Bundle;

import com.example.kobac.myapplication.galery.GalleryModel;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.kobac.myapplication.R.id.imageIcon;

/**
 * Created by kobac on 20.7.17..
 */

public class DetailsModel implements Serializable{

    String indexTitle;
    String indexClient;
    String indexYear;
    String indexText;
    String indexServices;
    String id;
    String frontImage;
    private ArrayList<GalleryModel> project_gallery;

    public DetailsModel(String indexTitle, String indexClient, String indexYear, String indexText, String indexServices, String id,final ArrayList<GalleryModel> images, String frontImage) {
        this.indexTitle = indexTitle;
        this.indexClient = indexClient;
        this.indexYear = indexYear;
        this.indexText = indexText;
        this.indexServices = indexServices;
        this.id = id;
        this.project_gallery = images;
        this.frontImage = frontImage;
    }

    public String getIndexTitle() {
        return indexTitle;
    }

    public void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }

    public String getIndexClient() {
        return indexClient;
    }

    public void setIndexClient(String indexClient) {
        this.indexClient = indexClient;
    }

    public String getIndexYear() {
        return indexYear;
    }

    public void setIndexYear(String indexYear) {
        this.indexYear = indexYear;
    }

    public String getIndexText() {
        return indexText;
    }

    public void setIndexText(String indexDescription) {
        this.indexText = indexDescription;
    }

    public String getIndexServices() {
        return indexServices;
    }

    public void setIndexServices(String indexServices) {
        this.indexServices = indexServices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<GalleryModel> getProject_gallery() {
        return project_gallery;
    }

    public void setProject_gallery(ArrayList<GalleryModel> project_gallery) {
        this.project_gallery = project_gallery;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }
}
