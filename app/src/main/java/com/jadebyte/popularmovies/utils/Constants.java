package com.jadebyte.popularmovies.utils;public final class Constants {

    public static final class Keys {
        public static final String MOVIE_OBJECT = "movieObject";
        public static final String MOVIE_URL = "movieUrl";
    }

    public static final class URLS {
        public static final String POPULAR_URL = "http://api.themoviedb.org/3/movie/popular?api_key=YOUR_API_KEY";
        public static final String HIGHEST_RATING_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=YOUR_API_KEY";
        public static String getFullPosterUrl(int size, String path) {
            return "https://image.tmdb.org/t/p/w" + size + path;
        }
    }

}
