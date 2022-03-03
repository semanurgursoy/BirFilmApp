package com.sg.birfilm.movie.utils;

import com.sg.birfilm.movie.models.Movie;
import com.sg.birfilm.movie.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key")String key,
            @Query("query")String query,
            @Query("page")int page
    );

    @GET("/3/movie/{id}")
    Call<Movie> getMovie(
        @Path("id")int id,
        @Query("api_key")String key
    );

    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("api_key")String key,
            @Query("page")int page
    );

//    @GET("/3/movie/{category}")
//    Call<MovieSearchResponse> searchMovie(
//            @Path("category")String category,
//            @Query("api_key")String key,
//            @Query("query")String query,
//            @Query("page")int page
//    );

}
