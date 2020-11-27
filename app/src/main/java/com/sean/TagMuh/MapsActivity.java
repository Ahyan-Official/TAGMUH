package com.sean.TagMuh;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.ResultReceiver;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.GeoPoint;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener  {



    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    private LatLng mCenterLatLong;


    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private Location mylocation;
    private GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    TextView tvLocation;
    LocationListener locationListener;
    GoogleMap mMap;
    public  static final int PERMISSIONS_RECORD_REQUEST = 122;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    TextView mLocationMarkerText;
    private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 6;
    TextView mLocationText;
    Context mContext;
    Toolbar mToolbar;

    TextView tvSearch;

    String shopId;

    Button btnSaveLocation;

    Location loc;

    boolean click = true;

    TextView etLookingFor;
    String location_country ="Pakistan",location_city="Lahore";
    private DatabaseReference mDatabaseReference;
    String uuid,type;

    ImageButton imBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mContext = this;

        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));

        shopId = getIntent().getStringExtra("shopId");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        mLocationAddress = (EditText) findViewById(R.id.Address);
        mLocationText = (TextView) findViewById(R.id.Locality);
        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);

        etLookingFor = (TextView) findViewById(R.id.etLookingFor);

        tvSearch = (TextView) findViewById(R.id.tvSearch);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // getSupportActionBar().setTitle(getResources().getString(R.string.app_name));


        mLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAutocompleteActivity();

            }


        });
        mapFragment.getMapAsync(this);

        mResultReceiver = new AddressResultReceiver(new Handler());



        setUpGClient();


//        tvSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openAutocompleteActivity();
//            }
//        });



        //String apiKey = "asdasdasdas";
        String apiKey = "AIzaSyBBxYN3O30EheBMlJTH2Afyi5ZJsGhitxI";
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

// Create a new Places client instance.





        final String uuid1 = UUID.randomUUID().toString().toUpperCase();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Share Location").child(uuid1).child(uuid);


        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //final String uuid = UUID.randomUUID().toString().toUpperCase();




                mDatabaseReference.child("latitude").setValue(loc.getLatitude());
                mDatabaseReference.child("longitude").setValue(loc.getLongitude());




            }
        });


















        imBack = (ImageButton)findViewById(R.id.imBack);


        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });






































    }








    public void tt(String name){
        Toast.makeText(getApplicationContext(),name, Toast.LENGTH_SHORT).show();
    }
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.e("ChatStart==========","location====================");


                click = false;
                btnSaveLocation.setText("Loading..");
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;


                mMap.clear();
                click = true;

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    loc = mLocation;
                    startIntentService(mLocation);
                    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);


                    Log.e("ChatEnd==========","location====================");
                    btnSaveLocation.setText("Share Location");



//                    Geocoder geocoder;
//                    List<Address> addresses;
//                    geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
//
//                    try {
//
//                        addresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
//                        String country = addresses.get(0).getCountryName();
//                        String postalCode = addresses.get(0).getPostalCode();
//                        String knownName = addresses.get(0).getFeatureName();
//                        location_country = country;
//                        location_city = city;
//
//                        //tvSearch.setText(address+" "+state+" "+city+" "+country);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    private synchronized void setUpGClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(MapsActivity.this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }






    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

                    mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.





                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(MapsActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }

                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But  could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(MapsActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        //finish();
                        break;
                }
                break;
        }


        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                //Place place = PlaceAutocomplete.getPlace(mContext, data);
                com.google.android.libraries.places.api.model.Place place = Autocomplete.getPlaceFromIntent(data);

                tvSearch.setText(place.getName().toString());
                // TODO call location based filter


                LatLng latLong;


                latLong = place.getLatLng();

                loc.setLatitude(latLong.latitude);
                loc.setLongitude(latLong.longitude);

                //mLocationText.setText(place.getName() + "");

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(15f).tilt(0).build();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


//                Geocoder geocoder;
//                List<Address> addresses;
//                geocoder = new Geocoder(this, Locale.getDefault());
//
//                try {
//
//                    addresses = geocoder.getFromLocation(latLong.latitude, latLong.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                    String city = addresses.get(0).getLocality();
//                    String state = addresses.get(0).getAdminArea();
//                    String country = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
//                    String knownName = addresses.get(0).getFeatureName();
//
//                    tvSearch.setText(address+" "+state+" "+city+" "+country);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        int permissionLocation = ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

            getMyLocation();

        }
    }



    public static boolean isLocationEnabled(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        } else {

            return false;
        }
    }



    private void changeMap(Location location) {



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;



            Log.e("latlong",location.getLatitude()+" "+location.getLongitude());
            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            loc = location;
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).tilt(0).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));



            mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            startIntentService(location);


//            Geocoder geocoder;
//            List<Address> addresses;
//            geocoder = new Geocoder(this, Locale.getDefault());
//
//            try {
//
//                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//
//                tvSearch.setText(address+" "+state+" "+city+" "+country);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    protected void displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
                // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mAddressOutput);
            //mLocationText.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openAutocompleteActivity() {
        // The autocomplete activity requires Google Play Services to be available. The intent
        // builder checks this and throws an exception if it is not the case.


        List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields) //NIGERIA
                .build(this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);

//            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
//            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);


    }



    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }




    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }


    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu.add(0,2,0, R.string.ShowNHide).setIcon(R.drawable.newhidenshowdark).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //menu.add(0,3,0,getString(R.string.Demo)).setIcon(R.drawable.mark).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //MenuItemCompat.setShowAsAction(menu.add(0,7,0,getString(R.string.Demo)).setIcon(R.drawable.location_bar), MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==6){


            openAutocompleteActivity();


        }



        return super.onOptionsItemSelected(item);
    }




























    @Override
    public void onBackPressed() {




        super.onBackPressed();




    }







    @Override
    protected void onResume() {
        super.onResume();

        //SharedPreferences sp = getSharedPreferences("RVPOSITIONVISIBLE", Context.MODE_PRIVATE);
        //lastFirstVisiblePosition = sp.getInt("rvPosition",0);

        //((LinearLayoutManager) recyclerViewVisible.getLayoutManager()).scrollToPositionWithOffset(lastFirstVisiblePosition,0);
    }

    private boolean isWifiAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    private boolean isNetWorkAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if(pd!=null){
            pd.dismiss();
        }
        if (recyclerViewVisible != null) {
            recyclerViewVisible.setAdapter(null);
        }
        rl.removeAllViews();
        rl.removeAllViewsInLayout();
        if (strReload.equals("h")) {

            editor.remove(getResources().getString(R.string.a_codeV));
            editor.remove(getResources().getString(R.string.a_codeSM));
            editor.remove(getResources().getString(R.string.a_codeSW));
            editor.remove(getResources().getString(R.string.a_codeW));
            editor.remove(getResources().getString(R.string.a_codeU));
            editor.remove(getResources().getString(R.string.a_codeCC));
            editor.remove(getResources().getString(R.string.a_codeFS));
            editor.remove(getResources().getString(R.string.a_codeIC));
            editor.remove(getResources().getString(R.string.a_codeFIC));
            editor.commit();
        }

        //mDatabaseVisible = null;
        //mDatabaseShopIdWorld = null;
        //mDatabseCheckLike = null;
        //mDatabaseShopLike = null;
        //mDatabaseUserShow = null;
        AB = null;
        sr = null;
        recyclerViewVisible = null;
        //shopId = null;
        //tv = null;
        mRecorder = null;
        //mFileName = null;

        actionBarRl = null;

        actionBarRlGoing = false;
        show = false;
        insidesNBlind = null;
        shopMainList = null;
        shopWhoAndOnWhichYou = null;
        userShow = null;


        llm = null;

        recycleRl = null;
        recordingBtn = null;
        stopBtn = null;
        recyclerVieTouch = false;
        player = null;

        r = 0;

        stopRecordingBtn = null;
        sendRecordingBtn = null;

        //reloadBtn = null;
        if(pd!=null){
            pd.dismiss();
            //pd = null;
        }

        //strReload = null;

        rl = null;
        if(firebaseRecyclerAdapter!=null && fire.equals("f")){
            firebaseRecyclerAdapter.cleanup();
            firebaseRecyclerAdapter = null;
        }
        if(firebaseRecyclerAdapter!=null && fire.equals("w")){
            //firebaseRecyclerAdapter.cleanup();
            firebaseRecyclerAdapter = null;
        }

        firebaseRecyclerAdapter = null;
        //fire = null;
        rcPosition = 0;
        sp = null;
        editor = null;
        visibleActivityWeakReference = null;
        weakActivity = null;*/

//        finish();
//        System.gc();
//        Runtime.getRuntime().gc();
//        finish();

    }




    class as extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.runFinalization();
            Runtime.getRuntime().gc();
            System.gc();
            return null;
        }
    }





    public void deletingProcesses(){
        //int s = android.os.Process.myPid();
        //android.os.Process.killProcess(s);

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> pidss = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for(int i = 0; i < pidss.size(); i++){
            ActivityManager.RunningTaskInfo info = pidss.get(i);
            Process.killProcess(info.id);
            Process.killProcess(info.numActivities);
            Process.killProcess(info.numRunning);
            Process.killProcess(info.describeContents());
        }
        ActivityManager activityManager1 = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> pidss1 = activityManager1.getRunningAppProcesses();

        for(int i = 0; i < pidss1.size(); i++){
            ActivityManager.RunningAppProcessInfo info = pidss1.get(i);
            Process.killProcess(info.importance);
            Process.killProcess(info.describeContents());
            Process.killProcess(info.lru);

        }
    }

    public void delet(){


        finish();
        System.gc();
        Runtime.getRuntime().gc();
        finish();
    }

}
