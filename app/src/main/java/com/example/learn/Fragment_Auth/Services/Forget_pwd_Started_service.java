package com.example.learn.Fragment_Auth.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_pwd_Started_service extends Service {
    private static  final String TAG = Forget_pwd_Started_service.class.getSimpleName();
    FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        Log.i(TAG,"onCreate , Thread Name " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //int sleepTime = intent.getIntExtra("sleepTime",1);
        String forget_email = intent.getStringExtra("data");
        new MyAsyncTask().execute(forget_email);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind , Thread Name " + Thread.currentThread().getName());
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy , Thread Name " + Thread.currentThread().getName());
    }

    class MyAsyncTask extends AsyncTask<String, String, Void> {
        private final String TAG = MyAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute, Thread name " + Thread.currentThread().getName());
        }

        // Perform our Long Running Task
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground, Thread name " + Thread.currentThread().getName());
            String email = params[0];

                //publishProgress("Counter is now ");
                try {
                    //Thread.sleep(10000);
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Forget_pwd_Started_service.this, "Check your Email !", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Forget_pwd_Started_service.this, "Try again ! Something went wromg !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(Forget_pwd_Started_service.this, values[0], Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Counter Value " + values[0]+ " onProgressUpdate, Thread name " + Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stopSelf(); // Destroy the Service from within the Service class itself
            Log.i(TAG, "onPostExecute, Thread name " + Thread.currentThread().getName());
        }
    }
}