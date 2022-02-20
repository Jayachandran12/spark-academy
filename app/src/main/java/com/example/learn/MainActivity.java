package com.example.learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.*;

import com.example.learn.Admin.AdminActivity;
import com.example.learn.Fragment_Auth.Forget_pwd;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// 1. ADD collapsing toolbar
public class MainActivity extends AppCompatActivity {

    private TextView forget_pwd,create_account;
    private EditText upwd,uname;
    private CheckBox checkbox;
    private Button login_btn,admin_log;
    private ProgressDialog loadingBar;
    private ImageView glogin;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser,Adminuser;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //progress bar
        loadingBar = new ProgressDialog(this);

        //password
        forget_pwd = (TextView) findViewById(R.id.FPwd_MA);
        checkbox  = (CheckBox) findViewById(R.id.checkbox);

        //login
        upwd = (EditText) findViewById(R.id.UPwd_MA);
        uname = (EditText) findViewById(R.id.UName_MA);
        login_btn = (Button) findViewById(R.id.btn_login_MA);
        glogin = (ImageView) findViewById(R.id.btn_g_login);
        admin_log = (Button) findViewById(R.id.btn_login_admin_MA);

        //new account
        create_account = (TextView) findViewById(R.id.CAccount_MA);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        FragmentManager manager = getFragmentManager();

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, create_account.class );
                startActivity(intent);
            }
        });

        // Activity to Fragment
        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment forgetpwd = new Forget_pwd();
                FragmentTransaction transaction = manager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("Email_Activity", uname.getText().toString());

                forgetpwd.setArguments(bundle);
                transaction.add(R.id.container, forgetpwd,"Forget_Fragment").addToBackStack(null).commit();

            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (value)
                    upwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  // Show Password
                else
                    upwd.setTransformationMethod(PasswordTransformationMethod.getInstance());     // Hide Password
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });

        admin_log.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AllowAdminToLogin();
            }
        });

        glogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Google Signin
                //signIn();
            }
        });

        // Google Login
        //createRequest();
    }

    private void AllowAdminToLogin() {
        String login_email = uname.getText().toString();
        String login_pwd = upwd.getText().toString();

        if(!isOnline(this)){
            sendBroadcast(new Intent(this,NetworkCheck1.class));
            showCustomDialog();
        }

        if(TextUtils.isEmpty(login_email)){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(login_pwd)){
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(login_email, login_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SendAdminToAdminActivity();
                        Toast.makeText(MainActivity.this, "Logged in successfully ", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(MainActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }
    }

    private void AllowUserToLogin() {

        String login_email = uname.getText().toString();
        String login_pwd = upwd.getText().toString();

        if(!isOnline(this)){
            sendBroadcast(new Intent(this,NetworkCheck1.class));
            showCustomDialog();
        }

        if(TextUtils.isEmpty(login_email)){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(login_pwd)){
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(login_email, login_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SendUserToHomeActivity();
                        Toast.makeText(MainActivity.this, "Logged in successfully ", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    } else {
                        //String message = task.getException().toString();
                        Toast.makeText(MainActivity.this, "Error : " /*+ message*/, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please connect to the internet to move further ?")
                .setCancelable(false)
                .setPositiveButton("connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private boolean isOnline(MainActivity mainActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager)mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wificonn != null && wificonn.isConnected()) ||(mobileconn != null && mobileconn.isConnected())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser != null){
            SendUserToHomeActivity();
            //SendAdminToAdminActivity();
        }
    }

    //whenever the user login and when he presses back button he will not goto login page from homescreen
    private void SendUserToHomeActivity() {
        Intent homeActivity = new Intent(this, HomeScreen.class);
        //homeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeActivity);
        //finish();
    }

    private void SendAdminToAdminActivity() {
        Intent intent = new Intent(MainActivity.this, AdminActivity.class );
        startActivity(intent);
    }

    // BROADCAST RECEIVER
    public static class NetworkCheck1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"connect it",Toast.LENGTH_LONG).show();
        }
    }
}