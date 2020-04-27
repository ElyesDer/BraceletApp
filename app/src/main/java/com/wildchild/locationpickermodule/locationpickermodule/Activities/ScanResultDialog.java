package com.wildchild.locationpickermodule.locationpickermodule.Activities;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;

import com.squareup.picasso.Picasso;
import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Factory.RetrofitServiceProvider;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.BraceletApiService;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.CompletionHandler;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.Bracelet;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.SResponse;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanResultDialog extends AppCompatDialog {

    private Context context;
    private Bracelet bracelet;

    public ScanResultDialog(@NonNull Context context, @Nullable Bracelet result) {
        super(context, resolveDialogTheme(context));
        this.context = context;
        this.bracelet = result;
        setTitle("QR CODE RESULT");
        setContentView(R.layout.dialog_scan_result);

        if (result != null) {
            // load image and bracelet model
            Picasso.get().load(bracelet.getUrl()).into(((ImageView) findViewById(R.id.imageView4)));
            ((TextView) findViewById(R.id.textView3)).setText(result.getmodel() + result.getVersion());
        } else {
            // error requesting watch
            ((TextView) findViewById(R.id.result)).setText("Couldn't request bracelet from server. Please make sure you are connected to internet and retry");
            findViewById(R.id.confirmButton).setVisibility(View.GONE);

        }


        findViewById(R.id.confirmButton).setOnClickListener(v -> {
            //noinspection ConstantConditions
//            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
//                    .setPrimaryClip(ClipData.newPlainText(null, result.getText()));
//            Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show();

            doPairBraceletWithUserID(User.currentUser.getId(), this.bracelet, new CompletionHandler<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    dismiss();
                }

                @Override
                public void onFailure(Throwable e) {
                    showPairError(true);
                }

                @Override
                public void doFinally() {

                }
            });
        });
        //noinspection ConstantConditions
        findViewById(R.id.close).setOnClickListener(v -> dismiss());
    }

    private void showPairError(Boolean show) {
        if (show) {
            findViewById(R.id.format).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.format).setVisibility(View.VISIBLE);
        }
    }


    private void doPairBraceletWithUserID(String id, Bracelet bracelet, CompletionHandler<Boolean> completionHandler) {
        BraceletApiService apiService = RetrofitServiceProvider.getBraceletApiService();
        apiService.pairDeviceWith(bracelet.getid_qr(), id).enqueue(new Callback<SResponse>() {
            @Override
            public void onResponse(Call<SResponse> call, Response<SResponse> response) {
                System.out.println("Response : " + response);
                if (response.body() != null) {
                    Toast.makeText(context, "Loaded bracelet info",
                            Toast.LENGTH_LONG).show();
                    completionHandler.onSuccess(true);
                } else {
                    // handle error or empty
                }
            }

            @Override
            public void onFailure(Call<SResponse> call, Throwable t) {
                System.out.println("Failure for error  : " + t.getMessage());
                Toast.makeText(context, "Failure " + t.getMessage(), Toast.LENGTH_LONG)
                        .show();
                completionHandler.onFailure(t);
            }
        });
    }

    private static int resolveDialogTheme(@NonNull Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(androidx.appcompat.R.attr.alertDialogTheme, outValue, true);
        return outValue.resourceId;
    }
}