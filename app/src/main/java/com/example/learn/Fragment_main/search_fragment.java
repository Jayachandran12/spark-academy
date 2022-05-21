package com.example.learn.Fragment_main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.learn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class search_fragment extends Fragment {

    EditText num1;
    Button run;
    TextView res;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_fragment, container, false);

        num1 = (EditText) view.findViewById(R.id.programs_sf);
        //num2 = (EditText) view.findViewById(R.id.output_sf);
        run = (Button) view.findViewById(R.id.run_sf);
        res = (TextView) view.findViewById(R.id.resView);

        if(!Python.isStarted())
            Python.start(new AndroidPlatform(getActivity()));

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Python py = Python.getInstance();

                PyObject pyobj = py.getModule("script");
                PyObject obj = pyobj.callAttr("main",num1.getText().toString());
                res.setText(obj.toString());
            }
        });


        return view;
    }
}