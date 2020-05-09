package com.example.internship;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    EditText titleEt, descEt;
    Button saveBt,showListBt;
    ProgressDialog pd;

    TextView addressTv;
    ImageButton locationSetBtn;

    FirebaseFirestore firestore;

    String putId,putTitle,putDesc;

    // Address Though location
    String address;

    // for location
    LocationManager locationManager;
    LocationListener locationListener;



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

            titleEt.setText(putTitle);
            descEt.setText(putDesc);
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
                    updateData(id,title,desc);

                }
                else{
                    String title = titleEt.getText().toString();
                    String desc = descEt.getText().toString();

                    uploadToFirestore(title,desc,address);

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
                }else{
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
    }


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
        }

    }



    private void updateLocationInfo(Location location) {
        address=" Could not find address :( ";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());

        try{
            List<Address> listAddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (listAddress != null && listAddress.size()>0){
                address="Address:\n";

                if(listAddress.get(0).getThoroughfare() !=null) {
                    address += listAddress.get(0).getThoroughfare() +" Thoroughfare"+ "\n";
                }

                if (listAddress.get(0).getSubLocality() !=null){
                    address+= listAddress.get(0).getLocality()+" SubLocality"+"\n";
                }

                if (listAddress.get(0).getLocality() !=null){
                    address+= listAddress.get(0).getLocality()+" Locality"+"\n";
                }


                if (listAddress.get(0).getSubAdminArea() !=null){
                    address+= listAddress.get(0).getLocality()+" SubAdminArea"+"\n";
                }

                if (listAddress.get(0).getPostalCode() !=null){
                    address+= listAddress.get(0).getPostalCode()+" Postal Code"+"\n";
                }

                if (listAddress.get(0).getAdminArea() !=null){
                    address+= listAddress.get(0).getAdminArea()+" AdminArea"+"\n";
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        addressTv.setText(address);

    }



    private void updateData(String id, String title, String desc) {
        pd.setTitle("Updating...");
        pd.show();

        firestore.collection("Data").document(id)
                .update("title",title,"desc",desc)
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

    private void uploadToFirestore(String title, String desc,String address) {
        pd.setTitle("Adding Data to Firestore");
        pd.show();

        String id = UUID.randomUUID().toString();

        Map<String,Object> doc = new HashMap<>();
        doc.put("id" , id);
        doc.put("title",title);
        doc.put("desc",desc);
        doc.put("address",address);



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
