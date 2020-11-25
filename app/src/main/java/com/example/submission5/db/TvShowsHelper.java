package com.example.submission5.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.submission5.model.tv.TvItem;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.example.submission5.db.DatabaseContract.TABLE_TV;
import static com.example.submission5.db.DatabaseContract.TvColumns.DATE;
import static com.example.submission5.db.DatabaseContract.TvColumns.OVERVIEW;
import static com.example.submission5.db.DatabaseContract.TvColumns.POSTER;
import static com.example.submission5.db.DatabaseContract.TvColumns.TITLE;

public class TvShowsHelper {
    private static final String DATABASE_TABLE = TABLE_TV;
    private static DataBaseHelper dataBaseHelper;
    private static TvShowsHelper INSTANCE;
    private static SQLiteDatabase database;

    private TvShowsHelper(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
    }

    public static TvShowsHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvShowsHelper(context);
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

    public ArrayList<TvItem> getAllTv() {
        ArrayList<TvItem> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        TvItem tvItem;
        if (cursor.getCount() > 0) {
            do {
                tvItem = new TvItem();
                tvItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                tvItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tvItem.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                tvItem.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                tvItem.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                arrayList.add(tvItem);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public TvItem getTvById(int id) {
        Cursor cursor = database.query(
                DATABASE_TABLE,
                new String[]{_ID, TITLE, OVERVIEW, DATE, POSTER},
                _ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);

        TvItem tvItem = new TvItem();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            tvItem.setId(cursor.getColumnIndex(_ID));
            tvItem.setName(cursor.getString(cursor.getColumnIndex(TITLE)));
            tvItem.setFirstAirDate(cursor.getString(cursor.getColumnIndex(DATE)));
            tvItem.setPosterPath(cursor.getString(cursor.getColumnIndex(POSTER)));

            cursor.close();
            return tvItem;
        }

        return null;
    }

    public long insertTv(TvItem tvItem) {
        ContentValues args = new ContentValues();
        args.put(_ID, tvItem.getId());
        args.put(TITLE, tvItem.getName());
        args.put(OVERVIEW, tvItem.getOverview());
        args.put(DATE, tvItem.getFirstAirDate());
        args.put(POSTER, tvItem.getPosterPath());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteTv(int id) {
        return database.delete(DATABASE_TABLE, _ID + "=?", new String[]{String.valueOf(id)});
    }
}
