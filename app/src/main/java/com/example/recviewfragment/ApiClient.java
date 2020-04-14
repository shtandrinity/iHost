package com.example.recviewfragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApiClient {
    private static String baseURL = "https://3b6e8032.ngrok.io/";
    private static Retrofit retrofit = null;
    private static JsonPlaceHolder instance = null;

    private static Retrofit getApiClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    static JsonPlaceHolder getInterface(){
        if (instance == null) {
            instance = getApiClient().create(JsonPlaceHolder.class);
        }
        return instance;
    }
}
