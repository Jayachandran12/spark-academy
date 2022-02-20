package com.example.learn.Fragment_main.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.learn.Admin.Fragments.quizList;
import com.example.learn.Admin.RecycleAdaptor.QuizAdaptor;
import com.example.learn.Admin.video.video_detailed_view;
import com.example.learn.Fragment_main.Fragment.Adaptor.Quiz_stu_adaptor;
import com.example.learn.Fragment_main.Quizz.QuizActivity;
import com.example.learn.Helper_class.Interface.quizItemClickListener;
import com.example.learn.Helper_class.Model.quizModel;
import com.example.learn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class quiz_stu_list extends Fragment {
    private static final String TAG = quizList.class.getSimpleName();
    String sub_name;

    public quiz_stu_list(String sub_name) {
        this.sub_name = sub_name;
    }

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference RootRef,databaseReference;

    // USED FOR FETCHING DATABASE DATA
    ArrayList<String> fetchData;
    RecyclerView recyclerView;
    Quiz_stu_adaptor quizAdaptor;

    //fetch questions
    //public static ArrayList<quizModel> listOfQ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_stu_list, container, false);
        // FIREBASE
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_content_QSL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchData = new ArrayList<>();

        // QUERY
        databaseReference = FirebaseDatabase.getInstance().getReference("Course").child(sub_name).child("Quiz");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    // GETTING DATA AS IN THE HELPER DATA
                    String data = ds.getKey();
                    fetchData.add(data);
                }
                quizAdaptor = new Quiz_stu_adaptor(getActivity(),fetchData, sub_name);
                recyclerView.setAdapter(quizAdaptor);
                quizAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;

    }

}