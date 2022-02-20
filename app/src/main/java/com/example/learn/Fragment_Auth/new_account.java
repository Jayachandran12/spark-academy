package com.example.learn.Fragment_Auth;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learn.MainActivity;
import com.example.learn.R;
import com.example.learn.Helper_class.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class new_account extends Fragment {

    //Fragment
    private EditText name,email,phno,pwd,img;
    private Button signup,login;
    private CheckBox checkbox;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private FirebaseDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_account, container, false);

        //Fragment details
        name = view.findViewById(R.id.Name_FNA);
        email = view.findViewById(R.id.UName_FNA);
        phno = view.findViewById(R.id.Phone_FNA);
        pwd = view.findViewById(R.id.Pwd_FNA);
        checkbox  = (CheckBox) view.findViewById(R.id.checkbox_NA);
        signup = view.findViewById(R.id.signup_FNA);
        login = view.findViewById(R.id.Login_FNA);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (value)
                    pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  // Show Password
                else
                    pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());     // Hide Password
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
                // Fragment to Fragment
                //setState();


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginPage();
            }
        });

        return view;
    }

    private void SendUserToLoginPage() {
        Intent loginActivity = new Intent(getActivity(), MainActivity.class);
        startActivity(loginActivity);
    }

    private void setState() {
        Bundle bundle = new Bundle();
        bundle.putString("UName", name.getText().toString());
        Success dialog = new Success();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(),"UName");
    }

    private void createNewAccount() {
        String create_name = name.getText().toString();
        String create_email = email.getText().toString();
        String create_phno = phno.getText().toString();
        String create_pwd = pwd.getText().toString();
        String create_img =  null;
        if(TextUtils.isEmpty(create_name)){
            Toast.makeText(getActivity(), "Please enter Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(create_email)){
            Toast.makeText(getActivity(), "Please enter Email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(create_phno)){
            Toast.makeText(getActivity(), "Please enter Phone Number", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(create_pwd)){
            Toast.makeText(getActivity(), "Please enter Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(), "Creating Account in Spark Education ", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(create_email, create_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //HELPER CLASS
                        Users users = new Users(create_name,create_email,create_phno,create_img,create_pwd);
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        RootRef.child("Users").child(currentUserId).setValue(users);

                        setState();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(getActivity(), "Error : " + message, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
