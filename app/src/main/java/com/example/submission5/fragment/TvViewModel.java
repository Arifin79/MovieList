package com.example.submission5.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.submission5.BuildConfig;
import com.example.submission5.model.tv.ResponseTvShows;
import com.example.submission5.model.tv.TvItem;
import com.example.submission5.network.ApiClient;
import com.example.submission5.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;;
    private MutableLiveData<List<TvItem>> listTvs = new MutableLiveData<>();

    MutableLiveData<List<TvItem>> getListTv() {
        return listTvs;
    }

    void setListTv() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseTvShows> tvCall = apiInterface.getTvShow(API_KEY);
        tvCall.enqueue(new Callback<ResponseTvShows>() {
            @Override
            public void onResponse(@NonNull Call<ResponseTvShows> call, @NonNull Response<ResponseTvShows> response) {
                if (response.body() != null) {
                    listTvs.postValue(response.body().getResults());
                    Log.d("onResponseTv ", response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseTvShows> call, @NonNull Throwable t) {
                Log.d("onFailureTv ", t.getMessage());
            }
        });
    }

    void setListSearchTv(String name) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseTvShows> tvCall = apiInterface.getSearchTvShow(API_KEY, name);
        tvCall.enqueue(new Callback<ResponseTvShows>() {
            @Override
            public void onResponse(@NonNull Call<ResponseTvShows> call, @NonNull Response<ResponseTvShows> response) {
                if (response.body() != null) {
                    listTvs.postValue(response.body().getResults());
                    Log.d("onResponseSearchTv ", response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseTvShows> call, @NonNull Throwable t) {
                Log.d("onFailureSearchTv ", t.getMessage());
            }
        });
    }
}
