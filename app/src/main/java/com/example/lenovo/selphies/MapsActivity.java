package com.example.lenovo.selphies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is the google map class
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double currentLatitude, currentLongitude;
    private GPSTracker gps;

    private DatabaseReference postref;

    private static final int REQUEST_CODE_PERMISSION = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try{
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        gps = new GPSTracker(MapsActivity.this);
        if(gps.canGetLocation){
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
        }else{
            gps.showSettingAlert();
        }

        postref = FirebaseDatabase.getInstance().getReference("posts");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng current = new LatLng(currentLatitude, currentLongitude);
        MarkerOptions myMarker = new MarkerOptions()
                .position(current).title("I am here!")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.my_marker));
        mMap.addMarker(myMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        postref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post: dataSnapshot.getChildren()){
                    double latitude = (double) post.child("latitude").getValue();
                    double longitude = (double) post.child("longitude").getValue();
                    String username = post.child("username").getValue().toString();
                    String desc = post.child("desc").getValue().toString();
                    LatLng location = new LatLng(latitude,longitude);
                    MarkerOptions marker = new MarkerOptions()
                            .position(location)
                            .title(username)
                            .snippet(desc)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker));
                    mMap.addMarker(marker);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is to fix some glitch
     */
    @Override
    public void onBackPressed(){
        startActivity(new Intent(MapsActivity.this, MainActivity.class));
    }
}
