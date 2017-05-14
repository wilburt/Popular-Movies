package com.jadebyte.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by wilbur on 5/13/17 at 6:09 PM.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.jadebyte.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES =  "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "release_date";
        public static final String POSTER =  "poster";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String PLOT_SYNOPSIS = "plot_synopsis";
        public static final String MOVIE_ID = "movie_id";

    }

}
