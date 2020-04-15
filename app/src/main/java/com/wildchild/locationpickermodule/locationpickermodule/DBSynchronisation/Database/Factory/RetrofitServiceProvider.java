package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Factory;

import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.BraceletApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceProvider<T> {
    private static final String BASE_URL = "https://bracletapp.herokuapp.com/";
    private static Retrofit retrofit = null;

    public static BraceletApiService getBraceletApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RetryCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(BraceletApiService.class);
    }

    public static BraceletApiService getClientApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RetryCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(BraceletApiService.class);
    }



}
