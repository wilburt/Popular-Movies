package com.jadebyte.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.pojos.Review;
import com.jadebyte.popularmovies.viewholders.ReviewViewHolder;

import java.util.List;

/**
 * Created by wilbur on 5/13/17 at 1:47 PM.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bindModel(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }
}
