package com.sg.birfilm.movie.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sg.birfilm.movie.models.Movie;
import com.sg.birfilm.movie.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;

    public MovieListViewModel() { movieRepository = MovieRepository.getInstance(); }

    public LiveData<List<Movie>> getMovies(){ return movieRepository.getMovies(); }

    public LiveData<List<Movie>> getPopularMovies(){ return movieRepository.getPopularMovies(); }

    // 3 -->> calling method in view-model
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query,pageNumber);
    }

    public void searchMoviePop(int pageNumber){
        movieRepository.searchMoviePop(pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }

}
