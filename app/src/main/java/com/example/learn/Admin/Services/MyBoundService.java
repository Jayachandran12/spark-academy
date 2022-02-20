package com.example.learn.Admin.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.learn.Helper_class.Model.Channel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyBoundService extends Service {
    private static final String TAG = MyBoundService.class.getSimpleName();

    private DatabaseReference RootRef;

    private MyLocalBinder myLocalBinder = new MyLocalBinder();

    /** [ CREATING INNER CLASS ] **/
    public class MyLocalBinder extends Binder {
        public MyBoundService getService(){
            return  MyBoundService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RootRef = FirebaseDatabase.getInstance().getReference();
        Log.i(TAG,"Spark onCreate , Thread Name :" + Thread.currentThread().getName());
    }

    @Nullable
    @Override  /** [ INTENT WILL CALL ( onBind ) ] **/
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"Spark onBind , Thread Name :" + Thread.currentThread().getName());
        return myLocalBinder;
    }

    public void create_new_subject(String create_sub,String create_desc,String create_staff,String create_code, String create_dept){
        // CREATION OF HELPER CLASS
        /*try{
            Thread.sleep(35000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/
        Channel channel = new Channel(create_sub, create_desc,create_staff,create_code,create_dept);
        RootRef.child("Course").child(create_sub).setValue(channel);
    }
}