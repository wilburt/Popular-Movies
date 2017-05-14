package com.jadebyte.popularmovies.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.listeners.MovieClickedListener;
import com.jadebyte.popularmovies.viewholders.FavouriteViewHolder;

/**
 * Created by wilbur on 5/13/17 at 11:59 PM.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteViewHolder> {
    private Cursor cursor;
    private MovieClickedListener clickedListener;

    public FavouriteAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void setClickedListener(MovieClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new FavouriteViewHolder(view, clickedListener, cursor);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.bindModel();
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }
}
