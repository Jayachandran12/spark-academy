package com.example.learn.Fragment_main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.learn.Fragment_main.Quizz.QuizActivity;
import com.example.learn.R;


public class Contests extends Fragment {

    Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bookmark_fragment, container, false);
        btn = view.findViewById(R.id.quiz);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), QuizActivity.class));
            }
        });
        return view;

    }
}