package com.sg.birfilm.movie.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sg.birfilm.movie.models.Movie;

// this class is for single movie request
public class MovieResponse {
    // 1-finding the movie object
    @SerializedName("results") // retrofit'e dizinin adını iletir
    @Expose                    // implemente ettiğimiz json kütüphanesini tanımlar
    private Movie movie;

    public Movie getMovie(){ return movie; }

    @Override
    public String toString() { // results yazdırılır
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }


}
