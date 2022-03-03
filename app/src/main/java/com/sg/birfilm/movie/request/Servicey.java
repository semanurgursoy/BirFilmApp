package com.sg.birfilm.movie.request;

import com.sg.birfilm.movie.utils.Credentials;
import com.sg.birfilm.movie.utils.MovieApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// calls for the retrofit api-with singleton pattern
public class Servicey {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Credentials.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
            // gson kütüphanesini retrofit ile yapılandırır ve bağlar -configure and bind

    private static Retrofit retrofit = retrofitBuilder.build();

    private static MovieApi movieApi = retrofit.create(MovieApi.class);

    public static MovieApi getMovieApi(){ return movieApi; }
}
