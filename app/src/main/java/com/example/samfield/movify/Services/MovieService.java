package com.example.samfield.movify.Services;

import com.example.samfield.movify.Config;
import com.example.samfield.movify.Models.Movie;
import com.example.samfield.movify.Models.ResponseData;
import com.example.samfield.movify.Models.VideoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET(Config.POPULAR_PATH)
    Call<ResponseData> getPopularMovies(@Query("api_key")String api_key,
                                       @Query("page") int page);
    @GET(Config.TOP_RATED_PATH)
    Call<ResponseData> getTopRatedMovies(@Query("api_key") String api_key,@Query("page") int page );

    @GET(Config.UPCOMING_PATH)
    Call<ResponseData> getUpcomingMovies(@Query("api_key")String api_key, @Query("page") int page);

    @GET(Config.MOVIE_DETAIL_PATH)
    Call<Movie> getDetail( @Path("movie_id") int movie_id,@Query("api_key")String api_key);

    @GET(Config.MOVIE_VIDEO_PATH)
    Call<VideoResponse> getVideo(@Path("movie_id") int movie_id, @Query("api_key")String api_key);

    @GET(Config.SEARCH_PATH)
    Call<ResponseData> searchMovie(@Query("api_key") String api_key,@Query("query") String query,@Query("page") int page);
}
