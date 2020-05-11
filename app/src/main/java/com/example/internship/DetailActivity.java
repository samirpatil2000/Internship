package com.example.internship;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int  placePosition;

    TextView title,desc,address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.dmap);
        mapFragment.getMapAsync(DetailActivity.this);

        title=findViewById(R.id.titleDTv);
        desc=findViewById(R.id.descdTv);
        address=findViewById(R.id.addressdTv);
    }

    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        // From Main Activity
//        final Intent intent = getIntent();
//        placePosition=intent.getIntExtra("location",0);
//        Location placeLocation =new Location(LocationManager.GPS_PROVIDER);
//        placeLocation.setLatitude(MainActivity.locations.get(placePosition).latitude);
//        placeLocation.setLongitude(MainActivity.locations.get(placePosition).longitude);
//
//        LatLng userLocation = new LatLng(placeLocation.getLongitude(), placeLocation.getLatitude());
//        mMap.addMarker(new MarkerOptions().position(userLocation).title("Is This Your Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
