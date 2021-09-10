package com.gptwgl.gohostel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MaterialSearchBar.OnSearchActionListener, OnMapReadyCallback {
    private MaterialSearchBar searchBar;
    private DrawerLayout drawer;
    private FirebaseAuth auth;


    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location mLastknownLocation;
    private LocationCallback locationCallback;

    private  View mapView;
    private final float DEFAULT_ZOOM=16;
    private Button searchbtn;
    private DatabaseReference databaseReference,userdata;
    private ChildEventListener childEventListener;
    Marker marker;
   String Snippet;
   private FirebaseApp firebase;
   private View bottomSheet;
   private BottomSheetBehavior bottomSheetBehavior;
   private TextView hostelname,hosteladdress,hosteltype,facilities,rent;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.searchBar);
       drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        searchBar.setOnSearchActionListener(this);

        navigationView.setNavigationItemSelectedListener(this);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
            mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(MainActivity.this);
            Places.initialize(MainActivity.this,"AIzaSyCa5Wp6zWQ1Wnj1B8XVRfJMsYI5G9ziZv4");
            placesClient= Places.createClient(this);
            AutocompleteSessionToken token= AutocompleteSessionToken.newInstance();
            auth= FirebaseAuth.getInstance();
            searchbtn = findViewById(R.id.searchhostelbtn);
            databaseReference= FirebaseDatabase.getInstance().getReference("HostelLocation");
            databaseReference.push().setValue(marker);
            bottomSheet= findViewById(R.id.bottom_sheet);
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(200);
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            hostelname = findViewById(R.id.bsheethostelname);
            hosteladdress = findViewById(R.id.bsheethosteladdress);
            hosteltype = findViewById(R.id.bsheethosteltype);
            facilities = findViewById(R.id.bsheethostelFacilities);
            rent = findViewById(R.id.bsheethostelRent);




        }
    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

               int id = item.getItemId();

        if (id == R.id.myaccount) {
           startActivity(new Intent(MainActivity.this,Profile.class));
        } else if (id == R.id.favorites) {
            Toast.makeText(this, "favorites activity", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.settings) {
           // startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            Toast.makeText(this, "favorites activity", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.help) {
            startActivity(new Intent(MainActivity.this,HelpActivity.class));

        } else if(id==R.id.about) {
            Toast.makeText(this, "About activity", Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.share) {
           Intent share = new Intent(Intent.ACTION_SEND);
           share.setType("text/plain");
           String shareBody ="Go Hostel";
           String shareSub = "Hey try this new App to find Hostels on the Go..";
           share.putExtra(Intent.EXTRA_SUBJECT,shareBody);
            share.putExtra(Intent.EXTRA_TEXT,shareSub);
            startActivity(Intent.createChooser(share,"Share Via"));

        }else if (id == R.id.logout) {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            auth.getInstance().signOut();
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            if (mapView!= null&& mapView.findViewById(Integer.parseInt("1"))!=null ){
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
                layoutParams.setMargins(0,0,40,180);

            }

        LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(5000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient= LocationServices.getSettingsClient(MainActivity.this);
        Task<LocationSettingsResponse> task= settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(MainActivity.this,new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();


            }
        });
        task.addOnFailureListener(MainActivity.this,new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvable = (ResolvableApiException)e;
                    try {
                        resolvable.startResolutionForResult(MainActivity.this,52);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s:dataSnapshot.getChildren()){
                            HostelLocation hostelLocation = s.getValue(HostelLocation.class);
                            LatLng location = new LatLng(hostelLocation.getLatitude(),hostelLocation.getLongtitude());
                            mMap.addMarker(new MarkerOptions().position(location).title(hostelLocation.getHostelName())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED

                            ));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                updateBottomSheetContent(marker);
                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });





    }

    private void updateBottomSheetContent(Marker marker) {
            hostelname.setText(marker.getTitle());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==51){
            if (resultCode==RESULT_OK){
                getDeviceLocation();
            }
        }
    }
    @SuppressLint("MissingPermission")
    public void getDeviceLocation(){
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()){
                        mLastknownLocation = task.getResult();
                        if (mLastknownLocation!= null){
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastknownLocation.getLatitude(),mLastknownLocation.getLongitude()),DEFAULT_ZOOM));

                        }else{
                           final   LocationRequest locationRequest = LocationRequest.create();
                            locationRequest.setInterval(10000);
                            locationRequest.setFastestInterval(5000);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            locationCallback = new LocationCallback(){
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    if (locationResult == null) {
                                        return;
                                    }


                                        mLastknownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastknownLocation.getLatitude(), mLastknownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);


                                }
                            };
                            mFusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);

                        }

                    }else {
                        Toast.makeText(MainActivity.this, "Unable to Fetch the Location", Toast.LENGTH_SHORT).show();
                    }

                }
            });

    }
}


