package com.example.samfield.movify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.samfield.movify.Client.MovieClient;
import com.example.samfield.movify.Models.Movie;
import com.example.samfield.movify.Models.VideoResponse;
import com.example.samfield.movify.Services.MovieService;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends YouTubeBaseActivity {
    YouTubePlayerView mPlayerView;
    public static final String YOUTUBE_MOVIE_ID = "movie_id";
    List<VideoResponse.Video> mVideos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mPlayerView = findViewById(R.id.player);


        mPlayerView.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                Intent intent = getIntent();
                if(intent != null){
                    if(intent.hasExtra(YOUTUBE_MOVIE_ID)){
                        String key = intent.getStringExtra(YOUTUBE_MOVIE_ID);
                        youTubePlayer.cueVideo(key);
                        youTubePlayer.setFullscreen(true);


                    }
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("YOUtube" , youTubeInitializationResult.toString());
            }
        });
    }
}
