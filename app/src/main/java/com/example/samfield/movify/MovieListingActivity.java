package com.example.samfield.movify;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.samfield.movify.Adapters.MovieListingAdapter;
import com.example.samfield.movify.Adapters.MovieListingOnScrollListener;
import com.example.samfield.movify.Client.MovieClient;
import com.example.samfield.movify.Models.ResponseData;
import com.example.samfield.movify.Services.MovieService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListingActivity extends AppCompatActivity {
    public static final String MOVIE_CATEGORY = "movie_category";
    private int START_PAGE = 1;
    MovieService mMovieService = MovieClient.getClient().create(MovieService.class);
    RecyclerView mRecyclerView;
    ProgressBar mProgressBar,mLoading;
    String cat, viewType = "linear";
    LinearLayoutManager mLayoutManager;
    Call<ResponseData> res =null;
    MovieListingAdapter adapter;
    public boolean isLoading = false;
    public boolean isLastPage = false;

    public int currentPage = START_PAGE;
    public int TOTAL_PAGE = 5;
    String mQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_listing);
        mRecyclerView = findViewById(R.id.list_rv);
        mProgressBar = findViewById(R.id.load_more_bar);
        mLoading = findViewById(R.id.progress_loading);
       handleIntent(getIntent());
        if(viewType.equals("linear")){
            mLayoutManager = new LinearLayoutManager(this);
        }else {
            mLayoutManager = new GridLayoutManager(this,2);
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new MovieListingOnScrollListener(mLayoutManager) {
            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGE;
            }

            @Override
            public void loadMoreMovies() {
                currentPage++;
                isLoading = true;
                loadMoreData();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /*check and handle intent on the activity
     * @para intent
     */
    public void handleIntent(Intent intent){
        if(intent!=null){
            if(Intent.ACTION_SEARCH.equals(intent.getAction())){
                mQuery = intent.getStringExtra(SearchManager.QUERY);
                cat = "search";
            }else{
                cat= intent.getStringExtra(MOVIE_CATEGORY);

            }
            loadData();
        }
    }

    /*Load the Movies from the api base of requested Path
     * @param null
     */
    public void loadData(){

            mLoading.setVisibility(View.VISIBLE);
            if(cat.equals("popular")){
                res  = mMovieService.getPopularMovies(Config.API_KEY, currentPage);
            }else if(cat.equals("top_rated")){
                res  = mMovieService.getTopRatedMovies(Config.API_KEY, currentPage);
            }else if(cat.equals("upcoming")){
                res  = mMovieService.getUpcomingMovies(Config.API_KEY, currentPage);
            }else if(cat.equals("search")){
                res = mMovieService.searchMovie(Config.API_KEY,mQuery,currentPage);
            }

        res.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                TOTAL_PAGE = response.body().getTotalPages();
                if(response.body().getPage() == TOTAL_PAGE){
                    isLastPage = true;
                }
                adapter = new MovieListingAdapter(MovieListingActivity.this,
                        response.body().getMovies());
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    /*Load more movies base on request.
     *@param null
     */
    public void loadMoreData(){
        mProgressBar.setVisibility(View.VISIBLE);
        if(cat.equals("popular")){
            res  = mMovieService.getPopularMovies(Config.API_KEY, currentPage);
        }else if(cat.equals("top_rated")){
            res  = mMovieService.getTopRatedMovies(Config.API_KEY, currentPage);
        }else if(cat.equals("upcoming")){
            res  = mMovieService.getUpcomingMovies(Config.API_KEY, currentPage);
        }else if(cat.equals("search")){
            res = mMovieService.searchMovie(Config.API_KEY,mQuery,currentPage);
        }

        res.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                isLoading = false;
                adapter.addMovies(response.body().getMovies());
//                adapter.removeLoadingFooter();
                mProgressBar.setVisibility(View.GONE);
                if(response.body().getPage() == TOTAL_PAGE){
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

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
