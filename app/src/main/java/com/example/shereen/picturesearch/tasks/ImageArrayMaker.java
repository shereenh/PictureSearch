package com.example.shereen.picturesearch.tasks;

import com.example.shereen.picturesearch.entity.SingleImage;
import com.example.shereen.picturesearch.gson.Photo;
import com.example.shereen.picturesearch.gson.TopLevel;

import java.util.ArrayList;
import java.util.List;

public class ImageArrayMaker {

    List<SingleImage> imageUrls = new ArrayList<>();


    public ImageArrayMaker(){

    }


    public List<SingleImage> makeImageArrayList(TopLevel topLevel){

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

//        for (SingleImage image: imageUrls){
//            System.out.println(image);
//        }

        return imageUrls;

    }

}
