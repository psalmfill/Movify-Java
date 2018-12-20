package com.example.samfield.movify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samfield.movify.Config;
import com.example.samfield.movify.Models.Movie;
import com.example.samfield.movify.Models.VideoResponse;
import com.example.samfield.movify.R;
import com.example.samfield.movify.VideoActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

public class VideosThumbAdapter extends RecyclerView.Adapter<VideosThumbAdapter.VideoThumbHolder> {
    Context mContext;
    Movie.Videos mVideos;

    public VideosThumbAdapter(Context context, Movie.Videos videos) {
        mContext = context;
        mVideos = videos;
    }

    @NonNull
    @Override
    public VideoThumbHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_thumnail_fragment,viewGroup,false);

        return new VideoThumbHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoThumbHolder videoThumbHolder, final int i) {
         final int pos = i;
        videoThumbHolder.mVideoThumb.initialize(Config.YOUTUBE_API_KEY,new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(mVideos.getResults().get(pos).getKey());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }
            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mVideos !=null?mVideos.getResults().size():0;
    }

    class VideoThumbHolder extends RecyclerView.ViewHolder implements YouTubeThumbnailView.OnClickListener{
        YouTubeThumbnailView mVideoThumb;
        public VideoThumbHolder(@NonNull View itemView) {
            super(itemView);
            mVideoThumb = itemView.findViewById(R.id.video_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,VideoActivity.class);
            intent.putExtra(VideoActivity.YOUTUBE_MOVIE_ID,mVideos.getResults().get(getAdapterPosition()).getKey());
            mContext.startActivity(intent);
        }
    }
}
