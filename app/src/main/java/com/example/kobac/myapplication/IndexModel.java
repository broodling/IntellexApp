package com.example.kobac.myapplication;

/**
 * Created by kobac on 19.7.17..
 */

public class IndexModel {

    String indexImage;
    String indexTitle;
    String indexDescription;
    String indexID;

    public IndexModel(String indexImage, String indexTitle, String indexDescription, String indexID) {
        this.indexImage = indexImage;
        this.indexTitle = indexTitle;
        this.indexDescription = indexDescription;
        this.indexID = indexID;
    }

    public String getIndexImage() {
        return indexImage;
    }

    public void setIndexImage(String indexImage) {
        this.indexImage = indexImage;
    }

    public String getIndexTitle() {
        return indexTitle;
    }

    public void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }

    public String getIndexDescription() {
        return indexDescription;
    }

    public void setIndexDescription(String indexDescription) {
        this.indexDescription = indexDescription;
    }

    public String getIndexID() {
        return indexID;
    }

    public void setIndexID(String indexID) {
        this.indexID = indexID;
    }
}
