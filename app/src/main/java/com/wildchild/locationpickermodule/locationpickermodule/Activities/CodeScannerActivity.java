package com.wildchild.locationpickermodule.locationpickermodule.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Factory.RetrofitServiceProvider;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.BraceletApiService;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.CompletionHandler;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.Bracelet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeScannerActivity extends AppCompatActivity {
    private static final int RC_PERMISSION = 10;
    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;
    private Bracelet deviceToPair = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        mCodeScanner = new CodeScanner(this, findViewById(R.id.scanner));

        mCodeScanner.setDecodeCallback(result -> {
            requestBraceletInfoWith(result.getText(), new CompletionHandler<Bracelet>() {
                @Override
                public void onSuccess(Bracelet response) {
                    deviceToPair = response;
                    doFinally();
                }

                @Override
                public void onFailure(Throwable e) {
                    deviceToPair = null;
                    doFinally();

                }

                @Override
                public void doFinally() {
                    runOnUiThread(() -> {
                        ScanResultDialog dialog = new ScanResultDialog(CodeScannerActivity.this, deviceToPair);
                        dialog.setOnDismissListener(d -> mCodeScanner.startPreview());
                        dialog.show();
                    });
                }
            });
        });

        mCodeScanner.setErrorCallback(error -> runOnUiThread(
                () ->
                        Toast.makeText(this, "Error happened while scanning", Toast.LENGTH_LONG).show()
                )
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
    }

    private void requestBraceletInfoWith(String id, CompletionHandler<Bracelet> completionHandler) {
        BraceletApiService apiService = RetrofitServiceProvider.getBraceletApiService();
        apiService.getBracelet(id).enqueue(new Callback<Bracelet>() {
            @Override
            public void onResponse(Call<Bracelet> call, Response<Bracelet> response) {
                System.out.println("Response : " + response);
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Loaded bracelet info",
                            Toast.LENGTH_LONG).show();
                    completionHandler.onSuccess(response.body());
                } else {
                    // handle error or empty
                }
            }

            @Override
            public void onFailure(Call<Bracelet> call, Throwable t) {
                System.out.println("Failure for error  : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG)
                        .show();
                completionHandler.onFailure(t);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}