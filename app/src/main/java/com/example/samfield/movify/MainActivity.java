package com.example.samfield.movify;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.samfield.movify.Adapters.MovieRecyclerAdapter;
import com.example.samfield.movify.Client.MovieClient;
import com.example.samfield.movify.Models.ResponseData;
import com.example.samfield.movify.Services.MovieService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView mPopularRv,mTopRated,mUpcoming;
    ProgressBar mProgressBar;
    TextView mAllPopular, mAllTopRated, mAllUpcoming;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPopularRv = findViewById(R.id.popular_rv);
        mTopRated = findViewById(R.id.toprated_rv);
        mUpcoming = findViewById(R.id.upcoming_rv);
        mProgressBar = findViewById(R.id.loading);
        mProgressBar.setVisibility(View.VISIBLE);
        mAllPopular = findViewById(R.id.all_popular);
        mAllTopRated = findViewById(R.id.all_top_rated);
        mAllUpcoming = findViewById(R.id.all_upcoming);
        mAllPopular.setOnClickListener(this);
        mAllTopRated.setOnClickListener(this);
        mAllUpcoming.setOnClickListener(this);

         MovieService service = MovieClient.getClient().create(MovieService.class);
        Call<ResponseData> popularMovies = service.getPopularMovies(Config.API_KEY,1);
        Call<ResponseData> topRatedmovies = service.getTopRatedMovies(Config.API_KEY,1);
        Call<ResponseData> upcomingMovies = service.getUpcomingMovies(Config.API_KEY,1);

       popularMovies.enqueue(new Callback<ResponseData>() {
           @Override
           public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
               Log.e("mydata",response.body().getMovies().toString());
               MovieRecyclerAdapter adapter = new MovieRecyclerAdapter(MainActivity.this,response.body().getMovies(),"popular");
               mPopularRv.setAdapter(adapter);
               mProgressBar.setVisibility(View.GONE);
           }

           @Override
           public void onFailure(Call<ResponseData> call, Throwable t) {
               Log.e("mydataerror",t.toString());

           }
       });

       topRatedmovies.enqueue(new Callback<ResponseData>() {
           @Override
           public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

               MovieRecyclerAdapter adapter = new MovieRecyclerAdapter(MainActivity.this,response.body().getMovies(),"top_rated");
               mTopRated.setAdapter(adapter);
           }

           @Override
           public void onFailure(Call<ResponseData> call, Throwable t) {

           }
       });
       upcomingMovies.enqueue(new Callback<ResponseData>() {
           @Override
           public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

               MovieRecyclerAdapter adapter = new MovieRecyclerAdapter(MainActivity.this,response.body().getMovies(),"upcoming");
               mUpcoming.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        switch (id){
            case R.id.all_popular:
                loadMore("popular");
                break;
            case R.id.all_top_rated:
                loadMore("top_rated");
                break;
            case R.id.all_upcoming:
                loadMore("upcoming");
                break;
        }
    }

    public  void loadMore(String pos){
        Intent intent = new Intent(this,MovieListingActivity.class);
        intent.putExtra(MovieListingActivity.MOVIE_CATEGORY,pos);
        startActivity(intent);
    }
}
