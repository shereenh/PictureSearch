package com.example.shereen.picturesearch.entity;

public class SingleImage {

    String imageUrl;
    String title;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        String result = "-->"+getTitle() +" "+getImageUrl();
        return result;
    }
}
