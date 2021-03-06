package com.example.internship;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.provider.Settings;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    EditText titleEt, descEt;
    Button saveBt,showListBt;
    ProgressDialog pd;

    TextView addressTv,locationTv;
    ImageButton locationSetBtn;

    FirebaseFirestore firestore;

    String putId,putTitle,putDesc,putAddress,putLocation;

    // Address Though location
    String address;

    String locationString;
    String locationStringUpdate;


    LatLng mainLocation;
    // FOR LATITUDE AND LONGITUDE
//    Location location;
    // for location
    LocationManager locationManager;
    LocationListener locationListener;


    static ArrayList<LatLng> locations= new ArrayList<LatLng>();

    double latitude,longitude;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titleEt = findViewById(R.id.title_Et);
        descEt = findViewById(R.id.desc_Et);
        saveBt = findViewById(R.id.save_btn);
        showListBt = findViewById(R.id.showList);
        addressTv =findViewById(R.id.addressTv);
        locationSetBtn=findViewById(R.id.locationImgBtn);

        locationTv=findViewById(R.id.locationTv);

        pd = new ProgressDialog(this);
        pd.dismiss();

        firestore = FirebaseFirestore.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Data");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            // update data
            actionBar.setTitle("Edit");
            saveBt.setText("Edit");
            putId = bundle.getString("putId");
            putTitle = bundle.getString("putTitle");
            putDesc = bundle.getString("putDesc");
            putAddress=bundle.getString("putAddress");
            putLocation=bundle.getString("putLocation");


//            seperateLocation(putLocation);
//            if(putLocation !=null && putLocation.isEmpty()) {
                String[] latlong = putLocation.split(",");
                String a1 = latlong[0];
                String a2 = latlong[1];
                latitude = Double.parseDouble(a1);
                longitude = Double.parseDouble(a2);
//            }else{
//                latitude=0;
//                longitude=0;
//            }

            mainLocation = new LatLng(latitude, longitude);

            titleEt.setText(putTitle);
            descEt.setText(putDesc);
            addressTv.setText(putAddress);
            locationTv.setText(putLocation);
        }
        else{

            actionBar.setTitle("Add Data");
            saveBt.setText("Save");
        }



        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    String id = putId;
                    String title = titleEt.getText().toString();
                    String desc = descEt.getText().toString();

                    // function call to update data
                    updateData(id,title,desc,address,locationString);

                }
                else{
                    String title = titleEt.getText().toString();
                    String desc = descEt.getText().toString();

                    uploadToFirestore(title,desc,address,locationString);

                }

            }
        });
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setTitle("Adding Your location");
                pd.show();
                locationListener=new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        pd.dismiss();
                        updateLocationInfo(location);
                        locationTv.setText(location.getLongitude()+","+location.getLatitude());
                        locationString = location.getLongitude()+","+location.getLatitude();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

                }
                else
                    {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    Location lastKnowLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(lastKnowLocation!=null){
                        updateLocationInfo(lastKnowLocation);
                    }
                }


            }
        });

        showListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addressTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mapActivity = new Intent(MainActivity.this,MapsActivity.class);
                mapActivity.putExtra("longitude",longitude);
                mapActivity.putExtra("latitude",latitude);
                startActivity(mapActivity);
                finish();
            }
        });


    }

//    private LatLng seperateLocation(String putLocation) {
//        String [] latlong =putLocation.split(",");
//        double latitude =Double.parseDouble(latlong[0]);
//        double longitude =Double.parseDouble(latlong[1]);
//        LatLng mainLocation = new LatLng(latitude, longitude);
//        return mainLocation;
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

            startListening();

        }

    }
    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }



    private void updateLocationInfo(final Location location) {

        address=" Could not find address :( ";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());


        locations.add(new LatLng(location.getLongitude(),location.getLatitude()));

        // send location to google map
        addressTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mapActivity = new Intent(MainActivity.this,MapsActivity.class);
                mapActivity.putExtra("location",location);
//                locationTv.setVisibility(View.VISIBLE);

                startActivity(mapActivity);
                finish();
            }
        });

        try{
            List<Address> listAddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (listAddress != null && listAddress.size()>0){
                address="Address:\n";

                if(listAddress.get(0).getThoroughfare() !=null) {
                    address += listAddress.get(0).getThoroughfare() +","+ "\n";
                }

                if (listAddress.get(0).getSubLocality() !=null){
                    address+= listAddress.get(0).getLocality()+" ,"+"\n";
                }

                if (listAddress.get(0).getLocality() !=null){
                    address+= listAddress.get(0).getLocality()+" ,"+"\n";
                }


                if (listAddress.get(0).getSubAdminArea() !=null){
                    address+= listAddress.get(0).getLocality()+" ,"+"\n";
                }

                if (listAddress.get(0).getPostalCode() !=null){
                    address+= listAddress.get(0).getPostalCode()+" ,"+"\n";
                }

                if (listAddress.get(0).getAdminArea() !=null){
                    address+= listAddress.get(0).getAdminArea()+" "+"\n";
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        addressTv.setText(address);

    }



    private void updateData(String id, String title, String desc,String address,String locationString) {
        pd.setTitle("Updating...");
        pd.show();

        firestore.collection("Data").document(id)
                .update("title",title,"desc",desc,"address",address,"location",locationString)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        // called when update successfully
                        showMessage("Successfully Updated");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                showMessage(e.getMessage());
            }
        });
    }

    private void uploadToFirestore(String title, String desc,String address,String locationString) {
        pd.setTitle("Adding Data to Firestore");
        pd.show();

        String id = UUID.randomUUID().toString();

        Map<String,Object> doc = new HashMap<>();
        doc.put("id" , id);
        doc.put("title",title);
        doc.put("desc",desc);
        doc.put("address",address);
        doc.put("location", locationString);

        firestore.collection("Data").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Data Is Successfully Added");
                        pd.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        showMessage(e.getMessage().toString());
                    }
                });
    }

    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
}
