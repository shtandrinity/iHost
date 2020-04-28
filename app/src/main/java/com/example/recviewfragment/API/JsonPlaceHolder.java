package com.example.recviewfragment.API;

import com.example.recviewfragment.Model.ItemArtist;
import com.example.recviewfragment.Model.ItemHost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

//RETROFIT post requests -- https://www.youtube.com/watch?v=GP5OyYDu_mU&t=5s

public interface JsonPlaceHolder {
    @GET("artists")
    Call<List<ItemArtist>> getArtistList();

    @GET("hosts")
    Call<List<ItemHost>> getHostList();

    @GET("hosts")                                                                                   //Search HOSTS by login
    Call<List<ItemHost>> getHostByLogin(@Query("login") String login);

    @GET("artists")
    Call<List<ItemArtist>> getArtistByDeviceID(@Query("uniqueId") String id);

    @GET("artists")
    Call<List<ItemArtist>> getArtistsByHostID(@Query("userId") int id);

    @POST("hosts")
    Call<ItemHost> createHost(@Body ItemHost itemHost);

    @POST("artists")
    Call<ItemArtist> createArtist(@Body ItemArtist itemArtist);

    @PATCH("artists/{id}")
    Call<ItemArtist> updateArtistFields(@Path("id") int id, @Body ItemArtist artistBody);

    @DELETE("artists/{id}")
    Call<Void> deleteArtist(@Path("id") int id);
}
