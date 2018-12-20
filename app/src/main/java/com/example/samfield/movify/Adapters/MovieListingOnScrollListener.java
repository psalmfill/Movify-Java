package com.example.samfield.movify.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class MovieListingOnScrollListener extends RecyclerView.OnScrollListener {
    LinearLayoutManager mLayoutManager;

    public MovieListingOnScrollListener(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

        if(!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition ) >= totalItemCount
                    && (firstVisibleItemPosition >= 0) /*&& (totalItemCount >= getTotalPageCount())*/) {
                loadMoreMovies();
            }
        }

    }

    public abstract int getTotalPageCount();
    public abstract void loadMoreMovies();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();

}
