package com.example.submission5.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.example.submission5.R;
import com.example.submission5.fragment.FavFragment;
import com.example.submission5.fragment.MoviesShowFragment;
import com.example.submission5.fragment.TVShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment pageContent = new MoviesShowFragment();
    public static final String KEY_FRAGMENT = "fragment";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    pageContent = new MoviesShowFragment();
                    openFragment(pageContent);
                    return true;
                case R.id.navigation_tv:
                    pageContent = new TVShowFragment();
                    openFragment(pageContent);
                    return true;
                case R.id.navigation_fav:
                    pageContent = new FavFragment();
                    openFragment(pageContent);
                    return true;
            }

            return false;

        }
    };

    void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        if (savedInstanceState == null) {
            openFragment(pageContent);
        } else {
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState,KEY_FRAGMENT);
            openFragment(pageContent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT, pageContent);
        super.onSaveInstanceState(outState);
    }
}
