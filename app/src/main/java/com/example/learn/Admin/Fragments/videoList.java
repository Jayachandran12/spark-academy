package com.example.learn.Admin.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.learn.Admin.RecycleAdaptor.subjectAdaptor;
import com.example.learn.Admin.video.uploadVideos;
import com.example.learn.Admin.video.video_detailed_view;
import com.example.learn.Helper_class.Interface.VideoItemClickListener;
import com.example.learn.Helper_class.Model.filemodel;
import com.example.learn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class videoList extends Fragment implements VideoItemClickListener{

    String sub_name;
    private FloatingActionButton upload;

    /** [ DATA FROM FRAGMENT_ADAPTOR ] **/
    public videoList(String subname) {
        this.sub_name = subname;
    }

    /** [ USED FOR FETCHING DATABASE DATA ] **/
    ArrayList<filemodel> fetchData;
    private RecyclerView recyclerView;
    subjectAdaptor subjectAdaptor;

    //Firebase
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_video_list, container, false);

        // attributes
        upload = (FloatingActionButton) view.findViewById(R.id.uploadVideo_VL);

        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_content_VL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchData = new ArrayList<>();

        // FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference("Course").child(sub_name).child("Videos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    // GETTING DATA AS IN THE HELPER DATA
                    filemodel data = ds.getValue(filemodel.class);
                    fetchData.add(data);
                }
                subjectAdaptor = new subjectAdaptor(getActivity(),fetchData,videoList.this);
                recyclerView.setAdapter(subjectAdaptor);
                subjectAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), uploadVideos.class).putExtra("subject",sub_name));
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