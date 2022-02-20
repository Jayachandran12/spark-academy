package com.example.learn.Fragment_main.Fragment.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.Admin.Quiz.Questions;
import com.example.learn.Fragment_main.Quizz.QuizActivity;
import com.example.learn.Helper_class.Interface.quizItemClickListener;
import com.example.learn.Helper_class.Model.quizModel;
import com.example.learn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Quiz_stu_adaptor extends RecyclerView.Adapter{
    private static final String TAG = Quiz_stu_adaptor.class.getSimpleName();

    Context context;
    ArrayList<String> list;
    quizItemClickListener quizItemClickListener;
    String name;
    private DatabaseReference databaseReference;

    public static ArrayList<quizModel> listOfQ;
    public Quiz_stu_adaptor(Context context, ArrayList<String> list,String sub_name) {
        this.context = context;
        this.list = list;
        this.name = sub_name;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        viewHolderClass.qzname.setText(list.get(position).toString());

        listOfQ = new ArrayList<>();
        /*listOfQ.add(new quizModel("wed0","wed0","wed","wed","wed","wed0"));
        listOfQ.add(new quizModel("wed1","wed1","wed","wed","wed","wed1"));
        listOfQ.add(new quizModel("wed2","wed2","wed","wed","wed","wed2"));
        listOfQ.add(new quizModel("wed3","wed3","wed","wed","wed","wed3"));
        listOfQ.add(new quizModel("wed4","wed4","wed","wed","wed","wed4"));
        listOfQ.add(new quizModel("wed5","wed5","wed","wed","wed","wed5"));
        listOfQ.add(new quizModel("wed6","wed6","wed","wed","wed","wed6"));
        listOfQ.add(new quizModel("wed7","wed7","wed","wed","wed","wed7"));
        listOfQ.add(new quizModel("wed8","wed8","wed","wed","wed","wed8"));
        listOfQ.add(new quizModel("wed9","wed9","wed","wed","wed","wed9"));*/



        viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Course").child(name).child("Quiz").child(list.get(position).toString());
                Log.i(TAG, "Spark clicked quiz :" + list.get(position));
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listOfQ.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            quizModel qm = dataSnapshot.getValue(quizModel.class);
                            listOfQ.add(qm);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                context.startActivity(new Intent(context, QuizActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView qzname;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            qzname = itemView.findViewById(R.id.Display_Quiz);
            // [CLICK EVENT]
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quizItemClickListener.onQuizClick(list.get(getAdapterPosition()));
                }
            });*/

            /*databaseReference = FirebaseDatabase.getInstance().getReference("Course").child(name).child("Quiz").child(String.valueOf(qzname));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        quizModel qm = dataSnapshot.getValue(quizModel.class);
                        listOfQ.add(qm);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/

        }
    }

}
