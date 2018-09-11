package com.example.shereen.picturesearch.gson;


public class Photos {

    int page;
    int pages;
    int perpage;
    String total;
    Photo[] photo;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public String getTotal() {
        return total;
    }

    public Photo[] getPhoto() {
        return photo;
    }

    public Photo getPhoto(int index){
        return photo[index];
    }
}
