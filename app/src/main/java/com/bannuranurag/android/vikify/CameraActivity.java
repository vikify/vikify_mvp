package com.bannuranurag.android.vikify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;

import java.io.File;

public class CameraActivity extends AppCompatActivity {
    CameraView cameraView;
    ImageView mImageView;
    Button mButton,mUButton;
    VideoView mVideoView;
    Button stopBtn;
    private StorageReference mStorageRef;
    File mfile;
    File mVideoFileForUpload;
    TextView mTextViewProg;
    String mCreatorUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mStorageRef=FirebaseStorage.getInstance().getReference();

        cameraView=findViewById(R.id.camera);
        mButton=findViewById(R.id.picture);
        cameraView.setFacing(Facing.FRONT);
        mUButton=findViewById(R.id.uploadBtn);
        mTextViewProg=findViewById(R.id.textViewprog);
//        mImageView=findViewById(R.id.imageView);
        mVideoView=findViewById(R.id.videoView);
        stopBtn=findViewById(R.id.stopbtn);
        mfile=new File(getBaseContext().getFilesDir()+"niki.mp4");



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cameraView.capturePicture();

                Log.v("TAG","mfile"+mfile);
                cameraView.startCapturingVideo(mfile);
                Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT).show();


            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.stopCapturingVideo();
                Toast.makeText(getApplicationContext(),"Stopped",Toast.LENGTH_SHORT).show();
            }
        });

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(File video) {
                // The File is the same you passed before.
                // Now it holds a MP4 video.
                Log.v("Video","Video"+video);
//
                mVideoFileForUpload=video;
                mVideoView.setVideoURI(Uri.parse("file:///"+video.getPath()));
                mVideoView.start();

            }

        });
        mUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToFirebase(mVideoFileForUpload.getPath());
            }
        });

        Intent mIntent=getIntent();
       String uid= mIntent.getStringExtra("CreatorUID");
       passCreatorUID(uid);
        Log.v("CAMUID","UID"+uid);

    }
    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }
    private void CallToFunction(Bitmap bmp)
    {
        if (bmp != null)
        {
            Toast.makeText(getApplicationContext(),"Not null",Toast.LENGTH_SHORT).show();
        }
    }

    private String passCreatorUID(String UID){
        mCreatorUID= UID;
        Log.v("MEthod","UID"+mCreatorUID);
        return mCreatorUID;
    }

    private void sendDataToFirebase(String filePath){
        Uri fileUpload = Uri.fromFile(new File(filePath));
        StorageReference mVideoRef= mStorageRef.child("videos/"+mCreatorUID);

        mVideoRef.putFile(fileUpload)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"UnSuccessfull",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                updateProgress(taskSnapshot);
            }
        });
    }

    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot){
        long FileSize=taskSnapshot.getTotalByteCount();
        long uploadBytes=taskSnapshot.getBytesTransferred();

        long progress=(uploadBytes*100)/FileSize;
        String prog= Long.toString(progress);

        ProgressBar progressBar=findViewById(R.id.progress_bar);
        progressBar.setProgress((int)progress);
        mTextViewProg.setText(prog);


    }


}
