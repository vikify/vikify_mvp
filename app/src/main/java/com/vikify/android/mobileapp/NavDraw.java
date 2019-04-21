package com.vikify.android.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class NavDraw extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<String> tagList = new ArrayList<>();
    private RecyclerView mRecycleViewVertical;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    List<String> mFinalName = new ArrayList<>();
    List<String> mName = new ArrayList<>();
    HorizontalClass video_class;
    EditText videoName, videoLink;
    private List<HorizontalClass> yearList = new ArrayList<>();
    private List<HorizontalClass> videoList = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<String> finalTags = new ArrayList<>();
    String valueName, valueTags;
    List<String> tagsSelect= new ArrayList<>();
    LinearLayout mLinearLayout;
    ProgressBar mProgresBar;
    private DatabaseReference mDatabase;
    StorageReference storageReference, mVideoReference;
    DatabaseReference mDatabaseReference, databaseReference;
    private static final String TAG = "State";
    AlertDialog.Builder builder;
    AutoCompleteTextView mAutoCompleteTextView;
    RecyclerView mRecyclerView;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    final List<String> selectedtags= new ArrayList<>();
    GoogleSignInClient mGoogleSignInClient;
    TextView mTextViewName, mTextViewEmail;
    ImageView mImageView;
    FirebaseUser user;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPLY = "com.example.android.VideoList.REPLY";

    @Override
    protected void onResume() {
        Log.v(TAG, "OnResume");
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        Log.v(TAG, "ondestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "Onstop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "OnStart");
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_draw);
        Log.v(TAG, "OnCreate");

        mProgresBar = findViewById(R.id.progress_bar);
        mProgresBar.setVisibility(View.VISIBLE);

        storageReference = FirebaseStorage.getInstance().getReference();
        mVideoReference = storageReference.child("videos/");
        mAuth = FirebaseAuth.getInstance();

        getKeyHash();
//        String Uid= FirebaseAuth.getInstance().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        builder = new AlertDialog.Builder(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        mTextViewName = (TextView) hView.findViewById(R.id.header_user_name);
        mTextViewEmail = hView.findViewById(R.id.header_user_email);
        mImageView = hView.findViewById(R.id.header_image_view);
        navigationView.setNavigationItemSelectedListener(this);

        tagsSelect.add("Tag1");
        tagsSelect.add("Tag2");
        tagsSelect.add("Tag3");
        tagsSelect.add("Tag4");
        tagsSelect.add("Tag5");
        tagsSelect.add("Tag6");
        tagsSelect.add("Tag7");


        try {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    user = firebaseAuth.getCurrentUser();
                    Log.v(TAG, "TestFB" + user);
                    if (user != null) {
                        Log.v(TAG, "User is not null");
                        user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mTextViewName.setText(user.getDisplayName());
                                mTextViewEmail.setText(user.getEmail());
                                if (user.getDisplayName() == null) {
                                    mTextViewName.setText(user.getPhoneNumber());
                                }
                                try {
                                    URL mUrl = new URL(user.getPhotoUrl().toString());
                                    GlideApp
                                            .with(getApplicationContext())
                                            .load(mUrl)
                                            .override(130, 130)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(mImageView);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());


                    } else {
                        // User is signed out
                        Log.v(TAG, "User is null");
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };
        } catch (NullPointerException e) {
            Log.v(TAG, "Nullpointer" + e);
        }

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int count = 0;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    count++;
                    if (count >= dataSnapshot.getChildrenCount()) {
                        //stop progress bar here
                        mProgresBar.setVisibility(View.GONE);
                        Log.v(TAG, "Loaded");

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

//Video population
        databaseReference = FirebaseDatabase.getInstance().getReference().child("VikifyDatabase").child("Video-details");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videoList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String creatorName = ds.child("name").getValue().toString();
                        long count = ds.child("tags").getChildrenCount();
                        Log.v(TAG, "TAGSCOUNT" + count);
                        List<String> taglist = new ArrayList<>();
                        for (long i = 0; i < count; i++) {
                            String tagsCount = Long.toString(i);
                            String tags = ds.child("tags").child(tagsCount).getValue().toString();
                            taglist.add(tags);
                        }
                        String videoName = ds.child("mVideoName").getValue().toString();
                        String videoURL = ds.child("downloadUrl").getValue().toString();
                        String videoDescription = ds.child("description").getValue().toString();
                        // Log.v(TAG,"NAME12345" + creatorName + " Tags: " + tags + " URL: " + videoURL+" Dscrip"+videoDescription+" VideoName"+videoName);
                        video_class = new HorizontalClass(creatorName, videoName, videoURL, videoDescription, taglist);
                        Log.v(TAG, "Feed names" + video_class.getmCreator() + " Feed tags" + video_class.getTags());
                        videoList.add(video_class);

                    } catch (NullPointerException e) {
                        Log.v(TAG, "NULL at feed population" + e);
                    }
//                    for(long i=0;i<videoList.size();i++) {
//
//
//                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFinalName.clear();
                finalTags.clear();
                HashSet<HorizontalClass> hashSet = new HashSet<HorizontalClass>();
                hashSet.addAll(videoList);
                videoList.clear();
                videoList.addAll(hashSet);
                Log.v(TAG, "sexuio" + videoList.size());
                TagClass tagClass = new TagClass();
                Log.v(TAG, "Data89" + dataSnapshot);
                long mNameSize = dataSnapshot.child("VikifyDatabase").child("Content-details").getChildrenCount();

                for (long i = 1; i <= mNameSize; i++) {
                    String iString = Long.toString(i);
                    long mTagSize = dataSnapshot.child("VikifyDatabase").child("Content-details").child("Category" + iString).child("Tags").getChildrenCount();
                    valueName = dataSnapshot.child("VikifyDatabase").child("Content-details").child("Category" + iString).child("Name").getValue().toString();
                    valueName = valueName.replaceAll("[\\[\\]]", " ");
                    mName.add(valueName);
                    for (long j = 0; j < mTagSize; j++) {

                        String jString = Long.toString(j);
                        valueTags = dataSnapshot.child("VikifyDatabase").child("Content-details").child("Category" + iString).child("Tags").child(jString).getValue().toString();
                        valueTags = valueTags.replaceAll("[\\{\\}]", " ");
                        finalTags.add(valueTags);

                    }

                    tags = finalTags;
                    Log.v(TAG, " Mtags " + finalTags);
                    tagClass.setmTags(tags);
                    mFinalName = mName;
                }
                Log.v(TAG, "Mname12:" + mFinalName);


                mRecycleViewVertical = findViewById(R.id.verticle_recycle);


                mRecycleViewVertical.setHasFixedSize(true);


                //Linear layout manager
                mLayoutManager = new LinearLayoutManager(NavDraw.this);


                mRecycleViewVertical.setLayoutManager(mLayoutManager);

                Log.v(TAG, "VideoList" + videoList);

                mAdapter = new verticalAdapter(getApplicationContext(), mFinalName, videoList, finalTags, databaseReference);


                Log.v(TAG, "hello100" + mFinalName);


                mRecycleViewVertical.setAdapter(mAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(TAG, "Value Error"+databaseError);

            }

        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menue, menu);
        return true;
    }


    public static String getUserUID() {
        FirebaseUser camUser = FirebaseAuth.getInstance().getCurrentUser();
        String camuid = camUser.getUid();
        return camuid;
    }

    public  static  String getUserName(){
        FirebaseUser camUser = FirebaseAuth.getInstance().getCurrentUser();
        String camName = camUser.getDisplayName();
        return camName;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goToVideoList) {
            Intent mIntent = new Intent(NavDraw.this, CameraActivity.class);
            FirebaseUser camUser = FirebaseAuth.getInstance().getCurrentUser();
            Log.v("CamUser", "CamUser" + camUser);
            Bundle camDetails = new Bundle();
            String camName = camUser.getDisplayName();
            String phoneNumber = camUser.getPhoneNumber();
            camDetails.putString("CreatorUID", getUserUID());
            camDetails.putString("CreatorPhone", phoneNumber);
            camDetails.putString("CreatorName", camName);//Get the user UID while recording

            mIntent.putExtras(camDetails);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        // Handle the camera action
        if (id == R.id.nav_gallery) {

            Intent intent = new Intent(NavDraw.this, SavedVideos.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);

        } else if (id == R.id.nav_goto_profile) {
            Intent mIntent = new Intent(this, ProfileActivity.class);
            FirebaseUser camUser = FirebaseAuth.getInstance().getCurrentUser();
            Bundle extras = new Bundle();
            extras.putString("CreatorUID", camUser.getUid());
            extras.putString("CreatorName", camUser.getDisplayName());
            mIntent.putExtras(extras);
            startActivity(mIntent);
        } else if (id == R.id.upload_youtube_videos) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NavDraw.this);
            LayoutInflater inflater = NavDraw.this.getLayoutInflater();
            final View main_view = inflater.inflate(R.layout.dialog_layout, null);
            builder.setView(main_view);
            videoName = main_view.findViewById(R.id.videoName);
            videoLink = main_view.findViewById(R.id.video_description);
            videoLink.setHint("Paste Youtube link");
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(NavDraw.this,R.layout.support_simple_spinner_dropdown_item,tagsSelect);
            mAutoCompleteTextView=main_view.findViewById(R.id.autocomplete_view);
            mAutoCompleteTextView.setAdapter(adapter);
            mRecyclerView=main_view.findViewById(R.id.recycler_iew);
            //final List<String> selectedtags= new ArrayList<>();

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
                    if (videoName.getText().toString().trim().length()>0 && videoLink.getText().toString().trim().length()>0 && videoLink.getText().toString().contains("you")) {
                        verifyYoutubeUrl(videoLink.getText().toString(),selectedtags,videoName.getText().toString());

                    } else{
                        Toast.makeText(NavDraw.this,"Please provide a valid name and url",Toast.LENGTH_LONG).show();
                    }
                    }
            }).show();

        } else if (id == R.id.sign_out) {
            builder.setMessage("Do you want to signout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            signOut();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Signout");
            alert.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // ...Toas
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        Intent mIntent = new Intent(getApplicationContext(), glogin.class);
                        startActivity(mIntent);
                        finish();
                    }
                });

    }

    public void getKeyHash() {
        byte sha1[] = {(byte) 0xAC, (byte) 0x83, (byte) 0x07, 0x6E, 0x55, 0x5B, (byte) 0x8D, (byte) 0x95, 0x03, 0x34, 0x42, (byte) 0xCC, (byte) 0x8B, (byte) 0x25, (byte) 0xF4, (byte) 0x89, (byte) 0x8D, (byte) 0xE7, (byte) 0x00, (byte) 0xA5};
        Log.e("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));
    }

    private void verifyYoutubeUrl(final String youtube_url, final List<String> tags, final String youtube_video_user_assigned_name) {
        String youtube_id=youtube_url.substring(youtube_url.length()-11,youtube_url.length());
        Log.v(TAG,"Youtube_id "+youtube_id);
        final boolean[] flagArray = {false};
        String json_request="https://www.youtube.com/oembed?format=json&url=https://www.youtube.com/watch?v="+youtube_id;
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, json_request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if(!response.toLowerCase().equals(("Not found").toLowerCase())){
                            Log.v(TAG, "Video Valid");
                            setFlagTrue(youtube_url,tags,youtube_video_user_assigned_name);
                        }
                        else {
                            Log.v(TAG, "Invalid video");
                            setFlagFalse();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(TAG, "Volley Error " + error);
                setFlagFalse();
            }
        });


// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
        private void setFlagTrue(String youtube_url,List<String> tags, String youtube_user_assigned_name){
            Toast.makeText(NavDraw.this,"Video Uploading",Toast.LENGTH_LONG).show();
            addVideoToRealTimeDB(youtube_url,tags,youtube_user_assigned_name);
        }

        private void setFlagFalse(){
        Toast.makeText(NavDraw.this,"This video doesn't exist",Toast.LENGTH_LONG).show();

        }

        private void addVideoToRealTimeDB(String youtube_url,List<String> tags, String youtube_user_assigned_name){
            CameraActivity cameraActivity = new CameraActivity();

        //Log.v(TAG,"Video is valid"+getUserUID())
//        final StorageReference mVideoRef = storageReference.child("videos/" + getUserUID() + "uniqueTimeStamp" + cameraActivity.getUnixTimeStamp() + "Name-" + youtube_user_assigned_name + "-" + "Creator-" + getUserName() + "-Tags-" + tags);  //Current time stamp in UTC
        Log.v(TAG,"Video Name is"+youtube_user_assigned_name);
        Log.v(TAG,"Storage ref");
        Log.v(TAG,"URI of video"+youtube_url);
        Log.v(TAG,"CreatorName"+getUserName());
        Log.v(TAG,"Time Stamp"+cameraActivity.getUnixTimeStamp());
        Log.v(TAG,"No desc");
        Log.v(TAG,"Video TAGS"+tags);
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();
            //senddataToRealTimeDataBase(youtube_user_assigned_name,tags,storageReference,getUserName(),cameraActivity.getUnixTimeStamp(),"Youtube video");
       // cameraActivity.senddataToRealTimeDataBase(youtube_user_assigned_name,tags,storageReference,getUserName(),cameraActivity.getUnixTimeStamp(),"Youtube video");
            try {
                VideoDetailsClass videoName=new VideoDetailsClass(youtube_user_assigned_name,tags,youtube_url,getUserName(),"Youtube");
                mDatabaseReference.child("VikifyDatabase").child("Video-details").child("Video By "+getUserName()+" At "+cameraActivity.getUnixTimeStamp()+" CreatorUID"+getUserUID()).setValue(videoName);
            }
            catch (Exception e){
            Log.v(TAG,"Exception "+e);
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
            mDatabase.child("VikifyDatabase").child("Video-details").child("Video By "+creatorName+" At "+timeStamp+" CreatorUID"+getUserUID()).setValue(videoName);
        }
        catch (Exception e){

        }

    }
}