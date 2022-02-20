package com.example.learn.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.learn.Admin.Services.MyBoundService;
import com.example.learn.Helper_class.Interface.SubjectItemClickListener;
import com.example.learn.MainActivity;
import com.example.learn.R;
import com.example.learn.Admin.RecycleAdaptor.HelperAdaptor;
import com.example.learn.Helper_class.Model.Channel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements SubjectItemClickListener {
    private static final String TAG = AdminActivity.class.getSimpleName();
    private FloatingActionButton create;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference RootRef,databaseReference;

    // USED FOR FETCHING DATABASE DATA
    ArrayList<Channel> fetchData;
    RecyclerView recyclerView;
    HelperAdaptor helperAdaptor;

    /** [ BOUND SERVICE ] **/
    boolean isBound = false;
    private MyBoundService myBoundService;

    /** [ 2. CREATING BOUND SERVICE CONNECTION ] **/
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"Spark onServiceConnected , Thread Name :" + Thread.currentThread().getName());
            MyBoundService.MyLocalBinder myLocalBinder = (MyBoundService.MyLocalBinder) iBinder;
            myBoundService = myLocalBinder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG,"Spark onServiceDisconnected , Thread Name :" + Thread.currentThread().getName());
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        create = findViewById(R.id.create_subject_AA);

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_subject_AA);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData = new ArrayList<>();

        // QUERY
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
                helperAdaptor = new HelperAdaptor(AdminActivity.this,fetchData,AdminActivity.this);
                recyclerView.setAdapter(helperAdaptor);
                helperAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // [CREATING NEW CHANNEL]
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChannelDialog();
            }
        });
    }

    // CHECKING CURRENT USER
    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null){
            sendUserToLoginActivity();
        }

        /** [ 1. CALLING BOUND SERVICE ] **/
        Log.i(TAG,"Spark onStart , Thread Name :" + Thread.currentThread().getName());
        Intent intent = new Intent(AdminActivity.this,MyBoundService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        /** [ ENDING BOUND SERVICE ] **/
        Log.i(TAG,"Spark onStop , Thread Name :" + Thread.currentThread().getName());
        if(isBound){
            unbindService(mConnection);
            isBound = false;
        }
    }

    // SENDING BACK TO ACTIVITY [LOGIN SCREEN]
    private void sendUserToLoginActivity() {
        Intent homescreen = new Intent(AdminActivity.this, MainActivity.class );
        startActivity(homescreen);
    }

    // CREATING NEW CHANNEL - CONTINUATION
    private void openChannelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("CREATE SUBJECT");

        View view = getLayoutInflater().inflate(R.layout.channel_dialog,null);
        builder.setView(view);

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText subname,subdesc,subcode,sub_staff,sub_dept;
                subname = view.findViewById(R.id.subject_name);
                subdesc = view.findViewById(R.id.subject_desc);
                subcode = view.findViewById(R.id.subject_code);
                sub_staff = view.findViewById(R.id.subject_staff);
                sub_dept = view.findViewById(R.id.dept);

                String create_sub = subname.getText().toString();
                String create_desc = subdesc.getText().toString();
                String create_code = subcode.getText().toString();
                String create_staff = sub_staff.getText().toString();
                String create_dept = sub_dept.getText().toString();

                /** [ PROCESSING BOUND SERVICE ( CHECKING THE ACTIVITY IS CONNECTED TO THE BOUNDED SERVICE ) ] **/
                if(isBound){
                    myBoundService.create_new_subject(create_sub,create_desc,create_staff,create_code,create_dept);
                }

                /** [ WITHOUT BOUND SERVICE ] **/
                //Channel channel = new Channel(create_sub, create_desc,create_staff,create_code,create_dept);
                //RootRef.child("Course").child(create_sub).setValue(channel);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onSubjectClick(Channel ch) {
        startActivity(new Intent(AdminActivity.this, Content.class)
                .putExtra("subCode",ch.getSub_code())
                .putExtra("subName",ch.getSub_name())
                .putExtra("subDesc",ch.getSub_desc())
                .putExtra("Instructor",ch.getSub_author()));
    }
}