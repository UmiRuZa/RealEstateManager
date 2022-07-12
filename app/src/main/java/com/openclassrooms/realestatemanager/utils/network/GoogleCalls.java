package com.openclassrooms.realestatemanager.utils.network;

import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.placeholder.PlaceholderContent;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleCalls {

    // Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable PlaceholderContent.PlaceholderItem residence);
        void onFailure();
    }

    // Public method to start fetching users following by Jake Wharton
    public static void fetchNearbyResidence(Callbacks callbacks, String location, String type, String key){

        // Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        // Get a Retrofit instance and the related endpoints
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);

        // Create the call on Github API
        Call<PlaceholderContent.PlaceholderItem> call = googleService.getNearbyResidence(location, type, key);
        // Start the call
        call.enqueue(new Callback<PlaceholderContent.PlaceholderItem>() {

            @Override
            public void onResponse(Call<PlaceholderContent.PlaceholderItem> call, Response<PlaceholderContent.PlaceholderItem> response) {
                // Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<PlaceholderContent.PlaceholderItem> call, Throwable t) {
                // Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
