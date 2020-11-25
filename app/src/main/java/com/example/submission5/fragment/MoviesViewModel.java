package com.example.submission5.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.submission5.BuildConfig;
import com.example.submission5.model.movie.MoviesItem;
import com.example.submission5.model.movie.ResponseMovie;
import com.example.submission5.network.ApiClient;
import com.example.submission5.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<List<MoviesItem>> listMovies = new MutableLiveData<>();

    public MutableLiveData<List<MoviesItem>> getListMovies() {
        return listMovies;
    }

    void setListMovies() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMovie> movieCall = apiInterface.getMovies(API_KEY);
        movieCall.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                if (response.body() != null) {
                    listMovies.postValue(response.body().getResults());
                    Log.d("onResponseMovie ", response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                Log.d("onFailureMovie ", t.getMessage());
            }
        });
    }

    void setListSearchMovies(String name) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMovie> movieCall = apiInterface.getSearchMovies(API_KEY, name);
        movieCall.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                if (response.body() != null) {
                    listMovies.postValue(response.body().getResults());
                    Log.d("onResponseSearchMovie ", response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                Log.d("onFailureSearchMovie ", t.getMessage());
            }
        });
    }
}
