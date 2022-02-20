package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.learn.Fragment_main.Contests;
import com.example.learn.Fragment_main.home_fragment;
import com.example.learn.Fragment_main.profile_fragment;
import com.example.learn.Fragment_main.search_fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeScreen extends AppCompatActivity {

    //TextView username;
    /*
        // Fragment to activity
        username = (TextView) findViewById(R.id.nameHS);
        Bundle bundle = getIntent().getExtras();
        String str = bundle.getString("Name");
        username.setText("Welcome "+str);
    */

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    Toolbar mToolbar;
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mToolbar = (Toolbar) findViewById(R.id.toolbarHS);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Spark Academy");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        chipNavigationBar = findViewById(R.id.bottom_navigation_menu);
        chipNavigationBar.setItemSelected(R.id.home,true);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new home_fragment()).commit();
        bottomMenu();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null){
            sendUserToLoginActivity();
        }
    }

    private void sendUserToLoginActivity() {
        Intent homescreen = new Intent(HomeScreen.this, MainActivity.class );
        startActivity(homescreen);
    }

    private void bottomMenu(){
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch(i){
                    case R.id.home:
                        fragment = new home_fragment();
                        break;
                    case R.id.search:
                        fragment = new search_fragment();
                        break;
                    case R.id.bkmark:
                        fragment = new Contests();
                        break;
                    case R.id.profile:
                        fragment = new profile_fragment();
                        break;
                }
                if(fragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
                }
            }
        });
    }
}