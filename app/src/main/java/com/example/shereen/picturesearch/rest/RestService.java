package com.example.shereen.picturesearch.rest;

import com.example.shereen.picturesearch.gson.TopLevel;

import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RestService {

@GET("services/rest/?method=flickr.photos.search")
io.reactivex.Observable<TopLevel> getStarredRepositories1(@Query("api_key") String API_KEY,@Query("text") String searchContent,
                                                              @Query("safe_search") String SAFE_SEARCH,@Query("format") String FORMAT,
                                                              @Query("nojsoncallback") String NO_JSON_CALLBACK,@Query("per_page") String PER_PAGE);


}
