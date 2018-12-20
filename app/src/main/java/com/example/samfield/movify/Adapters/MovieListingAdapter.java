package com.example.samfield.movify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.samfield.movify.Config;
import com.example.samfield.movify.Models.Movie;
import com.example.samfield.movify.MovieDetailActivity;
import com.example.samfield.movify.R;

import java.util.List;

public class MovieListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private static boolean linear = true;
    private List<Movie> mMovies;
    private Context mContext;
    public static boolean isLoading = false;
    public MovieListingAdapter(Context context, List<Movie> movies) {
        mContext =context;
        mMovies = movies;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i ==1 ){
            View view = LayoutInflater.from(mContext).inflate(R.layout.listing_full_length,viewGroup,false);
            return  new ListingLongHolder(view);
        }
        if(isLoading){
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_linear,viewGroup,false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int type = getItemViewType(i);
        if(type == 1){
            Movie movie = mMovies.get(i);
            ListingLongHolder holder = (ListingLongHolder) viewHolder;
            holder.mTitle.setText(movie.getTitle());
            if(movie.getBackdropPath()==null || movie.getBackdropPath().equals("")){
                holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
            holder.mYear.setText(movie.getReleaseDate());
            if(movie.getVoteAverage() != null){
                holder.mRating.setRating((float) (movie.getVoteAverage()/2));
            }else {
                holder.mRating.setRating(0);
            }

            RequestOptions requestOp = new RequestOptions()
                    .placeholder(R.drawable.loading);
            Glide.with(mContext).load(Config.BASE_IMAGE_URL + movie.getPosterPath())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(requestOp)
                    .into(holder.mPoster);
            Glide.with(mContext).load(Config.BASE_IMAGE_URL_W500 + movie.getBackdropPath())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.mBackdrop);
        }

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }



    @Override
    public int getItemViewType(int position) {
        return (position == mMovies.size() -1 && isLoading )?0:1;
    }

    public void addMovies(List<Movie>movies){
        for(Movie movie:movies){
            addMovie(movie);
            this.notifyItemInserted(mMovies.size()-1);
        }

    }
    public void addMovie(Movie movie){
        mMovies.add(movie);
    }

    public void addLoadingFooter(){
        isLoading = true;
        addMovie(new Movie());
    }

    public void removeMovie(Movie movie){
        int pos = mMovies.indexOf(movie);
        if(pos > -1){
            mMovies.remove(movie);
            notifyItemRemoved(pos);
        }
    }

    public void removeLoadingFooter(){
        int pos = mMovies.size() - 1;
        isLoading = false;
        Movie movie = mMovies.get(pos);
        if(movie !=null ){
            removeMovie(movie);
        }
    }

    class ListingLongHolder extends RecyclerView.ViewHolder{
        ImageView mPoster,mBackdrop;
        TextView mTitle,mYear;
        RatingBar mRating;
        public ListingLongHolder(@NonNull View itemView) {
            super(itemView);
            mPoster = itemView.findViewById(R.id.movie_poster);
            mBackdrop = itemView.findViewById(R.id.movie_backdrop);
            mTitle = itemView.findViewById(R.id.movie_title);
            mYear = itemView.findViewById(R.id.movie_year);
            mRating = itemView.findViewById(R.id.movie_rating);
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

    class LoadingHolder extends RecyclerView.ViewHolder{

        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
