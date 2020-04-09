package com.example.recviewfragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolder {
    @GET("artists")                                                                                   //https://jsonplaceholder.typicode.com/posts on API
    Call<List<ItemList>> getItemList();                                                                     //get the list of Posts from API

    @GET("artists/{id}")
    Call<List<ItemList>> getItemList(@Path("id") int id);
}
