package com.example.learn.Fragment_main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.learn.Admin.RecycleAdaptor.HelperAdaptor;
import com.example.learn.Fragment_main.Fragment.PagerAdaptor.Student_content;
import com.example.learn.Helper_class.Model.Channel;
import com.example.learn.Helper_class.Interface.SubjectItemClickListener;
import com.example.learn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class home_fragment extends Fragment implements SubjectItemClickListener {

    private DatabaseReference databaseReference;

    // USED FOR FETCHING DATABASE DATA
    ArrayList<Channel> fetchData;
    RecyclerView recyclerView;
    HelperAdaptor helperAdaptor;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                helperAdaptor.getFilter().filter(s);
                return false;
            }
        });

        recyclerView = view.findViewById(R.id.subject_list_HM);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchData = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Course");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    // GETTING DATA AS IN THE HELPER DATA
                    Channel data = ds.getValue(Channel.class);
                    fetchData.add(data);
                }
                helperAdaptor = new HelperAdaptor(getActivity(),fetchData,home_fragment.this);
                recyclerView.setAdapter(helperAdaptor);
                helperAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return  view;
    }

    @Override
    public void onSubjectClick(Channel ch) {
        startActivity(new Intent(getActivity(), Student_content.class)
                .putExtra("subCode",ch.getSub_code())
                .putExtra("subName",ch.getSub_name())
                .putExtra("subDesc",ch.getSub_desc())
                .putExtra("Instructor",ch.getSub_author()));
    }

}