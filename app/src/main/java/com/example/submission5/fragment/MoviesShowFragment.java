package com.example.submission5.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.submission5.R;
import com.example.submission5.activity.DetailActivity;
import com.example.submission5.adapter.MoviesAdapter;
import com.example.submission5.model.movie.MoviesItem;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesShowFragment extends Fragment {
    private MoviesAdapter movieAdapter;
    private RecyclerView rvMovie;
    private ProgressBar pgMovie;
    private MoviesViewModel movieViewModel;


    public MoviesShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return getView() != null ? getView() :
                inflater.inflate(R.layout.fragment_movies_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieViewModel = ViewModelProviders.of(MoviesShowFragment.this)
                .get(MoviesViewModel.class);
        movieViewModel.getListMovies().observe(this, getMovie);

        rvMovie = view.findViewById(R.id.rv_movie);
        pgMovie = view.findViewById(R.id.pg_movie);


        movieAdapter = new MoviesAdapter(getActivity());
        movieAdapter.notifyDataSetChanged();

        movieViewModel.setListMovies();
        showLoading(true);
        showRecyclerList();
        setHasOptionsMenu(true);
    }

    private Observer<List<MoviesItem>> getMovie = new Observer<List<MoviesItem>>() {
        @Override
        public void onChanged(@Nullable List<MoviesItem> movieItems) {
            if (movieItems != null) {
                movieAdapter.setListData(movieItems);
                showLoading(false);
            }
        }
    };

    private void showRecyclerList() {
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickCallback(new MoviesAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(MoviesItem data) {
                showSelectedMovie(data);
            }
        });
    }

    private void showSelectedMovie(MoviesItem movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_DETAIL_DATA, movie);
        intent.putExtra(DetailActivity.KEY_JENIS_DATA, "movie");
        startActivity(intent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            pgMovie.setVisibility(View.VISIBLE);
        } else {
            pgMovie.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_movie, menu);
        final MenuItem searchItem = menu.findItem(R.id.nav_search_movie);

        final SearchView searchView = (SearchView) menu.findItem(R.id.nav_search_movie).getActionView();
        searchView.setQueryHint(getString(R.string.search_maches));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                /*Intent intent = new Intent(getActivity(), SearchMovie.class);
                intent.putExtra("query", s);*/
                movieViewModel.setListSearchMovies(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchItem.collapseActionView();
                    movieViewModel.setListMovies();
                }
            }
        });
    }

}
