package com.bannuranurag.android.vikify;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NavDraw extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecycleViewVertical;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    List<String> mFinalName = new ArrayList<>();
    List<String> mName= new ArrayList<>();
    private List<HorizontalClass> yearList= new ArrayList<>();
    private List<String> tags= new ArrayList<>();
    private List<String> finalTags=new ArrayList<>();
    String valueName,valueTags;

    private static final String TAG="State";
//    private String myDataset[]={"Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW","Hello","What","NOW"};
    AlertDialog.Builder builder;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;
    TextView mTextViewName,mTextViewEmail;
    ImageView mImageView;
    FirebaseUser user;
    @Override
    protected void onResume() {
        Log.v(TAG,"OnResume");
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        Log.v(TAG,"ondestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.v(TAG,"Onstop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.v(TAG,"OnStart");
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_draw);
        Log.v(TAG,"OnCreate");




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
        View hView =  navigationView.getHeaderView(0);
        mTextViewName = (TextView)hView.findViewById(R.id.header_user_name);
        mTextViewEmail=hView.findViewById(R.id.header_user_email);
        mImageView=hView.findViewById(R.id.header_image_view);
        navigationView.setNavigationItemSelectedListener(this);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext()); //Get all user details
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            mTextViewName.setText(personName);
            mTextViewEmail.setText(personEmail);


            try {
                URL mUrl=new URL(personPhoto.toString());
                GlideApp
                        .with(getApplicationContext())
                        .load(mUrl)
                        .override(130, 130)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageView);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TagClass tagClass= new TagClass();
                Log.v(TAG,"Data89"+dataSnapshot);
                long mNameSize=dataSnapshot.child("VikifyDatabase").child("Content-details").getChildrenCount();

                for(long i=1;i<=mNameSize;i++) {
                    String iString=Long.toString(i);
                    long mTagSize=dataSnapshot.child("VikifyDatabase").child("Content-details").child("Category"+iString).child("Tags").getChildrenCount();
                    valueName = dataSnapshot.child("VikifyDatabase").child("Content-details").child("Category"+iString).child("Name").getValue().toString();
                    valueName=valueName.replaceAll("[\\[\\]]"," ");
                    mName.add(valueName);
                    for(long j=0;j<mTagSize;j++) {

                        String jString=Long.toString(j);
                        valueTags = dataSnapshot.child("VikifyDatabase").child("Content-details").child("Category" + iString).child("Tags").child(jString).getValue().toString();
                        valueTags=valueTags.replaceAll("[\\{\\}]"," ");
                        finalTags.add(valueTags);

                    }

                    tags=finalTags;
                    Log.v(TAG," Mtags "+finalTags);
                    tagClass.setmTags(tags);
                    mFinalName=mName;
                }
                Log.v(TAG,"Mname12:"+mFinalName);


                mRecycleViewVertical=findViewById(R.id.verticle_recycle);



                mRecycleViewVertical.setHasFixedSize(true);



                //Linear layout manager
                mLayoutManager=new LinearLayoutManager(getApplicationContext());



                mRecycleViewVertical.setLayoutManager(mLayoutManager);



                mAdapter=new verticalAdapter(getApplicationContext(), mFinalName,yearList,finalTags);

                Log.v(TAG,"hello100"+mFinalName);


                mRecycleViewVertical.setAdapter(mAdapter);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(TAG,"Value69Error");

            }

        });


        prepareYearData();


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goToVideoList) {
            Intent mIntent = new Intent(getApplicationContext(),CameraActivity.class);
            FirebaseUser camUser = FirebaseAuth.getInstance().getCurrentUser();
            String camuid=camUser.getUid();
            mIntent.putExtra("CreatorUID",camuid);//Get the user UID while recording
            Log.v("CAMERA79","CAM"+camuid);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if(id== R.id.sign_out){
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
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // ...Toas
                        FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();
                        Log.v("CURRENTUSER","USER"+user1.getUid());
                        Toast.makeText(getApplicationContext(),"SignedOut",Toast.LENGTH_SHORT).show();
                        Intent mIntent=new Intent(getApplicationContext(),glogin.class);
                        startActivity(mIntent);
                        finish();
                    }
                });
    }




    public void prepareYearData(){
        HorizontalClass hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);
        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

        hClass=new HorizontalClass("Creator");
        yearList.add(hClass);

    }

}
