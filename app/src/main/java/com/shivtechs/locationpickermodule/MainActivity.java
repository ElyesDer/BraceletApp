package com.shivtechs.locationpickermodule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shivtechs.locationpickermodule.Adapters.WatchAdapter;
import com.shivtechs.locationpickermodule.Models.Bracelet;
import com.shivtechs.locationpickermodule.Models.ViewType;
import com.shivtechs.locationpickermodule.ViewHolders.RowType;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final int ADDRESS_PICKER_REQUEST = 1020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapUtility.apiKey = getResources().getString(R.string.api_key);

        List<RowType> bracelets = new ArrayList<>() ;
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v1));
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v2));
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v1));
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v2));
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v1));


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        WatchAdapter adapter = new WatchAdapter(bracelets);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
