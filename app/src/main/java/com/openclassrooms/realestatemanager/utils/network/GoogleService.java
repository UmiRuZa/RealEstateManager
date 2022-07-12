package com.openclassrooms.realestatemanager.utils.network;

import com.openclassrooms.realestatemanager.placeholder.PlaceholderContent;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleService {
    @GET("nearbysearch/json")
    Call<PlaceholderContent.PlaceholderItem> getNearbyResidence(@Query("location") String location, @Query("type") String type, @Query("key") String key);

    //OkHttpClient okhttpClient = new OkHttpClient();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
