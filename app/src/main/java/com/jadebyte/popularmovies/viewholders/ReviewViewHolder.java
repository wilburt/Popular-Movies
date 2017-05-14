package com.jadebyte.popularmovies.viewholders;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.pojos.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wilbur on 5/13/17 at 1:40 PM.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.review_author) TextView tvAuthor;
    @BindView(R.id.review_content) TextView tvContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindModel(Review review) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvAuthor.setText(Html.fromHtml(review.getAuthor(), Html.FROM_HTML_MODE_COMPACT));
            tvContent.setText(Html.fromHtml(review.getContent(), Html.FROM_HTML_MODE_COMPACT));

        } else {
            tvAuthor.setText(Html.fromHtml(review.getAuthor()));
            tvContent.setText(Html.fromHtml(review.getContent()));
        }
    }
}
