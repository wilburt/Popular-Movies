package com.jadebyte.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.pojos.Trailer;
import com.jadebyte.popularmovies.viewholders.TrailerViewHolder;

import java.util.List;

/**
 * Created by wilbur on 5/13/17 at 1:15 PM.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {
    private List<Trailer> trailerList;

    public TrailerAdapter(List<Trailer> trailerList) {
        this.trailerList = trailerList;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view, trailerList);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bindModel(position);
    }

    @Override
    public int getItemCount() {
        return trailerList == null ? 0 : trailerList.size();
    }
}
