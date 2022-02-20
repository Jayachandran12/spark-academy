package com.example.learn.Admin.RecycleAdaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.Admin.Quiz.Questions;
import com.example.learn.Helper_class.Interface.SubjectItemClickListener;
import com.example.learn.Helper_class.Interface.quizItemClickListener;
import com.example.learn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizAdaptor extends RecyclerView.Adapter{

    Context context;
    ArrayList<String> list;
    quizItemClickListener quizItemClickListener;
    String name;

    public QuizAdaptor(Context context, ArrayList<String> list, String sub_name) {
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
        viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Questions.class).putExtra("quiz_name",list.get(position).toString()).putExtra("sub_name",name));
            }
        });
        viewHolderClass.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Course").child(name).child("Quiz").child(list.get(position).toString());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //notifyItemRemoved(getAdapterPosition());
                return true;
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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    list.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
