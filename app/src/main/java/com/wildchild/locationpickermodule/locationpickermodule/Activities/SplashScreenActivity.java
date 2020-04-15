package com.wildchild.locationpickermodule.locationpickermodule.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wildchild.locationpickermodule.R;

import es.dmoral.prefs.Prefs;

public class SplashScreenActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        int TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            String userId = Prefs.with(this).read("user_id","0");
            Intent i ;
            if (userId.equals("0")){
                i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            }else{
                i = new Intent(SplashScreenActivity.this, MainActivity.class);
            }
            startActivity(i);
            finish();
        }, TIME_OUT);

    }




}
