package com.example.submission5.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.submission5.model.movie.MoviesItem;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static android.provider.MediaStore.MediaColumns.TITLE;
import static com.example.submission5.db.DatabaseContract.MovieColumns.DATE;
import static com.example.submission5.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.submission5.db.DatabaseContract.MovieColumns.POSTER;
import static com.example.submission5.db.DatabaseContract.TABLE_MOVIE;

public class MoviesHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DataBaseHelper dataBaseHelper;
    private static MoviesHelper INSTANCE;
    private static SQLiteDatabase database;

    private MoviesHelper(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
    }

    public static MoviesHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null) {
                    INSTANCE = new MoviesHelper(context);
                }
            }
        }

        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<MoviesItem> getAllMovies() {
        ArrayList<MoviesItem> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        MoviesItem note;
        if (cursor.getCount() > 0) {
            do {
                note = new MoviesItem();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                note.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                note.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public MoviesItem getMovieById(int id) {
        Cursor cursor = database.query(
                DATABASE_TABLE,
                new String[]{_ID, TITLE,OVERVIEW, DATE, POSTER},
                _ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);

        MoviesItem movieItem = new MoviesItem();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            movieItem.setId(cursor.getColumnIndex(_ID));
            movieItem.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            movieItem.setReleaseDate(cursor.getString(cursor.getColumnIndex(DATE)));
            movieItem.setPosterPath(cursor.getString(cursor.getColumnIndex(POSTER)));

            cursor.close();
            return movieItem;
        }

        return null;
    }

    public long insertMovie(MoviesItem movie) {
        ContentValues args = new ContentValues();
        args.put(_ID, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(OVERVIEW, movie.getOverview());
        args.put(DATE, movie.getReleaseDate());
        args.put(POSTER, movie.getPosterPath());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovie(int id) {
        return database.delete(DATABASE_TABLE, _ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
        , _ID + " = ?"
        , new String[]{id}
        , null
        , null
        , null
        , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + "=?", new String[]{id});
    }
}

