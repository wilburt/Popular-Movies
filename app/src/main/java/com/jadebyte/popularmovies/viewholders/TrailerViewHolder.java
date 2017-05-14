package com.jadebyte.popularmovies.viewholders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.pojos.Trailer;
import com.jadebyte.popularmovies.utils.MyGlide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wilbur on 5/13/17 at 12:35 PM.
 */

public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @BindView(R.id.trailer_thumb) ImageView trailerThumb;
    @BindView(R.id.trailer_share) ImageView trailerShare;
    private List<Trailer> trailerList;

    public TrailerViewHolder(View itemView, List<Trailer> trailers) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.trailerList = trailers;
        itemView.setOnClickListener(this);
        trailerShare.setOnClickListener(this);
    }

    public void bindModel(int position) {
        MyGlide.load(itemView.getContext(), trailerThumb, trailerList.get(position).getThumbUrl(), null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.trailer_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, trailerList.get(getAdapterPosition()).getVideoUrl());
            itemView.getContext().startActivity(intent);

        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerList.get(getAdapterPosition()).getVideoUrl()));
            itemView.getContext().startActivity(intent);
        }
    }
}
