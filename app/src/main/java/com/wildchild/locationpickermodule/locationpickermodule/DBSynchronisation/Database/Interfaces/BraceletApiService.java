package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces;

import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Annotations.Retry;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.Bracelet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface BraceletApiService {
    @Retry
    @GET("bracelets")
    Call<List<Bracelet>> getBracelets();

}
