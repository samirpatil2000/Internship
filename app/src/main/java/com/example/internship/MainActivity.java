package com.example.internship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText titleEt, descEt;
    Button saveBt,showListBt;
    ProgressDialog pd;

    FirebaseFirestore firestore;

    String putId,putTitle,putDesc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleEt = findViewById(R.id.title_Et);
        descEt = findViewById(R.id.desc_Et);
        saveBt = findViewById(R.id.save_btn);
        showListBt = findViewById(R.id.showList);
        pd = new ProgressDialog(this);

        firestore = FirebaseFirestore.getInstance();

        pd.dismiss();


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
                    uploadToFirestore(title,desc);

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

    private void uploadToFirestore(String title, String desc) {
        pd.setTitle("Adding Data to Firestore");
        pd.show();

        String id = UUID.randomUUID().toString();

        Map<String,Object> doc = new HashMap<>();
        doc.put("id" , id);
        doc.put("title",title);
        doc.put("desc",desc);



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
