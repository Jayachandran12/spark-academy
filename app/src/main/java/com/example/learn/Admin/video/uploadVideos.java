package com.example.learn.Admin.video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.learn.R;
import com.example.learn.Helper_class.Model.filemodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadVideos extends AppCompatActivity {


    private TextView title,videoName,videoDesc;
    private String videoId,vName;

    //video
    private int VIDEO_GALLERY_REQUEST=101;
    private VideoView videoview;
    private Button browse , upload ;
    private Uri videoUri;
    private MediaController mediaController;

    //Firebase
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_videos);

        title = (TextView) findViewById(R.id.title_UV);
        videoName = (EditText) findViewById(R.id.videoTitle_UV);
        videoDesc = (EditText) findViewById(R.id.videoDesc_UV);


        //video
        videoview = (VideoView) findViewById(R.id.VideoView_UV);
        browse = (Button) findViewById(R.id.browse_UP);
        upload = (Button) findViewById(R.id.upload_UP);

        //media controller
        mediaController = new MediaController(this);
        videoview.setMediaController(mediaController);
        videoview.start();

        //getting title
        Intent intent = getIntent();
        String titlename = intent.getStringExtra("subject");

        //setting title (header)
        title.setText(titlename);

        //Firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Course").child(titlename);

        //browse
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,VIDEO_GALLERY_REQUEST);
            }
        });

        //upload
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase(titlename);
            }
        });


    }

    //browse (continuation)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            videoUri = data.getData();
            videoview.setVideoURI(videoUri);
        }
    }

    //upload (continuation 2)
    public String getExtension(){
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(getContentResolver().getType(videoUri));
    }

    //upload (continuation 1)
    private void uploadToFirebase(String titlefetch) {

        //progressbar
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Lecture");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        //firebase storage
        final StorageReference databaseupload = storageReference.child("Lessons/" + System.currentTimeMillis()+"."+getExtension());
        databaseupload.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //creating url for saved video in firebase storage
                databaseupload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //sending data to next Activity
                        String vName = videoName.getText().toString();

                        filemodel obj = new filemodel(vName,uri.toString(),videoDesc.getText().toString(),"");

                        // creating ramdom generator code and pushing it into the firebase
                        String upload_id = databaseReference.push().getKey();

                        databaseReference.child("Videos").child(upload_id).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //assigning random id to string to move to next activity
                                videoId = upload_id;
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"successfully uploaded",Toast.LENGTH_SHORT).show();
                                if(videoId.equals(upload_id)){
                                    startActivity(new Intent(uploadVideos.this,uploadThumbnail.class)
                                            .putExtra("videoId",videoId)
                                            .putExtra("videoName",vName)
                                            .putExtra("Lecture",titlefetch)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"failed to upload",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float per = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                pd.setMessage("Uploaded :"+(int)per+"%");
            }
        });
    }

}