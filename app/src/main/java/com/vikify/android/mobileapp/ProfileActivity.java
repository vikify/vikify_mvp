package com.vikify.android.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

;

public class ProfileActivity extends AppCompatActivity {
    TextView mTextViewName,mTextViewEmail;
    ImageView mImageView;
    FirebaseUser user;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mlayoutManager;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    List<String> names = new ArrayList<>();
    List<String> uris= new ArrayList<>();
    List<String> videonames= new ArrayList<>();
    List<List<String>> tags= new ArrayList<List<String>>();
    List<String> description = new ArrayList<>();
    String jsonChild;
    Long size;
    private static final String TAG = "ProfileActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("VikifyDatabase").child("Video-details");


        String Uid= FirebaseAuth.getInstance().getUid();


        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        recyclerView=findViewById(R.id.profile_video_section_recycler);
        mTextViewName = (TextView)findViewById(R.id.header_user_name_prof);
        mTextViewEmail=findViewById(R.id.header_user_email_prof);
        mImageView=findViewById(R.id.header_image_view_prof);
        mAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //getUserProfile(getuser());

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String CreatorUID = extras.getString("CreatorUID");
        final String CreatorName = extras.getString("CreatorName");

        try {
            mlayoutManager = new LinearLayoutManager(ProfileActivity.this);
            recyclerView.setLayoutManager(mlayoutManager);
            mAdapter = new ProfileVideosAdapter(videonames, names, uris, tags, ProfileActivity.this, description);
            recyclerView.setAdapter(mAdapter);
        }
        catch (NullPointerException e){
            Log.v(TAG,"Nullpointerexception due to description"+e);
        }




        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    jsonChild=ds.getKey();
                     size=dataSnapshot.getChildrenCount();
                    boolean condition= parse(jsonChild,CreatorUID,size);
                    if(condition){
                        try {
                            String name = dataSnapshot.child(jsonChild).child("name").getValue().toString();
                            String videoname = dataSnapshot.child(jsonChild).child("mVideoName").getValue().toString();
                            List<String> tag = Collections.singletonList(dataSnapshot.child(jsonChild).child("tags").getValue().toString());
                            String uri = dataSnapshot.child(jsonChild).child("downloadUrl").getValue().toString();
                            String mdescription = dataSnapshot.child(jsonChild).child("description").getValue().toString();
                            names.add(name);
                            videonames.add(videoname);
                            uris.add(uri);
                            tags.add(tag);
                            description.add(mdescription);
                        }
                        catch (NullPointerException e){
                            Log.v(TAG,"Nullpointer because of desc"+e);
                        }
                    }

                }
                mAdapter.notifyDataSetChanged();
                Log.v(TAG,"Final names "+names+" final uris "+uris+" Tags "+tags+" Videonames "+videonames+" Descriptions"+description);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
try {
    mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mTextViewName.setText(user.getDisplayName());
                        mTextViewEmail.setText(user.getEmail());
                        if(user.getDisplayName()==null){
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
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };
}
catch (NullPointerException e){
    Log.v(TAG,"Nullpointer"+ e);
}

    }
    public boolean parse(String jsonchild,String UID,Long size){


            String uidfromjson = jsonchild.substring(jsonchild.indexOf("UID") + 3, jsonchild.length());
            if (uidfromjson.equals(UID)) {


                return true;
            } else {


                return false;
            }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


}


