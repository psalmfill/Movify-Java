package com.example.samfield.movify;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.samfield.movify.Adapters.VideosThumbAdapter;
import com.example.samfield.movify.Client.MovieClient;
import com.example.samfield.movify.Models.Movie;
import com.example.samfield.movify.Models.VideoResponse;
import com.example.samfield.movify.Services.MovieService;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_ID= "movie_id";
    private int mMovie_id;
    private TextView  mTitleTv,mOverview,mLanguage,mTagline;
    private ImageView mBackdrop,mPoster;
    private Button mWatch;
    private RatingBar mRating;
    private ProgressBar mProgressBar;
    private ConstraintLayout mConstraintLayout;
    private RecyclerView mVideoThumbRv;
    Movie mMovie;
    private VideosThumbAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mConstraintLayout = findViewById(R.id.detail_cl);
        mTitleTv = findViewById(R.id.movie_title);
        mLanguage = findViewById(R.id.language);
        mTagline = findViewById(R.id.movie_tagline);
        mOverview = findViewById(R.id.movie_overview);
        mBackdrop = findViewById(R.id.backdrop);
        mPoster = findViewById(R.id.movie_poster);
        mRating = findViewById(R.id.movie_rating);
        mProgressBar = findViewById(R.id.progress_loading);
        mWatch = findViewById(R.id.watch);
        mVideoThumbRv = findViewById(R.id.video_thumbnail_rv);
        mWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieService service = MovieClient.getClient().create(MovieService.class);
                Call<VideoResponse> videoResponseCall = service.getVideo(mMovie_id,Config.API_KEY);
                videoResponseCall.enqueue(new Callback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        List<VideoResponse.Video> mVideos = response.body().getVideos();
                        Intent intent = new Intent(MovieDetailActivity.this,VideoActivity.class);
                        if(mVideos.size() >0){
                            intent.putExtra(VideoActivity.YOUTUBE_MOVIE_ID,mVideos.get(0).getKey());
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<VideoResponse> call, Throwable t) {

                    }
                });

            }
        });
        Intent intent = getIntent();
        if(intent !=null){
            mMovie_id = intent.getIntExtra(MOVIE_ID,0);
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mConstraintLayout.setVisibility(View.GONE);
        MovieService service = MovieClient.getClient().create(MovieService.class);
        Call<Movie> movieCall = service.getDetail(mMovie_id,Config.API_KEY);
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                mMovie = response.body();
                mTitleTv.setText(mMovie.getTitle());
                mOverview.setText(mMovie.getOverview());
                mTagline.setText(mMovie.getTagline());
                mAdapter = new VideosThumbAdapter(MovieDetailActivity.this,mMovie.getVideos());
                mVideoThumbRv.setAdapter(mAdapter);
                if(mMovie.getVoteAverage() != null){
                    mRating.setRating((float) (mMovie.getVoteAverage()/2));
                }else{
                    mRating.setRating(0);
                }

                if(mMovie.getSpokenLanguages().size() >0){
                    StringBuilder lang = new StringBuilder();
                    for(Movie.SpokenLanguage language:mMovie.getSpokenLanguages()){
                        lang.append(" ");
                        lang.append(language.getName());
                    }
                    mLanguage.setText(lang.toString());

                }
                mProgressBar.setVisibility(View.GONE);
                mConstraintLayout.setVisibility(View.VISIBLE);
                Glide.with(MovieDetailActivity.this).load(Config.BASE_IMAGE_URL_W500 + mMovie.getBackdropPath())
                        .into(mBackdrop);
                Glide.with(MovieDetailActivity.this).load(Config.BASE_IMAGE_URL + mMovie.getPosterPath())
                        .into(mPoster);

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater .inflate(R.menu.action_menu,menu);

        //Associate search config
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

}
