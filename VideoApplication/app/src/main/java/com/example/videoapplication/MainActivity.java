package com.example.videoapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        videoView = findViewById(R.id.videoView);
        String video_path = "android.resource://"+getPackageName()+"/raw/sanjog";

        Uri videoURI = Uri.parse(video_path);
        videoView.setVideoURI(videoURI);
        videoView.start();
//
////        videoView.setVideoPath(video_path);
////        String online_video_path = "https://www.youtube.com/watch?v=o9DhvbqYzns&list=RDCLAK5uy_ksEjgm3H_7zOJ_RHzRjN1wY-_FFcs7aAU";
////        Uri online_videoURI = Uri.parse(online_video_path);
////        videoView.setVideoURI(online_videoURI);
////        videoView.start();

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }
}