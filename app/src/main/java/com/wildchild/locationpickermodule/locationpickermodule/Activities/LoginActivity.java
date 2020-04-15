package com.wildchild.locationpickermodule.locationpickermodule.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class LoginActivity extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        emailInput = findViewById(R.id.email_edit_text);
        passwordInput = findViewById(R.id.password_edit_text);
    }

    public void didPressLogin(View view) {
        this.performFormValidation(new CompletionHandler<String>() {
            @Override
            public void onSuccess(String response) {
                // do perform login
                User user = new User(emailInput.getText().toString(), passwordInput.getText().toString());

                performLoginWith(user, new CompletionHandler<User>() {
                    @Override
                    public void onSuccess(User user) {
                        // Save user to prefs .then
                        Prefs.with(LoginActivity.this).write("user_id", user.getId());
                        Prefs.with(LoginActivity.this).write("email", user.getEmail());
                        User.currentUser = user;
                        // perform Intent
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        // pop up alert
                        Utilities.AlertDialogOneBuilder(LoginActivity.this,
                                "Login error",
                                e.getMessage(),
                                R.drawable.ic_done_white,
                                (dialog, which) -> {
                                    // do smthg w/ positive button
                                }).show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                //do pop up alert
                Utilities.AlertDialogOneBuilder(LoginActivity.this,
                        "Login error",
                        e.getMessage(),
                        R.drawable.ic_done_white,
                        (dialog, which) -> {
                            // do smthg w/ positive button
                        }).show();
            }
        });
    }

    private void performLoginWith(User user, CompletionHandler<User> completionHandler) {
        UserApiService apiService = RetrofitServiceProvider.getUserApiService();
        apiService.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("Response : " + response);
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Loaded user " + response.body(),
                            Toast.LENGTH_LONG).show();
                    completionHandler.onSuccess(response.body());
                } else {
                    // handle error or empty
                    Utilities.AlertDialogOneBuilder(LoginActivity.this,
                            "Login error",
                            "Please make sure your credentials are okey",
                            R.drawable.ic_done_white,
                            (dialog, which) -> {
                        // do smthg w/ positive button
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG)
                        .show();
                completionHandler.onFailure(t);
            }
        });
    }

    private void performFormValidation(CompletionHandler<String> completionHandler) {
        if (emailInput.getText().length() > 4 && passwordInput.getText().length() > 4) {
            completionHandler.onSuccess("");
        } else {
            completionHandler.onFailure(new Exception("Invalid lenght content"));
        }
    }
}