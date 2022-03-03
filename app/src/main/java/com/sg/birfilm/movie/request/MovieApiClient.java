package com.sg.birfilm.movie.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sg.birfilm.movie.AppExecutors;
import com.sg.birfilm.movie.models.Movie;
import com.sg.birfilm.movie.response.MovieResponse;
import com.sg.birfilm.movie.response.MovieSearchResponse;
import com.sg.birfilm.movie.utils.Credentials;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieApiClient {
    //Live Data for Search
    private MutableLiveData<List<Movie>> mMovies;

    private static MovieApiClient instance;

    // making global runnable request
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    //Live Data for Popular
    private MutableLiveData<List<Movie>> mMoviesPop;

    // making global popular runnable request
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;

    public static MovieApiClient getInstance() {
        if(instance == null)
            instance = new MovieApiClient();
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getMovies() { return mMovies; }

    public LiveData<List<Movie>> getMoviesPop() { return mMoviesPop; }

    // 1-->>this method that we are going to call through to classes
    public void searchMoviesApi(String query, int pageNumber) {
        if(retrieveMoviesRunnable != null)
            retrieveMoviesRunnable=null;

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query,pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // cancelling the retrofit call
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    public void searchMoviesPop(int pageNumber) {
        if(retrieveMoviesRunnablePop != null)
            retrieveMoviesRunnablePop=null;

        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePop);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // cancelling the retrofit call
                myHandler2.cancel(true);
            }
        }, 1000, TimeUnit.MILLISECONDS);

    }

    private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // getting the response objects
            try{
                Response response = getMovies(query,pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if(response.code() == 200){
                    List<Movie> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber == 1){
                        // sending data to live data
                        // post value : used for background thread
                        // set value : not for background thread
                        mMovies.postValue(list);
                    }else{
//                        List<Movie> currentMovies = mMovies.getValue();
//                        Log.v("Tag","mMovies : "+mMovies);
//                        Log.v("Tag","mMovies.toString() : "+mMovies.toString());
//                        Log.v("Tag","mMovies.getValue() : "+mMovies.getValue());
//                        Log.v("Tag","currentMovies : "+currentMovies);
//                        currentMovies.addAll(list);
//                        mMovies.postValue(currentMovies);

                        mMovies.postValue(list);
                        List<Movie> currentMovies = mMovies.getValue();
                    }
                }else{
                    String error = response.errorBody().string();
                    Log.v("Tag","error "+error);
                    mMovies.postValue(null);
                }

            }catch(Exception e){
                e.printStackTrace();
                mMovies.postValue(null);
            }

        }

        // search method/query
        private Call<MovieSearchResponse> getMovies(String query,int pageNumber){
            return Servicey.getMovieApi().searchMovie(
                Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }

        private void setCancelRequest(){
            Log.v("Tag","cancelling search req");
            cancelRequest=true;
        }

    }

    private class RetrieveMoviesRunnablePop implements Runnable{

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // getting the response objects
            try{
                Response response2 = getPopularMovies(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if(response2.code() == 200){
                    List<Movie> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if(pageNumber == 1){
                        // sending data to live data
                        // post value : used for background thread
                        // set value : not for background thread
                        mMoviesPop.postValue(list);
                    }else{
//                        List<Movie> currentMovies = mMovies.getValue();
//                        Log.v("Tag","mMovies : "+mMovies);
//                        Log.v("Tag","mMovies.toString() : "+mMovies.toString());
//                        Log.v("Tag","mMovies.getValue() : "+mMovies.getValue());
//                        Log.v("Tag","currentMovies : "+currentMovies);
//                        currentMovies.addAll(list);
//                        mMovies.postValue(currentMovies);

                        mMoviesPop.postValue(list);
                        List<Movie> currentMovies = mMoviesPop.getValue();
                    }
                }else{
                    String error = response2.errorBody().string();
                    Log.v("Tag","error "+error);
                    mMoviesPop.postValue(null);
                }

            }catch(Exception e){
                e.printStackTrace();
                mMovies.postValue(null);
            }

        }

        // search method/query
        private Call<MovieSearchResponse> getPopularMovies(int pageNumber){
            return Servicey.getMovieApi().getPopularMovies(
                    Credentials.API_KEY,
                    pageNumber
            );
        }

        private void setCancelRequest(){
            Log.v("Tag","cancelling search req");
            cancelRequest=true;
        }

    }


}
