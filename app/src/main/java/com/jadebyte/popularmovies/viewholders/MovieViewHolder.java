

/**
 * Created by William Wilbur on 1/1/17.
 */
package com.jadebyte.popularmovies.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.listeners.MovieClickedListener;
import com.jadebyte.popularmovies.pojos.Movie;
import com.jadebyte.popularmovies.utils.Constants;
import com.jadebyte.popularmovies.utils.MyGlide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @BindView(R.id.movie_poster) ImageView movieAvatar;
    @BindView(R.id.movie_item_root) LinearLayout rootLayout;
    private MovieClickedListener clickedListener;
    private List<Movie> moviesList;

    public MovieViewHolder(final View View, MovieClickedListener clickedListener, List<Movie> moviesList) {
        super(View);
        ButterKnife.bind(this, View);
        this.clickedListener = clickedListener;
        this.moviesList = moviesList;
        rootLayout.setOnClickListener(this);
    }

    public void bindModel(final Movie movie) {
        String fullPosterUrl = Constants.URLS.getFullPosterUrl(300,  movie.getPoster());
        MyGlide.load(itemView.getContext(), movieAvatar, fullPosterUrl, null);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        clickedListener.onMovieClicked(moviesList.get(position));
    }
}
