package com.jadebyte.popularmovies.utils;

import android.net.Uri;

import com.jadebyte.popularmovies.BuildConfig;

public final class Constants {

    public static final class Keys {
        public static final String MOVIE_OBJECT = "movieObject";
        public static final String MOVIE_URL = "movieUrl";
        public static final String MOVIE_ID = "movieId";
    }

    public static final class URLS {
        public static String getFullPosterUrl(int size, String path) {
            // new Uri.Builder().scheme("https").authority("image.tmdb.org").path("/t/p/w")
            return "https://image.tmdb.org/t/p/w" + size + path;
        }

        public static String getThumbUrl(String key) {
            return new Uri.Builder().scheme("http").authority("img.youtube.com").appendPath("vi").appendPath(key).appendPath("mqdefault.jpg").build().toString();
        }

        public static String getVideoUrl(String key) {
            return new Uri.Builder().scheme("https").authority("youtube.com").appendPath("watch").appendQueryParameter("v", key).build().toString();
        }

        public static String getReviewsUrl(String id) {
            return new Uri.Builder().scheme("https").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath(id).appendPath("reviews")
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_TOKEN).build().toString();
        }

        public static String getTrailersUrl(String id) {
            return new Uri.Builder().scheme("https").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath(id).appendPath("videos")
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_TOKEN).build().toString();
        }

        public static String getPopularUrl() {
            return new Uri.Builder().scheme("https").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath("popular")
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_TOKEN).build().toString();
        }

        public static String getHighestRatingUrl() {
            return new Uri.Builder().scheme("https").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath("top_rated")
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_TOKEN).build().toString();
        }
    }

}
