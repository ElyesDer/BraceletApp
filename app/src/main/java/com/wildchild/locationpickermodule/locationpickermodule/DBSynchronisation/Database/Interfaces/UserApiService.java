package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces;

import androidx.annotation.Nullable;

import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Annotations.Retry;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.Bracelet;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApiService {
    @POST("account/login")
    Call<User>login(@Body User user);
}
