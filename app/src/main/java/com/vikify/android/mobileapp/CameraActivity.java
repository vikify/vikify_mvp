package com.vikify.android.mobileapp;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.vikify.android.mobileapp.DataSaving.DBEntityClass;
import com.vikify.android.mobileapp.DataSaving.SavedVideosDB;
import com.vikify.android.mobileapp.DataSaving.SavingImages;
import com.vikify.android.mobileapp.DataSaving.VideoViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    CameraView cameraView;
    Button mButton,mUButton,mCancelButton,mSaveButton;
    VideoView mVideoView;
    Button stopBtn, replayBtn;
    private StorageReference mStorageRef;
    File mfile;
    File mVideoFileForUpload;
    TextView mTextViewProg;
    private static final int RECORD_REQUEST_CODE = 101;
    String mCreatorUID,mCreatorName,mPhoneNumber;
    long mtimestamp;
    Uri mVideoPath;
    SavedVideosDB videoDB;
    ImageView mCancelVector;
    FrameLayout mFrameLayout,mRecordingFrameLayout;
    ProgressBar progressBar;
    LinearLayout mBtnLayout,OptionsID;
    private VideoViewModel videoModel;
    EditText mVideoName,mDescription;
    Boolean isFront;
    AutoCompleteTextView mAutoCompleteTextView;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String valueName,valueTags;
    private DatabaseReference mDatabase;
    Button Start, Stop;
    TextView textView;
    List<String> tagsSelect= new ArrayList<>();
    List<String> selectedtags= new ArrayList<>();
    List<String> reversedSelectedTags=new ArrayList<>();
    private List<String> finalTags=new ArrayList<>();
    private static final String TAG = "CameraActivity";
    final String DATABASE_NAME = "SavedVideos";

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
        mSaveButton=findViewById(R.id.saveBtn);
        mSaveButton.setVisibility(View.GONE);
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

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        int permission1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if(permission1!=PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission to Capture denied");
            makeRequest();
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequest();
        }

//        videoModel = ViewModelProviders.of(this).get(.class);

        tagsSelect.add("Tag1");
        tagsSelect.add("Tag2");
        tagsSelect.add("Tag3");
        tagsSelect.add("Tag4");
        tagsSelect.add("Tag5");
        tagsSelect.add("Tag6");
        tagsSelect.add("Tag7");


        mfile=new File(getBaseContext().getFilesDir()+File.separator+"file.mp4");  //Temporary file to store the video on device

        videoDB = Room.databaseBuilder(getApplicationContext(),
                SavedVideosDB.class, DATABASE_NAME)
                .build();

        final CircularProgressBar circularProgressBar = (CircularProgressBar)findViewById(R.id.yourCircularProgressbar);
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.progressBarColor));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundProgressBarColor));
        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
        final int animationDuration = 60000; // 2500ms = 2,5s
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();




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
                mSaveButton.setVisibility(View.GONE);

            }
        });

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long mNameSize=dataSnapshot.child("VikifyDatabase").child("Content-details").getChildrenCount();
                Log.v(TAG,"Longq"+mNameSize);
                for(long i=1;i<=mNameSize;i++) {
                    String iString=Long.toString(i);
                    long mTagSize=dataSnapshot.child("VikifyDatabase").child("Content-details").child("Category"+iString).child("Tags").getChildrenCount();
                    for(long j=0;j<mTagSize;j++) {

                        String jString=Long.toString(j);
                        valueTags = dataSnapshot.child("VikifyDatabase").child("Content-details").child("Category" + iString).child("Tags").child(jString).getValue().toString();
                        valueTags=valueTags.replaceAll("[\\{\\}]"," ");
                        finalTags.add(valueTags);
                    }

                    tagsSelect=finalTags;
                    Log.v(TAG," Mtags1 "+finalTags);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                mSaveButton.setVisibility(View. VISIBLE);
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
                    mSaveButton.setVisibility(View.VISIBLE);
                    replayBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    mCancelVector.setVisibility(View.VISIBLE);
                    Log.v("VideoURI", "VideoURI" + mVideoPath);

                    mCancelVector.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                            builder.setMessage("This will permanently delete your recording. Are you sure you want to proceed?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!
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
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                            Log.v(TAG,"Cancelled");
                                        }
                                    }).show();
                            // Create the AlertDialog object and return it

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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                String uniquenumber = Long.toString(number);
                File copyfile = new File(getBaseContext().getFilesDir()+File.separator+uniquenumber+".mp4");
                String path=  SavingImages.savingvids(mfile,copyfile);
                Log.v(TAG,"PATIS"+path);
                run(path,getUnixTimeStamp(), NavDraw.getUserUID(),videoDB);
                finish();
                }
                catch (Exception e){
                    Log.v(TAG,"Exception"+e);
                }
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
                mSaveButton.setVisibility(View.GONE);

                if(mVideoPath!=null) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                     LayoutInflater inflater = CameraActivity.this.getLayoutInflater();
                    final View main_view = inflater.inflate(R.layout.dialog_layout, null);

                    builder.setView(main_view);

                    ArrayAdapter<String> adapter= new ArrayAdapter<String>(CameraActivity.this,R.layout.support_simple_spinner_dropdown_item,tagsSelect);
                    mAutoCompleteTextView=main_view.findViewById(R.id.autocomplete_view);
                    mAutoCompleteTextView.setAdapter(adapter);
                    mRecyclerView=main_view.findViewById(R.id.recycler_iew);


                    mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                            String text=parent.getItemAtPosition(position).toString();
                            selectedtags.add(text);
                            Collections.reverse(selectedtags);
                            mRecyclerView.setHasFixedSize(true);

                            // use a linear layout manager
                            mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            // specify an adapter (see also next example)
                            mAdapter = new TagSelectionAdapter(selectedtags);
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    });

                    builder.setPositiveButton(R.string.Sendname, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mVideoName=main_view.findViewById(R.id.videoName);
                                    mDescription=main_view.findViewById(R.id.video_description);
                                    String videoName= mVideoName.getText().toString();
                                    String description=mDescription.getText().toString();
                                    Log.v("TAG","VideoName:"+mVideoName.getText().toString());
                                    mButton.setVisibility(View.GONE);
                                    stopBtn.setVisibility(View.GONE);
                                    mUButton.setVisibility(View.GONE);
                                    replayBtn.setVisibility(View.GONE);
                                    mVideoView.pause();
                                    progressBar.setVisibility(View.VISIBLE);  mCancelButton.setVisibility(View.GONE);
                                    Toast.makeText(CameraActivity.this, "Video submitted", Toast.LENGTH_SHORT).show();
                                    sendDataToFirebase(mVideoFileForUpload.getPath(),videoName,selectedtags,description);
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
        String phoneNumber=camDets.getString("CreatorPhone");
        passCreatorPhone(phoneNumber);
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



    private String passCreatorUID(String UID){
        mCreatorUID= UID;
        Log.v("MEthod","UID"+mCreatorUID);
        return mCreatorUID;
    }

    private String passCreatorPhone(String number){
        mPhoneNumber= number;
        Log.v("MEthod","Phone"+mPhoneNumber);
        return mPhoneNumber;
    }

    private String passCreatorName(String name){
        mCreatorName=name;
        return mCreatorName;
    }
    public static long getUnixTimeStamp(){
        final long uniqueTimeStamp=System.currentTimeMillis()/1000;
        return uniqueTimeStamp;
    }


    private void sendDataToFirebase(String filePath, final String name, final List<String> selectedTags, final String description){
        final Uri fileUpload = Uri.fromFile(new File(filePath));

         mtimestamp=getUnixTimeStamp();
        Log.v(TAG,"File passed is "+fileUpload);
        Log.v(TAG,"File path is "+filePath);
        if(mCreatorName==null){
            mCreatorName=mPhoneNumber;
        }

            final StorageReference mVideoRef = mStorageRef.child("videos/" + mCreatorUID + "uniqueTimeStamp" + mtimestamp + "Name-" + name + "-" + "Creator-" + mCreatorName + "-Tags-" + selectedTags);  //Current time stamp in UTC
            mVideoRef.putFile(fileUpload)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "UnSuccessfull", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    updateProgress(taskSnapshot, name, selectedTags, mVideoRef, mCreatorName, mtimestamp, description);
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    //Implement when needed
                }
            });

    }


    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot,String name,List<String> selectedtag,StorageReference Ref,String creatorname,long uniqueTimeStamp,String description){
        long FileSize=taskSnapshot.getTotalByteCount();
        long uploadBytes=taskSnapshot.getBytesTransferred();

        long progress=(uploadBytes*100)/FileSize;
        String prog= Long.toString(progress);


        progressBar.setProgress((int)progress);
        mTextViewProg.setText(prog);
        if(progress==100){

            senddataToRealTimeDataBase(name,selectedtag,Ref,creatorname,uniqueTimeStamp,description);
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

    public void senddataToRealTimeDataBase(final String name, final List<String> selectedtags, final StorageReference Ref, final String creatorName, final long uniqueTimeStamp, final String description){


        if(true){
            Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    finalLoadMethod(name,selectedtags,url,creatorName,uniqueTimeStamp,description);
                    Log.v(TAG,"URIS"+uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v(TAG,"Failed "+e);
                }
            });
        }

    }

    public void finalLoadMethod(String name,List<String> selectedtags, String uri,String creatorName, long uniqueTimeStamp,String description){   //ref is removed from a parameter

        VideoDetailsClass videoName=new VideoDetailsClass(name,selectedtags,uri,creatorName,description);
        String timeStamp=Long.toString(uniqueTimeStamp);
        Log.v(TAG,"Name "+name+"Tags "+selectedtags+"URI "+uri+ " Description "+description);
        mDatabase = FirebaseDatabase.getInstance().getReference();
       try {
           mDatabase.child("VikifyDatabase").child("Video-details").child("Video By "+creatorName+" At "+timeStamp+" CreatorUID"+mCreatorUID).setValue(videoName);
       }
       catch (Exception e){

       }

    }


    public void run(final String path, final long timeStamp, final String userUID, final SavedVideosDB videoDB){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBEntityClass videoEntity = new DBEntityClass();
                videoEntity.setCreatorUID(userUID);
                videoEntity.setmFilePath(path);
                videoEntity.setUnixTimeStamp(timeStamp);
                videoDB.daoAccess().insertOnlySingleVideo(videoEntity);
                try {
                    videoModel.insert(videoEntity);
                }
                catch (Exception e){
                    Log.v(TAG,"Exception: "+e);
                }
                LiveData<List<DBEntityClass>> videos=videoDB.daoAccess().getAllVideos();
                Log.v(TAG,"VideosAretotal:"+videoDB.daoAccess().getVideos(userUID));
               LiveData<List<DBEntityClass>> videosare=videoDB.daoAccess().getVideos(userUID);
                videoDB.close();
            }
        }) .start();
        Toast.makeText(CameraActivity.this,"Video Saved",Toast.LENGTH_SHORT).show();
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
        }
    }
}
