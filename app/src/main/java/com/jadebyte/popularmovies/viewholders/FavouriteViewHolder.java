package com.jadebyte.popularmovies.viewholders;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.database.MovieContract;
import com.jadebyte.popularmovies.listeners.MovieClickedListener;
import com.jadebyte.popularmovies.pojos.Movie;
import com.jadebyte.popularmovies.utils.Constants;
import com.jadebyte.popularmovies.utils.MyGlide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wilbur on 5/13/17 at 11:50 PM.
 */

public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @BindView(R.id.movie_poster) ImageView movieAvatar;
    @BindView(R.id.movie_item_root) LinearLayout rootLayout;
    private MovieClickedListener clickedListener;
    private Cursor cursor;

    public FavouriteViewHolder(final View View, MovieClickedListener clickedListener, Cursor cursor) {
        super(View);
        ButterKnife.bind(this, View);
        this.clickedListener = clickedListener;
        rootLayout.setOnClickListener(this);
        this.cursor = cursor;
    }

    public void bindModel() {
        String fullPosterUrl = Constants.URLS.getFullPosterUrl(300, cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.POSTER)));
        MyGlide.load(itemView.getContext(), movieAvatar, fullPosterUrl, null);
    }

    @Override
    public void onClick(View v) {
        cursor.moveToPosition(getAdapterPosition());
        clickedListener.onMovieClicked(new Movie(cursor));
    }
}
