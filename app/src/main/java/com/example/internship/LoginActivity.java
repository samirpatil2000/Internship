package com.example.internship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt,passwordEt;
    Button loginBtn;
    ProgressDialog pd;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt=findViewById(R.id.emailEt);
        passwordEt=findViewById(R.id.passwordEt);
        loginBtn=findViewById(R.id.loginBtn);

        pd = new ProgressDialog(LoginActivity.this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setTitle("Login..");
                pd.show();
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if (email.isEmpty() || password.isEmpty()){

                    pd.dismiss();
                    showMessage("All Fields are required ");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    pd.dismiss();
                    showMessage(" Invalid Email");
                    emailEt.setError("Invalid Email");
                    emailEt.setFocusable(true);
                }
                else{
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        pd.dismiss();
                                        showMessage("Authentication Failed" + task.getException().toString());
                                    }
                                }
                            });
                }
            }
        });






    }
    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // user is already connected so we need to redirect
            startActivity(new Intent(LoginActivity.this,ListActivity.class));

        }

    }
}
