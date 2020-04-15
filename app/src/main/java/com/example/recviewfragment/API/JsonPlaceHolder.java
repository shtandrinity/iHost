package com.example.recviewfragment.API;

import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.Model.ItemHost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

//RETROFIT post requests -- https://www.youtube.com/watch?v=GP5OyYDu_mU&t=5s

public interface JsonPlaceHolder {
    @GET("artists")
    Call<List<ItemArtist>> getItemList();

    @GET("hosts/{id}")
    Call<ItemHost> getHostByID(@Path("id") int id);

    @GET("hosts")                                                                                   //Search HOSTS by login
    Call<List<ItemHost>> getHostByLogin(@Query("login") String login);

    @POST("hosts")
    Call<ItemHost> createHost(@Body ItemHost itemHost);
}
