package com.example.samfield.movify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.samfield.movify.Config;
import com.example.samfield.movify.Models.Movie;
import com.example.samfield.movify.MovieDetailActivity;
import com.example.samfield.movify.MovieListingActivity;
import com.example.samfield.movify.R;

import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Movie> mMovies;
    Context mContext;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static boolean isLoadingAdded = true;//true for now
    private  String movieCat;
    public MovieRecyclerAdapter(Context context, List<Movie> movies,String movieCat) {
        mContext = context;
        mMovies = movies;
        this.movieCat = movieCat;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i){
            case ITEM:
                view = LayoutInflater.from(mContext).inflate(R.layout.single_movie_portrait,viewGroup,false);

                return new MovieHolder(view);
            case LOADING :
                view = LayoutInflater.from(mContext).inflate(R.layout.single_movie_portrait_loading,viewGroup,false);
                return new LoadMoreHolder(view);
            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()){
            case ITEM:
                Movie movie = mMovies.get(i);
                final MovieHolder movieHolder = (MovieHolder) viewHolder;
                RequestOptions requestOp = new RequestOptions()
                        .placeholder(R.drawable.loading);
                Glide.with(mContext).load(Config.BASE_IMAGE_URL + movie.getPosterPath())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                movieHolder.mProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                movieHolder.mProgressBar.setVisibility(View.GONE);

                                return false;
                            }
                        })
                        .apply(requestOp)
                        .into(movieHolder.mPoster);
                break;
            case LOADING:
                break;

        }
    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  (position == mMovies.size()-1 && isLoadingAdded)?LOADING:ITEM;
    }

    public String getMovieCat(){
        return this.movieCat;
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView mPoster;

        ProgressBar mProgressBar;
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            mPoster = itemView.findViewById(R.id.movie_poster);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie  movie = mMovies.get(getAdapterPosition());
                    Intent intent = new Intent(mContext,MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.MOVIE_ID,movie.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class LoadMoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button mLoadmore;
        public LoadMoreHolder(@NonNull View itemView) {

            super(itemView);
            mLoadmore = itemView.findViewById(R.id.loadmore);

            mLoadmore.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.loadmore){
                Intent intent = new Intent(mContext,MovieListingActivity.class);
                String pos = MovieRecyclerAdapter.this.getMovieCat();
                intent.putExtra(MovieListingActivity.MOVIE_CATEGORY,pos);
                mContext.startActivity(intent);

            }
        }
    }
}
