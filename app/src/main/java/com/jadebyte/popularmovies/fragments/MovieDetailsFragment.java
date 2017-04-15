package com.jadebyte.popularmovies.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.pojos.Movie;
import com.jadebyte.popularmovies.utils.Constants;
import com.jadebyte.popularmovies.utils.MyGlide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class MovieDetailsFragment extends Fragment {

    //Views
    @BindView(R.id.movie_poster) ImageView posterView;
    @BindView(R.id.movie_title) TextView titleView;
    @BindView(R.id.movie_date) TextView dateView;
    @BindView(R.id.movie_av_rating) TextView avRating;
    @BindView(R.id.movie_av_rating_star) RatingBar ratingBar;
    @BindView(R.id.movie_synopsis) TextView synopsisView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    //Fields
    private Unbinder unbinder;


    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.movie_fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        getMovieBundle();
        return view;
    }


    private void getMovieBundle(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            Movie movie = bundle.getParcelable(Constants.Keys.MOVIE_OBJECT);
            assert movie != null;
            String movieUrl = Constants.URLS.getFullPosterUrl(500, movie.getPoster());
            MyGlide.load(getActivity(), posterView, movieUrl, progressBar);
            titleView.setText(movie.getTitle());
            dateView.setText(movie.getReleaseDate());
            avRating.setText(String.valueOf(movie.getVoteAverage()));
            ratingBar.setRating(movie.getVoteAverage());
            synopsisView.setText(movie.getPlotSynopsis());
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        boolean hasTwoPanes = getResources().getBoolean(R.bool.has_two_panes);
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        // Don't hide the sort button on tablets
        if (!hasTwoPanes) {
            sortItem.setVisible(false);
        } else {
            sortItem.setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

}
