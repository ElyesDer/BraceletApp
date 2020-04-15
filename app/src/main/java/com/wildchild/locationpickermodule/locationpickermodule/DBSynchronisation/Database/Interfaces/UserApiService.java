package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces;

import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Annotations.Retry;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.Bracelet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApiService {
    @Retry
    @POST("bracelets")
    Call<Bracelet> login();
}
