package com.jadebyte.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jadebyte.popularmovies.database.MovieContract.MovieEntry;

/**
 * Created by wilbur on 5/13/17 at 6:24 PM.
 */

public class MovieDbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "movies";
    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.TITLE +  " REAL NOT NULL, " +
                        MovieEntry.RELEASE_DATE +  " REAL NOT NULL, " +
                        MovieEntry.POSTER +  " REAL NOT NULL, " +
                        MovieEntry.VOTE_AVERAGE +  " INTEGER NOT NULL, " +
                        MovieEntry.MOVIE_ID +  " INTEGER NOT NULL, " +
                        MovieEntry.PLOT_SYNOPSIS +  " REAL NOT NULL, " +
                        "UNIQUE (" + MovieEntry.MOVIE_ID +  ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
