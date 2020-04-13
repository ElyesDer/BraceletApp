package com.wildchild.locationpickermodule.locationpickermodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.Adapters.WatchAdapter;
import com.wildchild.locationpickermodule.locationpickermodule.Models.Bracelet;
import com.wildchild.locationpickermodule.locationpickermodule.Models.ViewType;

import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RecyclerOnItemClickListener;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RowType;
import com.wildchild.locationpickermodule.locationpickermodule.Utility.MapUtility;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final int ADDRESS_PICKER_REQUEST = 1020;
    RecyclerOnItemClickListener mItemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View childView, int position) {
            System.out.println("Clicked On position " + position);
            Intent intent = new Intent(MainActivity.this, LocationPickerActivity.class);
            intent.putExtra(MapUtility.ADDRESS,"Maranello");
            intent.putExtra(MapUtility.LATITUDE, "44.525551");
            intent.putExtra(MapUtility.LONGITUDE, "10.866320");
            startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapUtility.apiKey = getResources().getString(R.string.api_key);

        List<RowType> bracelets = new ArrayList<>();
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v1));
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v2));
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v1));
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v2));
        bracelets.add(new Bracelet(0, "Watch", "Rose", ViewType.v1));


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        WatchAdapter adapter = new WatchAdapter(bracelets, mItemClickListener);
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
