package com.vikify.android.mobileapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.vikify.android.mobileapp.DataSaving.DBEntityClass;
import com.vikify.android.mobileapp.DataSaving.SavedVideoAdapter;
import com.vikify.android.mobileapp.DataSaving.VideoViewModel;

import java.util.List;

public class SavedVideos extends AppCompatActivity {
    private static final String TAG = "SavedVideos";
    private VideoViewModel mVideoViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_videos);
        Log.v(TAG,"Saved Videos activity created");

        final SavedVideoAdapter videoAdapter = new SavedVideoAdapter(SavedVideos.this);
      final  RecyclerView recyclerView = findViewById(R.id.saved_video_recycler);
       // final SavedVideoAdapter videoAdapter = new SavedVideoAdapter(this);


        mVideoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);

        mVideoViewModel.getVideos().observe(this, new Observer<List<DBEntityClass>>() {
            @Override
            public void onChanged(final List<DBEntityClass> videos) {
                // Update the cached copy of the words in the adapter.
                videoAdapter.setVideo(videos);
                Log.v(TAG,"Saved video size"+videos.size());
            }
        });
        recyclerView.setAdapter(videoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SavedVideos.this));



    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            DBEntityClass video = new DBEntityClass(data.getStringExtra(NavDraw.EXTRA_REPLY));
            mVideoViewModel.insert(video);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Empty",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
