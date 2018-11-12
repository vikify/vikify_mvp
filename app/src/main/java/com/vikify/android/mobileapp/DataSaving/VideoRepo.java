package com.vikify.android.mobileapp.DataSaving;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class VideoRepo {
    private static final String TAG = "VideoRepo";
    private DBDao dao;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    String uid;
    private LiveData<List<DBEntityClass>> mAllVideos;

    //get the user


    VideoRepo(Application application) {
        SavedVideosDB db = SavedVideosDB.getDatabase(application);
        dao = db.daoAccess();
        try {
            mAllVideos =  dao.getVideos(FirebaseAuth.getInstance().getUid());
           // Log.v(TAG,"Current user is: "+currentUserUID);
        }
        catch (NullPointerException e){
            Log.v(TAG,"No user "+e);
        }

  //      Log.v(TAG,"All videos are:"+mAllVideos.getValue()+count);
    }

    public LiveData<List<DBEntityClass>> getmAllVideos() {
        return mAllVideos;
    }

    public int getCount(){     return dao.countVideos();}


    public void insertOnlySingleVideo(DBEntityClass video) {
        new insertAsyncTask(dao).execute(video);
    }
    public void deleteWord(DBEntityClass video)  {
        new deletevideo(dao).execute(video);
    }


    private static class deletevideo extends AsyncTask<DBEntityClass, Void, Void> {
        private DBDao mAsyncTaskDao;

        deletevideo(DBDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DBEntityClass... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<DBEntityClass, Void, Void> {

        private DBDao mAsyncTaskDao;

        insertAsyncTask(DBDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DBEntityClass... params) {
            mAsyncTaskDao.insertOnlySingleVideo(params[0]);
            return null;
        }
    }


    public void getUser(){
        try {

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                    user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                 Log.v(TAG,"Current user is:"+uid);
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
}
