package com.example.learn.Fragment_main.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Retrieve_profile_Intent_Service extends IntentService {
    private static final String TAG = Retrieve_profile_Intent_Service.class.getSimpleName();

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase db;
    private DatabaseReference rootRef;
    private Query query;

    public Retrieve_profile_Intent_Service() {
        super(" WorkerThread ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Spark onCreate , Thread Name :" + Thread.currentThread().getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Spark onHandleIntent, Thread name :" + Thread.currentThread().getName());
        Log.i(TAG, "Spark onHandleIntent [RETRIEVE] <-- [FIREBASE], Thread name ");
        Log.i(TAG, "Spark onHandleIntent [SENDING] --> [PROFILE_FRAGMENT], Thread name ");
        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        rootRef = db.getReference("Users");
        query = rootRef.orderByChild("email").equalTo(currentUser.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Retrieving Data from firebase

                    String get_name  = "" + dataSnapshot.child("name").getValue();
                    String get_email = "" + dataSnapshot.child("email").getValue();
                    String get_phno  = "" + dataSnapshot.child("phno").getValue();
                    String get_img   = "" + dataSnapshot.child("imageprofile").getValue();

                    Bundle bundle = new Bundle();
                    bundle.putString("get_name",get_name);
                    bundle.putString("get_email",get_email);
                    bundle.putString("get_phno",get_phno);
                    bundle.putString("get_img",get_img);
                    resultReceiver.send(18,bundle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Spark onDestroy, Thread name :" + Thread.currentThread().getName());
    }
}