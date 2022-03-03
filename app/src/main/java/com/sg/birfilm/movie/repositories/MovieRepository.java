package com.sg.birfilm.movie.repositories;

import androidx.lifecycle.LiveData;

import com.sg.birfilm.movie.models.Movie;
import com.sg.birfilm.movie.request.MovieApiClient;

import java.util.List;

public class MovieRepository {


    private static MovieRepository instance; // -->> singleton

    private MovieApiClient movieApiClient;

    private String mQuery;
    private  int mPageNumber;

    public static MovieRepository getInstance() {
        if (instance == null)
            instance = new MovieRepository();
        return instance;
    }

    private MovieRepository() {
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<Movie>> getMovies() {
        return movieApiClient.getMovies();
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return movieApiClient.getMoviesPop();
    }


// 2-->> calling the method in repository
    public void searchMovieApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(query,pageNumber);
    }

    public void searchMoviePop(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesPop(pageNumber);
    }

    public void searchNextPage(){
        searchMovieApi(mQuery,mPageNumber+1);
    }


}