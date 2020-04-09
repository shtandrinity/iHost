package com.example.recviewfragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

//RETROFIT post requests -- https://www.youtube.com/watch?v=GP5OyYDu_mU&t=5s

public interface JsonPlaceHolder {
    @GET("artists")                                                                                   //https://jsonplaceholder.typicode.com/posts on API
    Call<List<ItemArtist>> getItemList();                                                                     //get the list of Posts from API

    @POST("hosts")
    Call<ItemHost> createArtist(@Body ItemHost itemHost);
}
