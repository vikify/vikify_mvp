package com.bannuranurag.android.vikify;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    CameraView cameraView;
    ImageView mImageView;
    Button mButton;
    VideoView mVideoView;
    Button stopBtn;

    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraView=findViewById(R.id.camera);
        mButton=findViewById(R.id.picture);
        cameraView.setFacing(Facing.FRONT);
//        mImageView=findViewById(R.id.imageView);
        mVideoView=findViewById(R.id.videoView);
        stopBtn=findViewById(R.id.stopbtn);



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cameraView.capturePicture();
                File mfile=new File(getBaseContext().getFilesDir()+"niki.mp4");
                Log.v("TAG","mfile"+mfile);
                cameraView.startCapturingVideo(mfile);
                Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT).show();


            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.stopCapturingVideo();
                Toast.makeText(getApplicationContext(),"Stopped",Toast.LENGTH_SHORT).show();
            }
        });

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(File video) {
                // The File is the same you passed before.
                // Now it holds a MP4 video.
                Log.v("Video","Video"+video);
//

                mVideoView.setVideoURI(Uri.parse("file:///"+video.getPath()));
                mVideoView.start();
//


            }

        });

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



}
