package com.example.submission5.network;

import com.example.submission5.model.movie.ResponseMovie;
import com.example.submission5.model.tv.ResponseTvShows;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("discover/movie")
    Call<ResponseMovie> getMovies(@Query("api_key") String apikey);

    @GET("discover/tv")
    Call<ResponseTvShows> getTvShow(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<ResponseMovie> getSearchMovies(@Query("api_key") String apiKey,
                                        @Query("query") String movieName);

    @GET("search/tv")
    Call<ResponseTvShows> getSearchTvShow(@Query("api_key") String apiKey,
                                     @Query("query") String tvName);

    @GET("discover/movie")
    Call<ResponseMovie> getReleaseMovie(@Query("api_key") String apiKey,
                                        @Query("primary_release_date.gte") String todayGte,
                                        @Query("primary_release_date.lte") String todayLte);
}
