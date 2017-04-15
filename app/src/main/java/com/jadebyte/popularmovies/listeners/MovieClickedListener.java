package com.jadebyte.popularmovies.listeners;

import com.jadebyte.popularmovies.pojos.Movie;

public interface MovieClickedListener {
    //Handles clicks on the gadget and  items of GadgetFragment, MovieListFragment, CatsFragment and DetailsFragment
    void onMovieClicked(Movie movie);
    void onLoadMoreButtonClicked();
}
