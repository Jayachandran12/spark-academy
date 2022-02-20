package com.example.learn.Admin.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.learn.Admin.AdminActivity;
import com.example.learn.Admin.Quiz.Questions;
import com.example.learn.Admin.RecycleAdaptor.HelperAdaptor;
import com.example.learn.Admin.RecycleAdaptor.QuizAdaptor;
import com.example.learn.Helper_class.Interface.quizItemClickListener;
import com.example.learn.Helper_class.Model.Channel;
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


public class quizList extends Fragment  {

    private static final String TAG = quizList.class.getSimpleName();
    private FloatingActionButton createQ;
    String sub_name,create_Quiz;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference RootRef,databaseReference;

    // USED FOR FETCHING DATABASE DATA
    ArrayList<String> fetchData;
    RecyclerView recyclerView;
    QuizAdaptor quizAdaptor;

    public quizList(String subname) {
        this.sub_name = subname;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_quiz_list, container, false);
        createQ = view.findViewById(R.id.create_quiz_QL);

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_content_QL);
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
                quizAdaptor = new QuizAdaptor(getActivity(),fetchData, sub_name);
                recyclerView.setAdapter(quizAdaptor);
                quizAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // [CREATING NEW CHANNEL]
        createQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChannelDialog();
            }
        });
        return view;
    }

    private void openChannelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("CREATE SUBJECT");

        View view = getLayoutInflater().inflate(R.layout.quiz_dialog,null);
        builder.setView(view);


        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText Quizname;
                Quizname = view.findViewById(R.id.quiz_name_QD);
                create_Quiz = Quizname.getText().toString();

                /** [ PROCESSING BOUND SERVICE ( CHECKING THE ACTIVITY IS CONNECTED TO THE BOUNDED SERVICE ) ] **/
                /** [ WITHOUT BOUND SERVICE ] **/
                RootRef.child("Course").child(sub_name).child("Quiz").child(create_Quiz).setValue("");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}