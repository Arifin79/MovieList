package com.example.submission5.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.submission5.db.MoviesHelper;

import java.sql.SQLException;


import static com.example.submission5.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.submission5.db.DatabaseContract.AUTHORITY;
import static com.example.submission5.db.DatabaseContract.TABLE_MOVIE;


public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MoviesHelper movieHelper;


    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);

        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE + "/#", MOVIE_ID);
    }



    @Override
    public boolean onCreate() {
        movieHelper = MoviesHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            movieHelper.open();
        }catch (SQLException e){
            e.printStackTrace();
        }

        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
                default:
                    deleted = 0;
                    break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                updated = movieHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        }

        return updated;
    }
}
