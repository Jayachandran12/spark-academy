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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn.Admin.AdminActivity;
import com.example.learn.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadThumbnail extends AppCompatActivity {

    private TextView title;
    private Button browse,upload;
    private ImageView viewImage;

    //image
    private int IMAGE_GALLERY_REQUEST=102;
    private Uri ThumbnailUri;

    //Firebase
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_thumbnail);

        //getting details (header)
        Intent intent = getIntent();
        String videoId = intent.getStringExtra("videoId");
        String LectureName = intent.getStringExtra("videoName");
        String video_Name = intent.getStringExtra("Lecture");

        //other fields
        title = (TextView) findViewById(R.id.thumbTitle);
        browse = (Button) findViewById(R.id.browseThumbnail);
        upload = (Button) findViewById(R.id.uploadThumbnail);
        viewImage = (ImageView) findViewById(R.id.viewThumbnail);

        // FIREBASE
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Course").child(video_Name).child("Videos").child(videoId);

        //setting details
        title.setText(video_Name);

        //browse
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setAction(gallery.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,IMAGE_GALLERY_REQUEST);
            }
        });

        //upload
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase();
            }
        });
    }

    //browse (continuation)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            ThumbnailUri = data.getData();
            viewImage.setImageURI(ThumbnailUri);
        }
    }

    //upload (continuation 2)
    public String getExtension(){
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(getContentResolver().getType(ThumbnailUri));
    }

    private void uploadToFirebase() {

        //progressbar
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Thumbnail");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        //firebase storage
        final StorageReference reference = storageReference.child("Thumbnail/" + System.currentTimeMillis()+"."+getExtension());
        reference.putFile(ThumbnailUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //creating url for saved image in firebase storage
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.child("vThumb").setValue(uri.toString());
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(uploadThumbnail.this, AdminActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float per = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Uploaded : " + (int) per + "%");
            }
        });
    }
}