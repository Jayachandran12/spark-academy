package com.example.learn.Fragment_main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.learn.Admin.Fragments.videoList;
import com.example.learn.Admin.RecycleAdaptor.subjectAdaptor;
import com.example.learn.Admin.video.video_detailed_view;
import com.example.learn.Fragment_main.Bookmark.markAdaptor;
import com.example.learn.Fragment_main.Quizz.QuizActivity;
import com.example.learn.Helper_class.Interface.VideoItemClickListener;
import com.example.learn.Helper_class.Model.filemodel;
import com.example.learn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class bookmark extends Fragment implements VideoItemClickListener {

    /** [ USED FOR FETCHING DATABASE DATA ] **/
    ArrayList<filemodel> fetchData;
    private RecyclerView recyclerView;
    markAdaptor mAdaptor;

    //Firebase
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark_fragment, container, false);

        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_content_BMF);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchData = new ArrayList<>();

        // FIREBASE
        // Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Bookmarks").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    // GETTING DATA AS IN THE HELPER DATA
                    filemodel data = ds.getValue(filemodel.class);
                    fetchData.add(data);
                }
                mAdaptor = new markAdaptor(getActivity(),fetchData, bookmark.this);
                recyclerView.setAdapter(mAdaptor);
                mAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;

    }

    @Override
    public void onVideoClick(filemodel fm, ImageView imageView) {
        startActivity(new Intent(getActivity(), video_detailed_view.class)
                .putExtra("fmtitle",fm.getTitle())
                .putExtra("fmdesc",fm.getVdesc())
                .putExtra("fmVideo",fm.getVurl())
                .putExtra("fmThumb",fm.getvThumb()));
    }
}