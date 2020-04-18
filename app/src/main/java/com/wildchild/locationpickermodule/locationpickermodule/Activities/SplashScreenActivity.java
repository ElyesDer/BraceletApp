package com.wildchild.locationpickermodule.locationpickermodule.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Factory.RetrofitServiceProvider;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.CompletionHandler;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.UserApiService;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.User;
import com.wildchild.locationpickermodule.locationpickermodule.Utility.Utilities;

import es.dmoral.prefs.Prefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    Intent i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        int TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            String userId = Prefs.with(this).read("user_id", "0");

            if (userId.equals("0")) {
                i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            } else {
                // here we need to load Current User' Object , 2 reason => some info did change , two , to use User.currentUser

                loadCurrentUserWith(userId, new CompletionHandler<User>() {
                    @Override
                    public void onSuccess(User response) {
                        User.currentUser = response;
                        i = new Intent(SplashScreenActivity.this, MainActivity.class);
                        doFinally();
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Utilities.AlertDialogOneBuilder(SplashScreenActivity.this,
                                "Login error",
                                "Please login to continue",
                                R.drawable.ic_done_white,
                                (dialog, which) -> {
                                    // do smthg w/ positive button
                                }).show();
                        i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        doFinally();
                    }

                    @Override
                    public void doFinally() {
                        startActivity(i);
                        finish();
                    }
                });


            }

        }, TIME_OUT);

    }

    private void loadCurrentUserWith(String id, CompletionHandler<User> completionHandler) {

        UserApiService apiService = RetrofitServiceProvider.getUserApiService();
        apiService.getUser(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("Response : " + response);
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Loaded user " + response.body(),
                            Toast.LENGTH_LONG).show();
                    completionHandler.onSuccess(response.body());
                } else {
                    // handle error or empty

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG)
                        .show();
                System.out.println(t.getStackTrace());
                completionHandler.onFailure(t);
            }
        });

    }


}
