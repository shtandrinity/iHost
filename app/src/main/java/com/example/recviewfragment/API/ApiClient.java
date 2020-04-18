package com.example.recviewfragment.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String baseURL = "https://ae5d4e94.ngrok.io/";
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

    public static JsonPlaceHolder getInterface(){
        if (instance == null) {
            instance = getApiClient().create(JsonPlaceHolder.class);
        }
        return instance;
    }
}
