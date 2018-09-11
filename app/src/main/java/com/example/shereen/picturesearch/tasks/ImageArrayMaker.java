package com.example.shereen.picturesearch.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.shereen.picturesearch.entity.SingleImage;
import com.example.shereen.picturesearch.gson.Photo;
import com.example.shereen.picturesearch.gson.TopLevel;

import java.util.ArrayList;
import java.util.List;

public class ImageArrayMaker extends AsyncTask<Void,Void,Void> {

    List<SingleImage> imageUrls = new ArrayList<>();
    TopLevel topLevel;
    public AsyncResponse delegate = null;



    public ImageArrayMaker(TopLevel topLevel,AsyncResponse delegate){
        this.topLevel = topLevel;
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... params) {

        makeImageArrayList();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("Done execution");
        delegate.processFinish(imageUrls);

    }


    public void makeImageArrayList(){

        int len = topLevel.getPhotos().getPhoto().length;
        System.out.println("length"+len);

        SingleImage singleImage;


        for (Photo photo : topLevel.getPhotos().getPhoto()){

            String tempUrl = "https://farm"
                    + photo.getFarm() + ".staticflickr.com/"   //farm-id
                    + photo.getServer() + "/"                 //server-id
                    + photo.getId() + "_"          //id
                    + photo.getSecret() + ".jpg";       //secret

            singleImage = new SingleImage();
            singleImage.setImageUrl(tempUrl);
            singleImage.setTitle(photo.getTitle());
            imageUrls.add(singleImage);
        }

    }

}
