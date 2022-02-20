package com.example.learn;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.learn.Fragment_Auth.new_account;

//Fragment to Fragment
public class create_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_success);

        new_account newAcc = new new_account();
        getSupportFragmentManager().beginTransaction().add(R.id.framelout1,newAcc,"Create Account").commit();
    }
}