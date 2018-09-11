package com.example.shereen.picturesearch.gson;


public class TopLevel {

    Photos photos;
    String stat;

    public String getStat() {
        return stat;
    }

    public Photos getPhotos() {
        return photos;
    }

    @Override
    public String toString(){

        String result = photos.photo[0].toString();

        return result;



    }

}
