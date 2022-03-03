package com.sg.birfilm.tabs.movies;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sg.birfilm.R;
import com.sg.birfilm.movie.adapters.MovieRecyclerView;
import com.sg.birfilm.movie.adapters.OnMovieListener;
import com.sg.birfilm.movie.models.Movie;
import com.sg.birfilm.movie.request.Servicey;
import com.sg.birfilm.movie.response.MovieSearchResponse;
import com.sg.birfilm.movie.utils.Credentials;
import com.sg.birfilm.movie.utils.MovieApi;
import com.sg.birfilm.movie.viewmodels.MovieListViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieFragment extends Fragment implements OnMovieListener {

    private RecyclerView recyclerView;

    private MovieListViewModel movieListViewModel;
    private MovieRecyclerView movieRecyclerAdapter;

    private EditText searchBar;

    public SearchMovieFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_movie, container, false);

        searchBar = (EditText) view.findViewById(R.id.edit_search_bar);
        listSearchBar();

        recyclerView = view.findViewById(R.id.recyclerview);
        movieListViewModel = new ViewModelProvider(getActivity()).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        ObserveAnyChange();
        ObservePopularMovies();

        movieListViewModel.searchMoviePop(1);

        return view;
    }

    private void listSearchBar(){
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchBar.getText().toString().length()==0)
                    movieListViewModel.searchMoviePop(1);
                movieListViewModel.searchMovieApi(searchBar.getText().toString(),1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // 5 --> initializing recyclerView & adding data to it
    private void ConfigureRecyclerView(){
        // live data ctor'a parametre olarak geçilemez
        movieRecyclerAdapter = new MovieRecyclerView(this);

        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // recycler pagination --eksik
        // loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    // here we need to display the next search results on the next page of api
                    movieListViewModel.searchNextPage();
                }
            }
        });

    }

    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(getViewLifecycleOwner(),new Observer<List<Movie>>(){
            @Override
            public void onChanged(List<Movie> movie2s) {
                if(movie2s != null){
                    for(Movie movie:movie2s){
                        movieRecyclerAdapter.setmMovies(movie2s);
                    }
                }
            }
        });
    }

    private void ObservePopularMovies(){
        movieListViewModel.getPopularMovies().observe(getViewLifecycleOwner(),new Observer<List<Movie>>(){
            @Override
            public void onChanged(List<Movie> movie2s) {
                if(movie2s != null){
                    for(Movie movie:movie2s){
                        movieRecyclerAdapter.setmMovies(movie2s);
                    }
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie",movieRecyclerAdapter.getSelectedMovie(position));
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.search_movie_layout,fragment).addToBackStack(null).commit();
    }


//    private void GetRetrofitResponse(){
//        MovieApi movieApi = Servicey.getMovieApi();
//
//        Call<MovieSearchResponse> responseCall = movieApi
//                .getPopularMovies(
//                        Credentials.API_KEY,
//                        1
//                );
//        responseCall.enqueue(new Callback<MovieSearchResponse>() {
//            @Override
//            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
//                if(response.code()==200){ //ok?
//                    List<Movie> movies = new ArrayList<>(response.body().getMovies());
//                    movieRecyclerAdapter.setmMovies(movies);
//                }
//                else{
//                    try{
//                        Log.v("Tag","Error "+response.errorBody().string());
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
//
//            }
//        });
//    }

}
