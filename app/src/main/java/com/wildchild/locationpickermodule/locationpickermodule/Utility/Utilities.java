package com.wildchild.locationpickermodule.locationpickermodule.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Utilities {
    public static AlertDialog.Builder AlertDialogOneBuilder(
            Context context,
            String title,
            String message,
            @Nullable int icon,
            DialogInterface.OnClickListener positiveOnClickListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, positiveOnClickListener)
                .setIcon(icon);
    }

    private static String preprocessDate(String raw) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
        try {
            String finalStr = outputFormat.format(inputFormat.parse(raw));
            return finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getStringFromTimestamp(String timestamp) {
        String result = "Moments ago";
        try {
            String preprocessed = preprocessDate(timestamp);

            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date past = format.parse(preprocessed);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());


            if (seconds < 60) {
                result = seconds + " seconds ago";
            } else if (minutes < 60) {
                result = minutes + " minutes ago";

            } else if (hours < 24) {
                result = hours + " hours ago";
            } else {
                result = days + " days ago";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
