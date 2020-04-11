package com.benrostudios.bakingapp.network;

import com.benrostudios.bakingapp.network.response.RecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    public static final String Base_URL = "http://go.udacity.com/";
    @GET("android-baking-app-json")
    Call<List<RecipeResponse>> getRecipe();
}
