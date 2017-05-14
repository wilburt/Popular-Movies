package com.jadebyte.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jadebyte.popularmovies.database.MovieContract.MovieEntry;

/**
 * Created by wilbur on 5/13/17 at 6:07 PM.
 */

public class MovieProvider extends ContentProvider{
    public static final int CODE_MOVIE = 100;

    // We won't use it anyway
    public static final int CODE_MOVIE_WITH_ID = 1001;

    public static final UriMatcher uriMatcher = buildUriMatcher();
    public MovieDbHelper movieDbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // To match movies without id i.e the whole rows in the table
        matcher.addURI(authority, MovieContract.PATH_MOVIES, CODE_MOVIE);

        // To match a particular movie with id
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor cursor;

        // To match the uri if it's the whole list or a particular movie
        switch (uriMatcher.match(uri)) {

            // Just in case we are not using it
            case CODE_MOVIE_WITH_ID: {
                String idString = uri.getLastPathSegment();

                String[] selectionArguments = new String[] {idString};

                cursor = movieDbHelper.getReadableDatabase().query(MovieEntry.TABLE_NAME, projection, MovieEntry.MOVIE_ID + " = ?",
                        selectionArguments, null, null, sortOrder);
                break;
            }

            case CODE_MOVIE: {
                cursor = movieDbHelper.getReadableDatabase().query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: Where the goddamn fuck did you get " + uri + " from?");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
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
        long rowId = movieDbHelper.getWritableDatabase().insert(MovieEntry.TABLE_NAME, null, values);

        // If the row was successfully added
        if (rowId > 0) {
            Uri myUri = ContentUris.withAppendedId(MovieContract.BASE_CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(myUri, null);
            return myUri;
        }

        throw new SQLiteException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deletedMoviesRows = 0;

        // Passing null as selection will delete the entire table and will not return the number of rows deleted. But if we pass 1, it'll
        // still delete all the rows but in this case will return the number of rows deleted
        if (selection == null) {
            selection = "1";
        }

        switch (uriMatcher.match(uri)) {
            case CODE_MOVIE: {
                deletedMoviesRows = movieDbHelper.getWritableDatabase().delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            }

            default:
                throw new UnsupportedOperationException("Unknown uri: Where the goddamn fuck did you get " + uri + " from?");
        }

        // Notify that we have deleted n of rows
        if (deletedMoviesRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedMoviesRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Read the docs Engineer! We aren't supporting updating");
    }
}
