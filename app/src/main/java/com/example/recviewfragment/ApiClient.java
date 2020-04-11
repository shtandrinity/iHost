package com.example.recviewfragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String baseURL = "https://6e0b7c2d.ngrok.io/";
    public static Retrofit retrofit = null;
    public static JsonPlaceHolder instance = null;

    public static Retrofit getApiClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static JsonPlaceHolder getInterface(){
        if (instance == null) {
            instance = getApiClient().create(JsonPlaceHolder.class);
        }
        return instance;
    }
}
