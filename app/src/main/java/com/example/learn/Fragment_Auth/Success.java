package com.example.learn.Fragment_Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.learn.HomeScreen;
import com.example.learn.R;

public class Success extends DialogFragment {

    private TextView tv;
    private Button welcome;
    @Nullable
    @Override

    // Fragment to fragment

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.success_fragment, container, false);

        tv = view.findViewById(R.id.txt_SF);
        welcome = view.findViewById(R.id.Submit_SF);

        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToHomeActivity();
            }
        });
        Bundle bundle = getArguments();
        String str2 = bundle.getString("UName");
        tv.setText("Thankyou for Selecting Spark Education Mr / Mrs.  "+ str2);
        return view;
    }

    private void SendUserToHomeActivity() {
        Intent homeActivity = new Intent(getActivity(), HomeScreen.class);
        homeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeActivity);
        getActivity().finish();
    }
}
