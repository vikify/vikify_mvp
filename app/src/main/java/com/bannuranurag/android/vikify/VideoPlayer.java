package com.bannuranurag.android.vikify;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {
    VideoView mVideoView;
    ImageView mcancelVector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        String url = "https://www.youtube.com/watch?v=QK8mJJJvaes"; // your URL here
        Uri uri= Uri.parse(url);
        mVideoView = findViewById(R.id.video_player);
        mVideoView.setVideoURI(uri);
        mVideoView.start();
    }

}
