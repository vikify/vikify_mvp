package com.bannuranurag.android.vikify;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;

import java.io.File;

public class CameraActivity extends AppCompatActivity {
    CameraView cameraView;
    ImageView mImageView;
    Button mButton,mUButton,mCancelButton,ReplayBtnFull;
    VideoView mVideoView;
    Button stopBtn, replayBtn;
    private StorageReference mStorageRef;
    File mfile;
    File mVideoFileForUpload;
    TextView mTextViewProg;
    String mCreatorUID,mCreatorName;
    Uri mVideoPath;
    ImageView mCancelVector;
    FrameLayout mFrameLayout,mRecordingFrameLayout;
    ProgressBar progressBar;
    LinearLayout mBtnLayout,OptionsID;
    EditText mVideoName;
    Boolean isFront;

    Button Start, Stop;
    TextView textView;

    private static final String TAG = "CameraActivity";

    CountDownTimer countDownTimer=new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            String time=Long.toString(millisUntilFinished / 1000);
            textView.setText(time);
            textView.setVisibility(View.VISIBLE);
        }

        public void onFinish() {
            textView.setText("done!");
            stopRecording();
            countDownTimer.cancel();
            textView.setVisibility(View.GONE);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mStorageRef=FirebaseStorage.getInstance().getReference();
        mRecordingFrameLayout=findViewById(R.id.recording_frame_layout);
        cameraView=findViewById(R.id.camera);
        mButton=findViewById(R.id.picture);
        cameraView.setFacing(Facing.FRONT);
        mUButton=findViewById(R.id.uploadBtn);
        mUButton.setVisibility(View.GONE);
        replayBtn=findViewById(R.id.replayBtn);
        replayBtn.setVisibility(View.GONE);
        mTextViewProg=findViewById(R.id.textViewprog);
        mCancelVector=findViewById(R.id.cancelIcon);
        mFrameLayout=findViewById(R.id.replayFrame);
        mFrameLayout.setVisibility(View.GONE);
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        mBtnLayout=findViewById(R.id.btnLayout);
        mCancelButton=findViewById(R.id.cancelUpload);
        mVideoView=findViewById(R.id.videoView);
        mCancelButton.setVisibility(View.GONE);
        stopBtn=findViewById(R.id.stopbtn);
        stopBtn.setVisibility(View.GONE);
        OptionsID=findViewById(R.id.options_id);


        mfile=new File(getBaseContext().getFilesDir()+"niki.mp4");  //Temporarry file to store the video on device



        final CircularProgressBar circularProgressBar = (CircularProgressBar)findViewById(R.id.yourCircularProgressbar);
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.progressBarColor));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundProgressBarColor));
        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
        final int animationDuration = 60000; // 2500ms = 2,5s





        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cameraView.capturePicture();
                 circularProgressBar.setVisibility(View.VISIBLE);
                circularProgressBar.setProgressWithAnimation(100, animationDuration);
                countDownTimer.start();
                Log.v("TAG","mfile"+mfile);

                cameraView.startCapturingVideo(mfile);
                Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT).show();
               mButton.setVisibility(View.GONE);
               stopBtn.setVisibility(View.VISIBLE);
               mUButton.setVisibility(View.GONE);
               replayBtn.setVisibility(View.GONE);
               progressBar.setVisibility(View.GONE);

            }
        });

        stopBtn.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setVisibility(View.GONE);
                stopBtn.setVisibility(View.GONE);
                mUButton.setVisibility(View.VISIBLE);
                replayBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                circularProgressBar.setProgress(0);
                countDownTimer.onFinish();
                stopRecording();

            }
        });


        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    mVideoView.setVideoURI(mVideoPath);
                    circularProgressBar.setVisibility(View.GONE);
                    mVideoView.start();
                    cameraView.setVisibility(View.GONE);
                    mRecordingFrameLayout.setVisibility(View.GONE);
                    mFrameLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    mButton.setVisibility(View.GONE);
                    stopBtn.setVisibility(View.GONE);
                    mUButton.setVisibility(View.VISIBLE);
                    replayBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    mCancelVector.setVisibility(View.VISIBLE);
                    Log.v("VideoURI", "VideoURI" + mVideoPath);

                    mCancelVector.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.GONE);
                            mBtnLayout.setVisibility(View.VISIBLE);
                            cameraView.setVisibility(View.VISIBLE);
                            mCancelVector.setVisibility(View.GONE);
                            mFrameLayout.setVisibility(View.GONE);
                            mVideoView.stopPlayback();
                            mCancelButton.setVisibility(View.GONE);
                           stopBtn.setVisibility(View.GONE);
                            mUButton.setVisibility(View.GONE);
                            replayBtn.setVisibility(View.GONE);
                            mButton.setVisibility(View.VISIBLE);
                        }
                    });

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
                mVideoView.stopPlayback();
                mCancelButton.setVisibility(View.GONE);
                mVideoView.stopPlayback();
                mButton.setVisibility(View.VISIBLE);
            }
        });

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(File video) {
                // The File is the same you passed before.
                // Now it holds a MP4 video.
                Log.v("Video","Video"+video);
                mVideoFileForUpload=video;
                sendVideoPath(Uri.parse("file:///"+video.getPath()));

            }

        });
        mUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoPath!=null) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                     LayoutInflater inflater = CameraActivity.this.getLayoutInflater();
                    final View main_view = inflater.inflate(R.layout.dialog_layout, null);

                    builder.setView(main_view)
                            .setPositiveButton(R.string.Sendname, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mVideoName=main_view.findViewById(R.id.videoName);
                                    Toast.makeText(CameraActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                                    String videoName= mVideoName.getText().toString();
                                    Log.v("TAG","VideoName:"+mVideoName.getText().toString());
                                    mButton.setVisibility(View.GONE);
                                    stopBtn.setVisibility(View.GONE);
                                    mUButton.setVisibility(View.GONE);
                                    replayBtn.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.VISIBLE);  mCancelButton.setVisibility(View.GONE);
                                    Toast.makeText(CameraActivity.this, "Name submitted", Toast.LENGTH_SHORT).show();
                                    sendDataToFirebase(mVideoFileForUpload.getPath(),videoName);
                                }
                            }).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Record a video first",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Intent mIntent=getIntent();
        Bundle camDets=mIntent.getExtras();
        String uid= camDets.getString("CreatorUID");
        String name=camDets.getString("CreatorName");
        passCreatorUID(uid);
        passCreatorName(name);
        Log.v("CAMUID","UID"+uid);


        textView=findViewById(R.id.textView);
        textView.setVisibility(View.GONE);






    }
    @Override
    protected void onResume() {
        super.onResume();
        isFront=true;
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
        isFront=false;
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

    private String passCreatorName(String name){
        mCreatorName=name;
        return mCreatorName;
    }


    private void sendDataToFirebase(String filePath,String name){
        final Uri fileUpload = Uri.fromFile(new File(filePath));
        final StorageReference mVideoRef= mStorageRef.child("videos/"+mCreatorUID+"uniqueTimeStamp"+System.currentTimeMillis()/1000+"Name-"+name+"-"+"Creator-"+mCreatorName);  //Current time stamp in UTC



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
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
               //Implement when needed
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Implement when needed
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
        if(progress==100){
            Intent mIntent=new Intent(CameraActivity.this,NavDraw.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mIntent);
        }

    }

    private Uri sendVideoPath(Uri videoPath){
        mVideoPath=videoPath;
        return mVideoPath;
    }

    public void stopRecording(){
        cameraView.stopCapturingVideo();

        if(isFront==true) {
            Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
        }
        if(mUButton.getVisibility()==View.GONE){
            mUButton.setVisibility(View.VISIBLE);
        }
        if(replayBtn.getVisibility()==View.GONE){
            replayBtn.setVisibility(View.VISIBLE);
        }

    }

}
