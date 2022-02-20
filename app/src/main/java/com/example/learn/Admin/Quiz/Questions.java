package com.example.learn.Admin.Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.learn.Admin.RecycleAdaptor.QuestionAdaptor;
import com.example.learn.Admin.RecycleAdaptor.QuizAdaptor;
import com.example.learn.Helper_class.Model.quizModel;
import com.example.learn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Questions extends AppCompatActivity {

    private Button addqs;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference RootRef,databaseReference;

    // USED FOR FETCHING DATABASE DATA
    ArrayList<String> fetchData;
    RecyclerView recyclerView;
    QuestionAdaptor questionAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        addqs = (Button) findViewById(R.id.AddQs_UQ);

        Intent intent = getIntent();
        String sub_name = intent.getStringExtra("sub_name");
        String quiz_name = intent.getStringExtra("quiz_name");

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView)findViewById(R.id.DisplayQs_UQ);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData = new ArrayList<>();

        // QUERY

        databaseReference = FirebaseDatabase.getInstance().getReference("Course").child(sub_name).child("Quiz").child(quiz_name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    // GETTING DATA AS IN THE HELPER DATA
                    String data = ds.getKey();
                    fetchData.add(data);
                }
                questionAdaptor = new QuestionAdaptor(Questions.this,fetchData,sub_name,quiz_name);
                recyclerView.setAdapter(questionAdaptor);
                questionAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** THIS CODE SEGMENT IS USED TO GET THE COUNT OF THE CHILDREN **/
                FirebaseDatabase.getInstance().getReference("Course")
                        .child(sub_name).child("Quiz").child(quiz_name)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int size = (int) snapshot.getChildrenCount();
                                System.out.println(size);
                                openQuestionDialog(sub_name,quiz_name,size);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                });

            }
        });
    }

    private void openQuestionDialog(String sub_name,String quiz_name,int Size) {
        EditText qs,op1,op2,op3,op4,op_crt;
        Button add;
        DatabaseReference RootRef,databaseReference;

        Dialog ds = new Dialog(this);
        ds.setContentView(R.layout.dialog_add_question);
        qs  = (EditText) ds.findViewById(R.id.create_question);
        op1 = (EditText) ds.findViewById(R.id.create_option1);
        op2 = (EditText) ds.findViewById(R.id.create_option2);
        op3 = (EditText) ds.findViewById(R.id.create_option3);
        op4 = (EditText) ds.findViewById(R.id.create_option4);
        op_crt = (EditText) ds.findViewById(R.id.create_optioncrt);
        add = (Button) ds.findViewById(R.id.create_add);

        RootRef = FirebaseDatabase.getInstance().getReference("");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String create_qs = qs.getText().toString();
                String create_op1 = op1.getText().toString();
                String create_op2 = op2.getText().toString();
                String create_op3 = op3.getText().toString();
                String create_op4 = op4.getText().toString();
                String create_crt = op_crt.getText().toString();

                quizModel qm = new quizModel(create_qs,create_op1,create_op2,create_op3,create_op4,create_crt);

                RootRef.child("Course").child(sub_name).child("Quiz").child(quiz_name).child(String.valueOf(Size+1)).setValue(qm);
                ds.hide();
            }
        });
        ds.show();
    }
}