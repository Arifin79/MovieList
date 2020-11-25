package com.example.submission5.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.submission5.R;
import com.example.submission5.activity.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment {


    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPagerAdapter viewPagerAdapter = new  ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new FavMoviesFragment(), getResources().getString(R.string.movie));
        viewPagerAdapter.addFragment(new FavTvShowsFragment(), getResources().getString(R.string.tv));

        ViewPager viewPager = view.findViewById(R.id.pager_favorite);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabs = view.findViewById(R.id.tab_favorite);
        tabs.setupWithViewPager(viewPager);
    }

}
