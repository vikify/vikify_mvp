package com.bannuranurag.android.vikify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    Button stopBtn, replayBtn;
    private StorageReference mStorageRef;
    File mfile;
    File mVideoFileForUpload;
    TextView mTextViewProg;
    String mCreatorUID;
    Uri mVideoPath;
    ImageView mCancelVector;
    FrameLayout mFrameLayout;
    ProgressBar progressBar;
    LinearLayout mBtnLayout;
    EditText mVideoName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mStorageRef=FirebaseStorage.getInstance().getReference();

        cameraView=findViewById(R.id.camera);
        mButton=findViewById(R.id.picture);
        cameraView.setFacing(Facing.FRONT);
        mUButton=findViewById(R.id.uploadBtn);
        replayBtn=findViewById(R.id.replayBtn);
        mTextViewProg=findViewById(R.id.textViewprog);
        mCancelVector=findViewById(R.id.cancelIcon);
        mFrameLayout=findViewById(R.id.replayFrame);
        mFrameLayout.setVisibility(View.GONE);
        progressBar=findViewById(R.id.progress_bar);
        mBtnLayout=findViewById(R.id.btnLayout);
//        mImageView=findViewById(R.id.imageView);
        mVideoView=findViewById(R.id.videoView);
//        mVideoView.setVisibility(View.GONE);
//        mCancelVector.setVisibility(View.GONE);
        mVideoName=findViewById(R.id.videoName);

        stopBtn=findViewById(R.id.stopbtn);
        mfile=new File(getBaseContext().getFilesDir()+"niki.mp4");




        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cameraView.capturePicture();

                Log.v("TAG","mfile"+mfile);
                cameraView.startCapturingVideo(mfile);
                Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT).show();
                if(mUButton.getVisibility()==View.GONE){
                    mUButton.setVisibility(View.VISIBLE);
                }
                if(replayBtn.getVisibility()==View.GONE){
                    replayBtn.setVisibility(View.VISIBLE);
                }


            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.stopCapturingVideo();
                Toast.makeText(getApplicationContext(),"Stopped",Toast.LENGTH_SHORT).show();
                if(mUButton.getVisibility()==View.GONE){
                    mUButton.setVisibility(View.VISIBLE);
                }
                if(replayBtn.getVisibility()==View.GONE){
                    replayBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoPath!=null) {
                    cameraView.setVisibility(View.GONE);
                    mVideoView.setVideoURI(mVideoPath);
                    mFrameLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    mBtnLayout.setVisibility(View.GONE);



                    Log.v("VideoURI", "VideoURI" + mVideoPath);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Record a video first",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCancelVector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mBtnLayout.setVisibility(View.VISIBLE);
                cameraView.setVisibility(View.VISIBLE);
                mCancelVector.setVisibility(View.GONE);
                mFrameLayout.setVisibility(View.GONE);
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
                sendVideoPath(Uri.parse("file:///"+video.getPath()));

            }

        });
        mUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoPath!=null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                    LayoutInflater inflater = CameraActivity.this.getLayoutInflater();


                    builder.setView(inflater.inflate(R.layout.dialog_layout, null))
                            .setPositiveButton(R.string.Sendname, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(CameraActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                                    String videoName= mVideoName.getText().toString();
                                    replayBtn.setVisibility(View.GONE);
                                    mUButton.setVisibility(View.GONE);
                                    Toast.makeText(CameraActivity.this, videoName, Toast.LENGTH_SHORT).show();
                                    sendDataToFirebase(mVideoFileForUpload.getPath());
                                }
                            }).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Record a video first",Toast.LENGTH_SHORT).show();
                }

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
        StorageReference mVideoRef= mStorageRef.child("videos/"+mCreatorUID+"uniqueTimeStamp"+System.currentTimeMillis()/1000);  //Current time stamp in UTC



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


        progressBar.setProgress((int)progress);
        mTextViewProg.setText(prog);


    }

    private Uri sendVideoPath(Uri videoPath){
        mVideoPath=videoPath;
        return mVideoPath;
    }

}
