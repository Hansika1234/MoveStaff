package com.moveit.movestaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoging extends AppCompatActivity {

   private Button nLogin,nRegistration;
   private EditText nEmail,nPassword;

   private FirebaseAuth nAuth;
   private  FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_loging);

        nAuth = FirebaseAuth.getInstance();

        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent=new Intent(DriverLoging.this,DriverMapsActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        };

        nLogin=(Button) findViewById(R.id.login);
        nRegistration=(Button) findViewById(R.id.registration);

        nEmail=(EditText) findViewById(R.id.email);
        nPassword=(EditText) findViewById(R.id.password);


        nRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email=nEmail.getText().toString();
                final String password=nPassword.getText().toString();

                nAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(DriverLoging.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(DriverLoging.this, "Sign Up Error",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String uber_Id=nAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uber_Id);
                            current_user_db.setValue(true);
                        }
                    }
                });
                nLogin .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String email=nEmail.getText().toString();
                        final String password=nPassword.getText().toString();

                        nAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(DriverLoging.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(!task.isSuccessful()){
                                    Toast.makeText(DriverLoging.this, "Sign In Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    @Override
     protected  void onStart() {
        super.onStart();
        nAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected  void onStop() {
        super.onStop();
        nAuth.removeAuthStateListener(firebaseAuthListener);
    }
}