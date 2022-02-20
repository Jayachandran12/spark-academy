package com.example.learn.Fragment_Auth;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import com.example.learn.Fragment_Auth.Services.Forget_pwd_Started_service;
import com.example.learn.R;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_pwd extends Fragment{

    private EditText edt;
    private Button btn;

    // FIREBASE
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forget_pwd, container, false);
        btn = (Button) view.findViewById(R.id.Forget_btn_FP);
        edt = view.findViewById(R.id.UName_FP);

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();

        String str = "";
        Bundle data = getArguments();

        // Activity to Fragment
        if(data != null)
            str = data.getString("Email_Activity");
        edt.setText(str);

        // Fragment  to Activity
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle name = new Bundle();
                name.putString("Name", edt.getText().toString());
                Intent intent = new Intent(getActivity(), HomeScreen.class );
                intent.putExtras(name);
                //intent.putExtra("name", edtxt.getText().toString());
                startActivity(intent);
            }
        });*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
                Intent intent = new Intent(getActivity(), Forget_pwd_Started_service.class);  // FORGET PWD
                //Intent intent1 = new Intent(getActivity(), Additional.class);         // DUMMY

                intent.putExtra("data",edt.getText().toString());
                //intent1.putExtra("sleepTime",10);

                getActivity().startService(intent);
                //getActivity().startService(intent1);
            }
        });

        return view;
    }

    private void sendMail() {
        String email = edt.getText().toString().trim();

        if(email.isEmpty()){
            edt.setError("Please fill the Email field");
            edt.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt.setError("Please provide a valid Email");
            edt.requestFocus();
            return;
        }

       /* mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(),"Check your Email !",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"Try again ! Something went wromg !",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

}