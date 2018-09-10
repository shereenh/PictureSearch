package com.example.shereen.picturesearch.rest;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.shereen.picturesearch.R;
import com.example.shereen.picturesearch.gson.TopLevel;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {


    private static RestClient instance;
    private RestService restService;

    private RestClient() {
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(RestConstants.FLICKR_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);


    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public  io.reactivex.Observable<TopLevel> getStarredRepos1(@NonNull String searchContent){
        return restService.getStarredRepositories1(RestConstants.API_KEY , searchContent, RestConstants.SAFE_SEARCH, RestConstants.FORMAT,
                RestConstants.NO_JSON_CALLBACK,RestConstants.PER_PAGE);
    }

}
