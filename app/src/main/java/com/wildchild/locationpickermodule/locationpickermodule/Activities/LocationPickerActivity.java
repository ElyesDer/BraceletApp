//  The MIT License (MIT)

//  Copyright (c) 2018 Intuz Solutions Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.wildchild.locationpickermodule.locationpickermodule.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahc.custombottomsheetbehavior.BottomSheetBehaviorGoogleMapsLike;
import com.mahc.custombottomsheetbehavior.MergedAppBarLayout;
import com.mahc.custombottomsheetbehavior.MergedAppBarLayoutBehavior;
import com.squareup.picasso.Picasso;
import com.wildchild.locationpickermodule.BuildConfig;
import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.Adapters.HistoryAdapter;
import com.wildchild.locationpickermodule.locationpickermodule.Adapters.WatchPagerAdapter;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Factory.RetrofitServiceProvider;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.BraceletApiService;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces.CompletionHandler;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.Bracelet;
import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.History;
import com.wildchild.locationpickermodule.locationpickermodule.Utility.MapUtility;
import com.wildchild.locationpickermodule.locationpickermodule.Utility.Utilities;
import com.wildchild.locationpickermodule.locationpickermodule.Utility.directionhelpers.FetchURL;
import com.wildchild.locationpickermodule.locationpickermodule.Utility.directionhelpers.TaskLoadedCallback;
import com.wildchild.locationpickermodule.locationpickermodule.Utility.models.LatLng;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

enum ELocationType {
    history,
    current,
    owner
}

class LocationType {
    ELocationType locationType;
    String value;

    public LocationType(ELocationType locationType) {
        this.locationType = locationType;
    }

    public LocationType(ELocationType locationType, String value) {
        this.locationType = locationType;
        this.value = value;
    }
}

public class LocationPickerActivity extends AppCompatActivity implements
//        OnMapReadyCallback,
//        GoogleMap.OnMarkerClickListener,
        TaskLoadedCallback {
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private final String TAG = LocationPickerActivity.class.getSimpleName();
    private String userAddress = "";
    private String place_id = "";
    private String place_url = " ";
    private MapView map = null;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;
    private boolean mLocationPermissionGranted;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    //Declaration of FusedLocationProviderClient
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<AsyncTask> filterTaskList = new ArrayList<>();
    String regex = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)$";
    Pattern latLongPattern = Pattern.compile(regex);
    private int doAfterPermissionProvided, doAfterLocationSwitchedOn = 1;
    private LocationRequest userLocationRequest;
    private LocationCallback locationCallback;

    //    private TextView imgSearch;
    TextView bottomSheetTitle;
    TextView bottomSheetSubTitle;
    TextView bottomSheetTimer;

    Button button1;
    Button button2;
    Button button3;

    ListView bottomSheetHistoryList;

    private Bracelet currentBracelet;
    private History currentPosition;
    private Marker currentPositionOnMap;


    // owener current loc
    private double mLatitude;
    private double mLongitude;

    // bracelet current loc
    private double currentLatitude;
    private double currentLongitude;


    private List<History> histories = new ArrayList<>();
    private HistoryAdapter historiesAdapter;
    private BottomSheetBehaviorGoogleMapsLike<View> behavior;
    private Polyline currentPolyLine;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private FloatingActionButton floatingIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_location_picker);

        /*
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
            */
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(" ");
        }

 */

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);

        View bottomSheetLayout = bottomSheet.findViewById(R.id.bottom_sheet_layout);
        MergedAppBarLayout mergedAppBarLayout = findViewById(R.id.mergedappbarlayout);

        bottomSheetTitle = (TextView) bottomSheetLayout.findViewById(R.id.bottom_sheet_title);
        bottomSheetTimer = (TextView) bottomSheetLayout.findViewById(R.id.bottomSheetTimer);
        bottomSheetSubTitle = (TextView) bottomSheetLayout.findViewById(R.id.bottomSheetSubTitle);
//        floatingIcon = (FloatingActionButton) bottomSheetLayout.findViewById(R.id.floating_icon);

        bottomSheetHistoryList = (ListView) bottomSheetLayout.findViewById(R.id.history_list);


        Intent i = getIntent();
        if (i != null) {
            Bundle extras = i.getExtras();
            if (extras != null) {
                //userAddress = extras.getString(MapUtility.ADDRESS);
                //temp -> get lat , log from db
                mLatitude = getIntent().getDoubleExtra(MapUtility.LATITUDE, 0);
                mLongitude = getIntent().getDoubleExtra(MapUtility.LONGITUDE, 0);

                currentBracelet = (Bracelet) getIntent().getSerializableExtra("currentBracelet");
            }
        }

        if (currentBracelet != null) {
            bottomSheetTitle.setText(currentBracelet.getModel() != null ? currentBracelet.getModel().getName() : "Bracelet");
           // Picasso.get().load(currentBracelet.getModel().getUrl()).into(((ImageView) findViewById(R.id.imageView4)));

        }

        if (savedInstanceState != null) {
            mLatitude = savedInstanceState.getDouble("latitude");
            mLongitude = savedInstanceState.getDouble("longitude");
            userAddress = savedInstanceState.getString("userAddress");
            currentLatitude = savedInstanceState.getDouble("currentLatitude");
            currentLongitude = savedInstanceState.getDouble("currentLongitude");
        }


        behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                        Log.d("bottomsheet-", "STATE_COLLAPSED");
                        bottomSheet.post(() -> ((NestedScrollView) bottomSheet).fullScroll(bottomSheet.FOCUS_UP));
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                        Log.d("bottomsheet-", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                        Log.d("bottomsheet-", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        bottomSheet.post(() -> ((NestedScrollView) bottomSheet).fullScroll(bottomSheet.FOCUS_UP));

                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                        Log.d("bottomsheet-", "STATE_HIDDEN");
                        bottomSheet.post(() -> ((NestedScrollView) bottomSheet).fullScroll(bottomSheet.FOCUS_UP));

                        break;
                    default:
                        Log.d("bottomsheet-", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        mergedAppBarLayoutBehavior.setToolbarTitle(this.currentBracelet.getModel().getName());
        mergedAppBarLayoutBehavior.setNavigationOnClickListener(v -> behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT));


// VIEW PAGER SETUP
        WatchPagerAdapter adapter = new WatchPagerAdapter(this, this.currentBracelet);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        // LIST VIEW SETUP
        historiesAdapter = new HistoryAdapter(this, this.histories);
        bottomSheetHistoryList.setAdapter(historiesAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottomSheetHistoryList.setNestedScrollingEnabled(false);
        }
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

        behavior.setCollapsible(true);
        behavior.setPeekHeight(100);
        behavior.setAnchorPoint(500); // paralax pager size


        ImageView imgCurrentloc = findViewById(R.id.imgCurrentloc);
        ImageView directionButton = findViewById(R.id.directionImageView);
//        FloatingActionButton txtSelectLocation = findViewById(R.id.fab_select_location);
//        imgSearch = findViewById(R.id.imgSearch);
//        ImageView directionTool = findViewById(R.id.direction_tool);
//        ImageView googleMapTool = findViewById(R.id.google_maps_tool);

        //intitalization of FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        System.out.println("TEST LOCATION : " + fusedLocationProviderClient);

        //define callback of location request
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.d(TAG, "onLocationAvailability: isLocationAvailable =  " + locationAvailability.isLocationAvailable());
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "onLocationResult: " + locationResult);
                if (locationResult == null) {
                    return;
                }

                //show location on map
                switch (doAfterLocationSwitchedOn) {
                    case 1:
//                        startParsingAddressToShow();
                        break;
                    case 2:
                        //on click of imgCurrent
                        showCurrentLocationOnMap(false);
                        break;
                    case 3:
                        //on Click of Direction Tool
                        showCurrentLocationOnMap(true);
                        break;
                }

                //Location fetched, update listener can be removed
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };

        //Prepare for Request for current location
        getLocationRequest();

//        // Try to obtain the map from the SupportMapFragment.
//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        //if you want to open the location on the LocationPickerActivity through intent



        Context ctx = getApplicationContext();
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);


        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        configureMapView();

        requestPermissionsIfNecessary(new String[]{
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });


        if (!MapUtility.isNetworkAvailable(this)) {
            MapUtility.showToast(this, "Please Connect to Internet");
        }


//        imgSearch.setOnClickListener(view -> {
//            if (!Places.isInitialized()) {
//                Places.initialize(LocationPickerActivity.this.getApplicationContext(), MapUtility.apiKey);
//            }
//
//            // Set the fields to specify which types of place data to return.
//            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
//
//
//            // Start the autocomplete intent.
//            Intent intent = new Autocomplete.IntentBuilder(
//                    AutocompleteActivityMode.FULLSCREEN, fields)
//                    .build(LocationPickerActivity.this);
//            LocationPickerActivity.this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//        });

//        txtSelectLocation.setOnClickListener(view -> {
//            Intent intent = new Intent();
//            intent.putExtra(MapUtility.ADDRESS, imgSearch.getText().toString().trim());
//            intent.putExtra(MapUtility.LATITUDE, mLatitude);
//            intent.putExtra(MapUtility.LONGITUDE, mLongitude);
//            intent.putExtra("id", place_id);//if you want place id
//            intent.putExtra("url", place_url);//if you want place url
//            LocationPickerActivity.this.setResult(Activity.RESULT_OK, intent);
//            LocationPickerActivity.this.finish();
//        });
//
        imgCurrentloc.setOnClickListener(view -> {
//
            showCurrentLocationOnMap(false);
            doAfterPermissionProvided = 2;
            doAfterLocationSwitchedOn = 2;
        });
//
//        directionTool.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LocationPickerActivity.this.showCurrentLocationOnMap(true);
//                doAfterPermissionProvided = 3;
//                doAfterLocationSwitchedOn = 3;
//            }
//        });
//
//        googleMapTool.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Default google map
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
//                        "http://maps.google.com/maps?q=loc:" + mLatitude + ", " + mLongitude + ""));
//                LocationPickerActivity.this.startActivity(intent);
//            }
//        });

        directionButton.setOnClickListener(view -> {

            // will provide direction from the available markers on map
            didTapDirectUserToLocation();
        });

        fetchWatchHistoriesWithID(new CompletionHandler<List<History>>() {
            @Override
            public void onSuccess(List<History> response) {
                histories.clear();
                histories.addAll(response);

                // update history tableview
                historiesAdapter.notifyDataSetChanged();
                // update view

                if (histories.size() > 0) {
                    currentPosition = histories.get(0);

                    bottomSheetSubTitle.setText("Last known position :" + currentPosition.getPlace());
                    bottomSheetTimer.setText(Utilities.getStringFromTimestamp(currentPosition.getCreatedAt()));

                    initWithCurrentPosition();
                }

            }

            @Override
            public void onFailure(Throwable e) {

            }

            @Override
            public void doFinally() {

            }
        });


        bottomSheetHistoryList.setOnItemClickListener((parent, view, position, id) -> {
            History history = histories.get(position);
            didTapHistoryWith(new GeoPoint(
                    Double.parseDouble(history.getLatitude()),
                    Double.parseDouble(history.getLongitude())
            ), history.getCreatedAt());
        });

    }

    private void initWithCurrentPosition() {
        if (this.currentPosition != null) {
            this.currentLatitude = Double.parseDouble(this.currentPosition.getLatitude());
            this.currentLongitude = Double.parseDouble(this.currentPosition.getLongitude());

            showLocationOnMap(
                    new GeoPoint(Double.parseDouble(this.currentPosition.getLatitude()),
                            Double.parseDouble(this.currentPosition.getLongitude())),
                    new LocationType(ELocationType.current)
            );
        }
    }

    private void configureMapView() {
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        map.setMaxZoomLevel(20.0);
        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
    }

    private void clearMap() {
        map.getOverlays().clear();
        map.invalidate();
    }

    private void didTapDirectUserToLocation() {
        addTurnByTurn(
                new GeoPoint(this.mLatitude, this.mLongitude),
                new GeoPoint(this.currentLatitude, this.currentLongitude)
        );

    }

    private void addTurnByTurn(GeoPoint from, GeoPoint to) {
        new Thread(() -> {

            RoadManager roadManager = new OSRMRoadManager(LocationPickerActivity.this);

            ArrayList<GeoPoint> waypoints = new ArrayList<>();
            waypoints.add(from);
            waypoints.add(to);

            Road road = roadManager.getRoad(waypoints);

            roadManager.addRequestOption("routeType=fastest");
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            map.getOverlays().add(roadOverlay);


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    map.invalidate();
                }
            });
        }).start();


    }

    @Override
    public void onTaskDone(Object... values) {
//        if (currentPolyLine != null)
//            currentPolyLine.
//        currentPolyLine = mMap.addPolyline((PolylineOptions) values[0]);
    }

    private void didTapHistoryWith(GeoPoint latLng, String locatedAt) {
        //mMap.clear();

        clearMap();

        // Update class field location
        this.currentLatitude = latLng.getLatitude();
        this.currentLongitude = latLng.getLongitude();

        showLocationOnMap(latLng, new LocationType(ELocationType.history, locatedAt));

        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
    }

    public void didTapButton1(View view) {
//        mMap.clear();

        clearMap();
//        currentPositionOnMap = new MarkerOptions()
//                .position(new LatLng(Double.parseDouble(this.currentPosition.getLatitude()), Double.parseDouble(this.currentPosition.getLongitude())))
//                .title(User.currentUser.getFirstname() + "'s" +
//                        this.currentBracelet.getmodel());
//        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_red_800));
//
//        System.out.println("LOGGING LOCATION");
//
//        MarkerOptions currentUserLocation = new MarkerOptions()
//                .position(new LatLng(mLatitude, mLongitude))
//                .title("Your location");
//
//        Marker marker1 = mMap.addMarker(currentPositionOnMap);
//        Marker marker2 = mMap.addMarker(currentUserLocation);
        // Update class field location
        if (this.currentPosition != null && this.currentPosition != null) {
            this.currentLatitude = Double.parseDouble(this.currentPosition.getLatitude());
            this.currentLongitude = Double.parseDouble(this.currentPosition.getLongitude());

            showLocationOnMap(
                    new GeoPoint(Double.parseDouble(this.currentPosition.getLatitude()),
                            Double.parseDouble(this.currentPosition.getLongitude())),
                    new LocationType(ELocationType.current)
            );
            showLocationOnMap(new GeoPoint(mLatitude, mLongitude), new LocationType(ELocationType.owner));

//        getAdressByGeocodingLatLng(marker1, Double.parseDouble(this.currentPosition.getLatitude()), Double.parseDouble(this.currentPosition.getLongitude()));
//        getAdressByGeocodingLatLng(marker2, mLatitude, mLongitude);

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
        } else {
            System.err.println("Null lat long");
        }

    }

    void fetchWatchHistoriesWithID(CompletionHandler<List<History>> completionHandler) {
        BraceletApiService apiService = RetrofitServiceProvider.getBraceletApiService();
        apiService.getBraceletHistory(currentBracelet.getId_qr()).enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                System.out.println("Response : " + response);
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Loaded watches " + response.body().size(),
                            Toast.LENGTH_LONG).show();
                    completionHandler.onSuccess(response.body());
                } else {
                    // handle error or empty
                }
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                System.out.println("Failure for error  : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG)
                        .show();
                completionHandler.onFailure(t);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        } catch (NullPointerException ne) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //after a place is searched
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                userAddress = place.getAddress();
////                imgSearch.setText("" + userAddress);
//                mLatitude = place.getLatLng().latitude;
//                mLongitude = place.getLatLng().longitude;
//                place_id = place.getId();
//                place_url = String.valueOf(place.getWebsiteUri());
//
//                //addMarker();
//
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i(TAG, status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
//            //after location switch on dialog shown
//            if (resultCode != RESULT_OK) {
//                //Location not switched ON
//                Toast.makeText(LocationPickerActivity.this, "Location Not Available..", Toast.LENGTH_SHORT).show();
//
//            } else {
//                // Start location request listener.
//                //Location will be received onLocationResult()
//                //Once loc recvd, updateListener will be turned OFF.
//                Toast.makeText(this, "Fetching Location...", Toast.LENGTH_LONG).show();
//                startLocationUpdates();
//
//            }
//        }
    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        //getSettingsLocation();
        return true;

    }

    private void showLocationOnMap(GeoPoint latLng, LocationType locationType) {
        if (checkAndRequestPermissions()) {
            getAddressByGeoCodingLatLng(latLng, locationType);
        }
    }

    private void showCurrentLocationOnMap(boolean clear) {

        if (checkAndRequestPermissions()) {

            @SuppressLint("MissingPermission")
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        if (clear) {
//                            map.clear();
                            clearMap();
                        }
                        //Go to User Current Location
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        LocationPickerActivity.this.getAddressByGeoCodingLatLng(
                                new GeoPoint(
                                        mLatitude, mLongitude
                                ), new LocationType(ELocationType.owner));

                    } else {
                        //Gps not enabled if loc is null
                        LocationPickerActivity.this.getSettingsLocation();
                        Toast.makeText(LocationPickerActivity.this, "Location not Available", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            lastLocation.addOnFailureListener(e -> {
                //If perm provided then gps not enabled
//                getSettingsLocation();
                Toast.makeText(LocationPickerActivity.this, "Location Not Availabe", Toast.LENGTH_SHORT).show();

            });
        }

    }

    private void addMarker(GeoPoint latLng, String address, LocationType locationType) {

        if (map != null) {

//            MarkerOptions markerOptions;
            try {
                String preAddress = "";
                switch (locationType.locationType) {
                    case current:
                        preAddress = "Current location :";
                        break;
                    case owner:
                        preAddress = "Your location :";
                        break;
                    case history:
                        preAddress = "Located at : " + locationType.value;
                        break;
                }

                Marker startMarker = new Marker(map);
                startMarker.setPosition(latLng);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

                startMarker.setTitle(address);
                map.invalidate();
                map.getController().setCenter(latLng);
//                markerOptions = new MarkerOptions()
//                        .position(latLng)
//                        .title(address);
//                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_red_800));
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
//                mMap.animateCamera(cameraUpdate);
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//                mMap.setOnMarkerClickListener(this);

//                Marker marker = mMap.addMarker(markerOptions);
//                marker.showInfoWindow();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        System.out.println("LOGGING - on Map REady");
//        mMap = googleMap;
//        mMap.clear();
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.getUiSettings().setMapToolbarEnabled(false);
//        if (mMap.isIndoorEnabled()) {
//            mMap.setIndoorEnabled(false);
//        }
//
//
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            // Use default InfoWindow frame
//            @Override
//            public View getInfoWindow(Marker arg0) {
//                return null;
//            }
//
//            // Defines the contents of the InfoWindow
//            @Override
//            public View getInfoContents(Marker arg0) {
//                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
//
//                // Getting the position from the marker
//                LatLng latLng = arg0.getPosition();
//                mLatitude = latLng.latitude;
//                mLongitude = latLng.longitude;
//                TextView tvLat = v.findViewById(R.id.address);
//                tvLat.setText(arg0.getTitle());
//                return v;
//
//            }
//        });
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//
//        // Setting a click event handler for the map
////        mMap.setOnMapClickListener(latLng -> {
////            mMap.clear();
////            mLatitude = latLng.latitude;
////            mLongitude = latLng.longitude;
////            Log.e("latlng", latLng + "");
////            LocationPickerActivity.this.addMarker(new LatLng(mLatitude, mLongitude), "");
////            if (!MapUtility.isNetworkAvailable(LocationPickerActivity.this)) {
////                MapUtility.showToast(LocationPickerActivity.this, "Please Connect to Internet");
////            }
////            LocationPickerActivity.this.getAddressByGeoCodingLatLng(latLng);
////
////        });
//
//        if (checkAndRequestPermissions()) {
//            //startParsingAddressToShow();
//        } else {
//            doAfterPermissionProvided = 1;
//        }
//
//    }

    private void getSettingsLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(userLocationRequest);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    //...
                    if (response != null) {
                        LocationSettingsStates locationSettingsStates = response.getLocationSettingsStates();
                        Log.d(TAG, "getSettingsLocation: " + locationSettingsStates);
                        LocationPickerActivity.this.startLocationUpdates();

                    }
                } catch (ApiException exception) {
                    Log.d(TAG, "getSettingsLocation: " + exception);
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        LocationPickerActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            //...
                            break;
                    }
                }
            }
        });
    }

    /**
     * Show location from intent
     */
//    private void startParsingAddressToShow() {
//        //get address from intent to show on map
//        if (userAddress == null || userAddress.isEmpty()) {
//
//            //if intent does not have address,
//            //cell is blank
////            showCurrentLocationOnMap(false);
//
//        } else
//
//            //check if address contains lat long, then extract
//            //format will be lat,lng i.e 19.23234,72.65465
//            if (latLongPattern.matcher(userAddress).matches()) {
//
//                Pattern p = Pattern.compile("(-?\\d+(\\.\\d+)?)");   // the pattern to search for
//                Matcher m = p.matcher(userAddress);
//
//                // if we find a match, get the group
//                int i = 0;
//                while (m.find()) {
//                    // we're only looking for 2s group, so get it
//                    if (i == 0)
//                        mLatitude = Double.parseDouble(m.group());
//                    if (i == 1)
//                        mLongitude = Double.parseDouble(m.group());
//
//                    i++;
//
//                }
//                //show on map
////                getAddressByGeoCodingLatLng();
//                addMarker();
//
//            } else {
//                //get  latlong from String address via reverse geo coding
//                //Since lat long not present in db
//                if (mLatitude == 0 && mLongitude == 0) {
//                    getLatLngByRevGeoCodeFromAdd();
//                } else {
//                    // Latlong is more accurate to get exact point on map ,
//                    // String address might not be sufficient (i.e Mumbai, Mah..etc)
//                    addMarker();
//                }
//            }
//
//    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("latitude", mLatitude);
        outState.putDouble("longitude", mLongitude);
        outState.putString("userAddress", userAddress);
        outState.putDouble("currentLatitude", currentLatitude);
        outState.putDouble("currentLongitude", currentLongitude);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getAddressByGeoCodingLatLng(GeoPoint latLng, LocationType locationType) {

        //Get string address by geo coding from lat long
        if (latLng.getLatitude() != 0 && latLng.getLongitude() != 0) {

            if (MapUtility.popupWindow != null && MapUtility.popupWindow.isShowing()) {
                MapUtility.hideProgress();
            }

            Log.d(TAG, "getAddressByGeoCodingLatLng: START" + latLng.getLatitude() + " -- " + latLng.getLongitude());
            //Cancel previous tasks and launch this one
            for (AsyncTask prevTask : filterTaskList) {
                //prevTask.cancel(true);
            }

            filterTaskList.clear();
            GetAddressFromLatLng asyncTask = new GetAddressFromLatLng(locationType);
            filterTaskList.add(asyncTask);
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, latLng.getLatitude(), latLng.getLongitude());
        }
    }

//    private void getAddressByGeoCodingLatLng() {
//
//        //Get string address by geo coding from lat long
//        if (mLatitude != 0 && mLongitude != 0) {
//
//            if (MapUtility.popupWindow != null && MapUtility.popupWindow.isShowing()) {
//                MapUtility.hideProgress();
//            }
//
//            Log.d(TAG, "getAddressByGeoCodingLatLng: START");
//            //Cancel previous tasks and launch this one
//            for (AsyncTask prevTask : filterTaskList) {
//                prevTask.cancel(true);
//            }
//
//            filterTaskList.clear();
//            GetAddressFromLatLng asyncTask = new GetAddressFromLatLng();
//            filterTaskList.add(asyncTask);
//            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mLatitude, mLongitude);
//        }
//    }


    @SuppressLint("StaticFieldLeak")
    private class GetAddressFromLatLng extends AsyncTask<Double, Void, String> {
        Double latitude, longitude;
        LocationType locationType;

        public GetAddressFromLatLng(LocationType locationType) {
            this.locationType = locationType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MapUtility.showProgress(LocationPickerActivity.this);
        }

        @Override
        protected String doInBackground(Double... doubles) {
            try {

                latitude = doubles[0];
                longitude = doubles[1];

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(LocationPickerActivity.this, Locale.getDefault());
                StringBuilder sb = new StringBuilder();

                System.out.println("LOGGING with lat , long " + latitude + "" + longitude);
                //get location from lat long if address string is null
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {

                    String address = addresses.get(0).getAddressLine(0);
                    if (address != null)
                        sb.append(address).append(" ");
                    String city = addresses.get(0).getLocality();
                    if (city != null)
                        sb.append(city).append(" ");

                    String state = addresses.get(0).getAdminArea();
                    if (state != null)
                        sb.append(state).append(" ");
                    String country = addresses.get(0).getCountryName();
                    if (country != null)
                        sb.append(country).append(" ");

                    String postalCode = addresses.get(0).getPostalCode();
                    if (postalCode != null)
                        sb.append(postalCode).append(" ");
                    return sb.toString();

                } else {
                    System.out.println("LOGGING returning nulllll");
                    MapUtility.hideProgress();
                    return "";
                }
            } catch (IOException e) {
                System.out.println("LOGGING eerorro");
                MapUtility.hideProgress();
                e.printStackTrace();
                return roundAvoid(latitude) + "," + roundAvoid(longitude);

            }
        }


        @Override
        protected void onPostExecute(String stringAddress) {
            super.onPostExecute(stringAddress);
            LocationPickerActivity.this.userAddress = stringAddress;
            MapUtility.hideProgress();
            addMarker(new GeoPoint(latitude, longitude), stringAddress, locationType);
        }
    }

    double roundAvoid(double value) {
        double scale = Math.pow(10, 6);
        return Math.round(value * scale) / scale;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (AsyncTask task : filterTaskList) {
            task.cancel(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        //Do tasks for which permission was granted by user in onRequestPermission()
        if (!isFinishing() && mLocationPermissionGranted) {
            // perform action required b4 asking permission
            mLocationPermissionGranted = false;
            switch (doAfterPermissionProvided) {
                case 1:
//                    startParsingAddressToShow();
                    break;
                case 2:
//                    showCurrentLocationOnMap(false);
                    break;
                case 3:
//                    showCurrentLocationOnMap(true);
                    break;
            }

        }

    }

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(LocationPickerActivity.this, "Location not Available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userLocationRequest == null) {
            getLocationRequest();
        }

        if (fusedLocationProviderClient == null) {
            return;
        }

        System.out.println("A : "+userLocationRequest);
        System.out.println("B : "+fusedLocationProviderClient);

        fusedLocationProviderClient.requestLocationUpdates(userLocationRequest,
                locationCallback,
                null /* Looper */)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "startLocationUpdates: onSuccess: "))
                .addOnFailureListener((OnFailureListener) e -> {
                    if (e instanceof ApiException) {
                        Log.d(TAG, "startLocationUpdates: " + ((ApiException) e).getMessage());
                    } else {
                        Log.d(TAG, "startLocationUpdates: " + e.getMessage());
                    }
                });

    }

    private void getLocationRequest() {
        System.out.println("LOGGING : getLocationRequest");
        userLocationRequest = new LocationRequest();
        userLocationRequest.setInterval(10000);
        userLocationRequest.setFastestInterval(3000);
        userLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        ArrayList<String> permissionsToRequest = new ArrayList<>();
//        for (int i = 0; i < grantResults.length; i++) {
//            permissionsToRequest.add(permissions[i]);
//        }
//        if (permissionsToRequest.size() > 0) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    permissionsToRequest.toArray(new String[0]),
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

}

