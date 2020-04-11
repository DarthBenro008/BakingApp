package com.benrostudios.bakingapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit client = null;

    public static Retrofit getClient() {
        if(client == null){
            client = new Retrofit.Builder()
                    .baseUrl(ApiService.Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return client;
    }
}
