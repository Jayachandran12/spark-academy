package com.example.learn.Fragment_main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.learn.Fragment_main.Services.Retrieve_profile_Intent_Service;
import com.example.learn.MainActivity;
import com.example.learn.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class profile_fragment extends Fragment {
    private static final String TAG = profile_fragment.class.getSimpleName();

    //other attributes
    private TextView logout_btn, about;
    private ImageView profile_img;
    private EditText name_pr, email_pr, phno_pr;

    //image
    private int IMAGE_GALLERY_REQUEST=111;
    private FloatingActionButton fabcam;
    private Uri profileUri;

    //dialog
    private ProgressDialog progressDialog;
    private BottomSheetDialog abt_dialog;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase db;
    private DatabaseReference rootRef;
    private FirebaseStorage storage;
    private Query query;

    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);

        logout_btn = (TextView) view.findViewById(R.id.logout_PF);
        name_pr = view.findViewById(R.id.Name_PF);
        email_pr = view.findViewById(R.id.UName_PF);
        phno_pr = view.findViewById(R.id.Phone_PF);
        about = (TextView) view.findViewById(R.id.about_PF);

        // IMAGE
        fabcam = (FloatingActionButton) view.findViewById(R.id.fab_camera_PF);
        profile_img = (ImageView) view.findViewById(R.id.image_profile_PF);

        // BOTTOM DIALOG
        abt_dialog = new BottomSheetDialog(getActivity());
        progressDialog= new ProgressDialog(getActivity());

        // FIREBASE
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        rootRef = db.getReference("Users");
        
        // ----------------------------- [ IN MAIN THREAD ] -----------------------------

        /*query = rootRef.orderByChild("email").equalTo(currentUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Retrieving Data from firebase

                    String get_name  = "" + dataSnapshot.child("name").getValue();
                    String get_email = "" + dataSnapshot.child("email").getValue();
                    String get_phno  = "" + dataSnapshot.child("phno").getValue();
                    String get_img   = "" + dataSnapshot.child("imageprofile").getValue();

                    name_pr.setText(get_name);
                    email_pr.setText(get_email);
                    phno_pr.setText(get_phno);
                    try {
                        Glide.with(getActivity()).load(get_img).into(profile_img);
                    }
                    catch (Exception e) { }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/

        // ----------------------------- [ INTENT SERVICE ] -----------------------------
        // ---------------- [ CREATING INNER CLASS FOR ( RESULT RECEIVER )---------------

        ResultReceiver resultReceiver =new MyResultReceiver(null);
        Intent in = new Intent(getActivity(), Retrieve_profile_Intent_Service.class);
        in.putExtra("receiver",resultReceiver);
        getActivity().startService(in);

        // ------------------------ [ Fetching Google Sign In ] ------------------------

        /*GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(signInAccount != null){
            name_pr.setText(signInAccount.getDisplayName());
            email_pr.setText(signInAccount.getEmail());
            Glide.with(getActivity()).load(signInAccount.getPhotoUrl()).into(profile_img);
        }*/

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSignOut();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        fabcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return view;
    }

    // ----------------------------- [ INTENT SERVICE - 1] -----------------------------
    private class MyResultReceiver extends ResultReceiver {
        public MyResultReceiver(Handler handler) {

            // HANDLER - To update the UI element
            // WHENEVER THE WORK IS DOING IN THE BACKGROUND IT CAN'T ABLE TO UPDATE ANY UI ELEMENT ... SO THAT HANDLER IS USED
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            //Log.i(TAG,"Spark MyResultReceiver , Thread Name :" + Thread.currentThread().getName());

            if(resultCode == 18 && resultData != null){
                final String name = resultData.getString("get_name");
                final String email = resultData.getString("get_email");
                final String phno = resultData.getString("get_phno");
                final String img = resultData.getString("get_img");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.i(TAG,"Spark MyHandler , Thread Name :" + Thread.currentThread().getName());
                        name_pr.setText(name);
                        email_pr.setText(email);
                        phno_pr.setText(phno);
                        try {
                            Glide.with(getActivity()).load(img).into(profile_img);
                        }
                        catch (Exception e) { }
                    }
                });
            }
        }
    }

    // GALLERY
    private void openGallery() {
        Intent gallery = new Intent();
        gallery.setAction(gallery.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,IMAGE_GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            profileUri = data.getData();
            uploadToFirebase();
        }
    }

    // UPLOAD
    private void uploadToFirebase() {
        if(profileUri != null){
            progressDialog.setTitle("Uploading");
            progressDialog.setMessage("Please wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            profile_img.setImageURI(profileUri);

            final StorageReference reference = storage.getReference().child("profile_picture").child(mAuth.getUid());
            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getContext(),"UPLOADED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            db.getReference().child("Users").child(mAuth.getUid()).child("imageprofile").setValue(uri.toString());
                            //Toast.makeText(getContext(),"PROFILE PICTURE UPLOADED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    // SIGN OUT
    private void showDialogSignOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to Sign out?");
        builder.setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mAuth.signOut();
                sendUserToLoginActivity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    // ABOUT
    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_layout_about, null, false);
        abt_dialog.setContentView(view);
        abt_dialog.show();
        //abt_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        abt_dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }*/

    // BACK TO LOGIN PAGE
    private void sendUserToLoginActivity() {
        Intent homescreen = new Intent(getActivity(), MainActivity.class);
        startActivity(homescreen);
    }
}