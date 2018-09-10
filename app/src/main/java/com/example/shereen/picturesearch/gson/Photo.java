package com.example.shereen.picturesearch.gson;

public class Photo {

    String id;
    String owner;
    String secret;
    String server;
    int farm;
    String title;
    int ispublic;
    int isfriend;
    int isfamily;

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public int getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public int getIspublic() {
        return ispublic;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public int getIsfamily() {
        return isfamily;
    }

    @Override
    public String toString(){

        String result = "--> "+getId()+" "+getOwner()+" "+getTitle();
        return result;

    }
}
