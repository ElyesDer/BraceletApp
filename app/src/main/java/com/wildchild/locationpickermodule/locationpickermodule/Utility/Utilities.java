package com.wildchild.locationpickermodule.locationpickermodule.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;


public class Utilities {
    public static AlertDialog.Builder AlertDialogOneBuilder(
            Context context,
            String title,
            String message ,
            @Nullable int icon,
            DialogInterface.OnClickListener positiveOnClickListener){
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, positiveOnClickListener)
                .setIcon(icon);
    }
}
