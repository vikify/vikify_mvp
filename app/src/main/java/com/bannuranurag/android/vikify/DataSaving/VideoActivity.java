package com.bannuranurag.android.vikify.DataSaving;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.bannuranurag.android.vikify.NavDraw;
import com.bannuranurag.android.vikify.R;
import com.bannuranurag.android.vikify.TagSelectionAdapter;
import com.bannuranurag.android.vikify.VideoDetailsClass;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.otaliastudios.cameraview.CameraView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoActivity extends AppCompatActivity {
    CameraView cameraView;
    Button mUButton,mDeleteButton;
    VideoView mVideoView;
//    Button stopBtn, replayBtn;
    private StorageReference mStorageRef;
    File mfile;
    File mVideoFileForUpload;
    String mCreatorUID,mCreatorName;
    long mtimestamp;
    Uri mVideoPath;
    SavedVideosDB videoDB;
    ImageView mCancelVector;
    FrameLayout mFrameLayout,mRecordingFrameLayout;
    ProgressBar progressBar;
    SavedVideosDB videosDB;
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
    TextView textView;
    List<String> tagsSelect= new ArrayList<>();
    List<String> selectedtags= new ArrayList<>();
    List<String> reversedSelectedTags=new ArrayList<>();
    private List<String> finalTags=new ArrayList<>();
    private static final String TAG = "VideoActivity";
    final String DATABASE_NAME = "SavedVideos";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mStorageRef=FirebaseStorage.getInstance().getReference();
        mUButton=findViewById(R.id.upload_saved_video);
        textView=findViewById(R.id.textViewprog);
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        mBtnLayout=findViewById(R.id.btnLayout);
        mVideoView=findViewById(R.id.play_saved_video_view);
        OptionsID=findViewById(R.id.options_id);
        mDeleteButton=findViewById(R.id.delete_saved_video);
        tagsSelect.add("Tag1");
        tagsSelect.add("Tag2");
        tagsSelect.add("Tag3");
        tagsSelect.add("Tag4");
        tagsSelect.add("Tag5");
        tagsSelect.add("Tag6");
        tagsSelect.add("Tag7");
        videoDB = Room.databaseBuilder(getApplicationContext(),
                SavedVideosDB.class, DATABASE_NAME)
                .build();

        Intent mIntent=getIntent();
        String url=mIntent.getStringExtra("URI");
        final String path=mIntent.getStringExtra("Filepath");
        final int position = mIntent.getIntExtra("Position",0);

        final Uri uri = Uri.parse(url);
        mVideoView.setVideoURI(uri);
        mVideoView.start();

        mfile=new File(url);  //Temporary file to store the video on device


        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    mDeleteButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    videoDB.daoAccess().deleteVideoWithPath(path);
                }
            }) .start();


            Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
//            Intent goBackToSavedVideos= new Intent(VideoActivity.this, SavedVideos.class);
//            startActivity(goBackToSavedVideos);
            finish();
//            videoToBeDeleted.getKey(position);

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


        mUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mfile!=null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                    LayoutInflater inflater = VideoActivity.this.getLayoutInflater();
                    final View main_view = inflater.inflate(R.layout.dialog_layout, null);

                    builder.setView(main_view);

                    ArrayAdapter<String> adapter= new ArrayAdapter<String>(VideoActivity.this,R.layout.support_simple_spinner_dropdown_item,tagsSelect);
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
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(VideoActivity.this, "Video submitted", Toast.LENGTH_SHORT).show();
                            sendDataToFirebase(path,videoName,selectedtags,description);
                        }
                    }).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Record a video first",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront=true;

    }

    @Override
    protected void onPause() {
        super.onPause();

        isFront=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    public static long getUnixTimeStamp(){
        final long uniqueTimeStamp=System.currentTimeMillis()/1000;
        return uniqueTimeStamp;
    }



    private void sendDataToFirebase(String filePath, final String name, final List<String> selectedTags, final String description){
        final Uri fileUpload = Uri.fromFile(new File(filePath));
        mtimestamp=getUnixTimeStamp();
        Log.v(TAG,"File passed is "+fileUpload);
        final StorageReference mVideoRef= mStorageRef.child("videos/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"uniqueTimeStamp"+mtimestamp+"Name-"+name+"-"+"Creator-"+FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"-Tags-"+selectedTags);  //Current time stamp in UTC
        mVideoRef.putFile(fileUpload)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"UnSuccessfull",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                updateProgress(taskSnapshot,name,selectedTags,mVideoRef,mCreatorName,mtimestamp,description);
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
        textView.setText(prog);
        if(progress==100){

            senddataToRealTimeDataBase(name,selectedtag,Ref,creatorname,uniqueTimeStamp,description);
            Intent mIntent=new Intent(VideoActivity.this,NavDraw.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mIntent);
        }

    }

    private Uri sendVideoPath(Uri videoPath){
        mVideoPath=videoPath;
        return mVideoPath;
    }



    public void senddataToRealTimeDataBase(final String name, final List<String> selectedtags, final StorageReference Ref, final String creatorName, final long uniqueTimeStamp, final String description){


        if(true){
            Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    finalLoadMethod(name,selectedtags,Ref,url,creatorName,uniqueTimeStamp,description);
                    Log.v(TAG,"URIIS"+uri);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v(TAG,"Failed");
                }
            });
        }

    }

    public void finalLoadMethod(String name,List<String> selectedtags,StorageReference Ref, String uri,String creatorName, long uniqueTimeStamp,String description){

        VideoDetailsClass videoName=new VideoDetailsClass(name,selectedtags,uri,creatorName,description);
        String timeStamp=Long.toString(uniqueTimeStamp);
        Log.v(TAG,"Name "+name+"Tags "+selectedtags+"URI "+uri+ " Description "+description);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        try {
            mDatabase.child("VikifyDatabase").child("Video-details").child("Video By "+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+" At "+timeStamp+" CreatorUID"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(videoName);
        }
        catch (Exception e){

        }

    }



}
