package com.jadebyte.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.listeners.MovieClickedListener;
import com.jadebyte.popularmovies.pojos.Movie;
import com.jadebyte.popularmovies.viewholders.BottomViewHolder;
import com.jadebyte.popularmovies.viewholders.MovieViewHolder;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MovieClickedListener onMovieClicked;
    private List<Movie> movieList;
    private int lastPosition = -1;
    private final int VIEW_MOVIE = 0;
    private final int VIEW_PROG = 1;
    private BottomViewHolder bottomViewHolder;
    public enum Status {FAILED, SUCCESS, PRE_REQUEST}

    public MovieAdapter(List<Movie> movieList) {
        super();
        this.movieList = movieList;
        this.onMovieClicked = null;
    }

    public void setOnMovieClickedListener (MovieClickedListener onItemClickedListener) {
        this.onMovieClicked = onItemClickedListener;
    }

    public void processBottomViews(Status status) {
        if (bottomViewHolder != null) { // To avoid good 'ol NullPointerException don't show/hide bottom loading indicator and button
            bottomViewHolder.processViews(status);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPosition(position)) {
            return VIEW_MOVIE;
        } else {
            return VIEW_PROG;
        }
    }

    private boolean isPosition(int position) {
        return position != getItemCount()-1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            if (viewType == VIEW_MOVIE) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_card, parent, false);
                return new MovieViewHolder(view, onMovieClicked, movieList);
            } else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_bottm_layout, parent, false);
                bottomViewHolder =  new BottomViewHolder(view, onMovieClicked);
                return bottomViewHolder;
            }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            ((MovieViewHolder) holder).bindModel(movieList.get(position));
            setAnimation(holder.itemView, position);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        //if the item wasn't already displayed on screen. it's animated
        if (position > lastPosition) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, 80, 0);
            animation.setDuration(400);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount(){
        //Return the number of items in the data set
        return movieList == null ? (0) : movieList.size()+1;
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        holder.itemView.clearAnimation();
    }
}
