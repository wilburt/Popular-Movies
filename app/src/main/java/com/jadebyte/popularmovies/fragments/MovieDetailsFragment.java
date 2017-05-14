package com.jadebyte.popularmovies.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.adapters.ReviewAdapter;
import com.jadebyte.popularmovies.adapters.TrailerAdapter;
import com.jadebyte.popularmovies.database.MovieContract.MovieEntry;
import com.jadebyte.popularmovies.pojos.Movie;
import com.jadebyte.popularmovies.pojos.Review;
import com.jadebyte.popularmovies.pojos.Trailer;
import com.jadebyte.popularmovies.utils.Constants;
import com.jadebyte.popularmovies.utils.MyGlide;
import com.jadebyte.popularmovies.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsFragment extends Fragment {

    // Constants
    private final String TAG = "MovieDetailsFragment";
    private final String KEY_REVIEWS = "KEY_REVIEWS";
    private final String KEY_TRAILERS = "KEY_TRAILERS";

    //Views
    @BindView(R.id.movie_poster) ImageView posterView;
    @BindView(R.id.movie_title) TextView titleView;
    @BindView(R.id.movie_date) TextView dateView;
    @BindView(R.id.movie_av_rating) TextView avRating;
    @BindView(R.id.movie_av_rating_star) RatingBar ratingBar;
    @BindView(R.id.movie_synopsis) TextView synopsisView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.movie_trailers) RecyclerView rvTrailers;
    @BindView(R.id.trailers_progress) ProgressBar pbTrailers;
    @BindView(R.id.movie_reviews) RecyclerView rvReviews;
    @BindView(R.id.review_progress) ProgressBar pbReviews;
    @BindView(R.id.trailers_button) Button trailersButton;
    @BindView(R.id.reviews_button) Button reviewsButton;
    @BindView(R.id.movie_favourite) ImageView ivFavourites;

    //Fields
    private Unbinder unbinder;
    private List<Review> reviewList;
    private List<Trailer> trailerList;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private String movieId;
    private boolean hasBeenFavourited = false;
    private String title, releaseDate, posterUrl, plotSynopsis;
    private int voteAverage;
    RetryPolicy policy = new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


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
        View view = inflater.inflate(R.layout.movie_fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMovieBundle();
        setViewListeners();

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_TRAILERS)) {
            trailerList = savedInstanceState.getParcelableArrayList(KEY_TRAILERS);

        } else {
            trailerList = new ArrayList<>();
            loadTrailers();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_REVIEWS)) {
            reviewList = savedInstanceState.getParcelableArrayList(KEY_REVIEWS);

        } else {
            reviewList = new ArrayList<>();
            loadReviews();
        }

        setupRecyclerViews();
    }

    private void getMovieBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Movie movie = bundle.getParcelable(Constants.Keys.MOVIE_OBJECT);
            assert movie != null;
            movieId = String.valueOf(movie.getId());
            posterUrl = movie.getPoster();
            MyGlide.load(getActivity(), posterView, Constants.URLS.getFullPosterUrl(500, posterUrl), progressBar);
            title = movie.getTitle();
            releaseDate = movie.getReleaseDate();
            voteAverage = movie.getVoteAverage();
            plotSynopsis = movie.getPlotSynopsis();

            titleView.setText(title);
            dateView.setText(releaseDate);
            avRating.setText(String.valueOf(voteAverage));
            ratingBar.setRating(voteAverage);
            synopsisView.setText(plotSynopsis);

            checkIfMovieHasBeenFavourited();
        }
    }


    private void checkIfMovieHasBeenFavourited() {
        String[] projection = {MovieEntry.MOVIE_ID};
        String selection = MovieEntry.MOVIE_ID + " = " + movieId;
        Cursor cursor = getActivity().getContentResolver().query(MovieEntry.CONTENT_URI, projection, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            hasBeenFavourited = true;
            ivFavourites.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), PorterDuff.Mode.SRC_IN);
        }

        ivFavourites.setVisibility(View.VISIBLE);
        if (cursor != null) {
            cursor.close();
        }
    }


    private void setupRecyclerViews() {
        reviewAdapter = new ReviewAdapter(reviewList);
        trailerAdapter = new TrailerAdapter(trailerList);

        rvReviews.setAdapter(reviewAdapter);
        rvTrailers.setAdapter(trailerAdapter);

        rvReviews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTrailers.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvReviews.setNestedScrollingEnabled(false);
        rvTrailers.setNestedScrollingEnabled(false);
    }

    private void setViewListeners() {
        reviewsButton.setOnClickListener(clickListener);
        trailersButton.setOnClickListener(clickListener);
        ivFavourites.setOnClickListener(clickListener);
    }

    private void loadReviews() {

        pbReviews.setVisibility(View.VISIBLE);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(new JsonObjectRequest(Constants.URLS.getReviewsUrl(movieId), null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pbReviews.setVisibility(View.GONE);
                        try {
                            JSONArray array = response.getJSONArray("results");
                            for (int i = 0; i < array.length(); i++) {
                                reviewList.add(new Review(array.getJSONObject(i)));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        reviewAdapter.notifyItemRangeChanged(0, reviewList.size());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbReviews.setVisibility(View.GONE);
                reviewsButton.setVisibility(View.VISIBLE);
            }
        }).setTag(TAG).setShouldCache(true).setRetryPolicy(policy));

    }

    private void loadTrailers() {

        pbTrailers.setVisibility(View.VISIBLE);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(new JsonObjectRequest(Constants.URLS.getTrailersUrl(movieId), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pbTrailers.setVisibility(View.GONE);
                        try {
                            JSONArray array = response.getJSONArray("results");
                            for (int i = 0; i < array.length(); i++) {
                                trailerList.add(new Trailer(array.getJSONObject(i)));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        trailerAdapter.notifyItemRangeChanged(0, trailerList.size());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbTrailers.setVisibility(View.GONE);
                trailersButton.setVisibility(View.VISIBLE);
            }
        }).setTag(TAG).setShouldCache(true).setRetryPolicy(policy));
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.reviews_button: {
                    view.setVisibility(View.GONE);
                    loadReviews();
                    break;
                }

                case R.id.trailers_button: {
                    view.setVisibility(View.GONE);
                    loadTrailers();
                    break;
                }
                
                case R.id.movie_favourite: {
                    if (hasBeenFavourited) {
                        removeMovieMovieFrmCR();

                    } else {
                        insertMovieToCR();
                    }
                    break;
                }
            }
        }
    };

    private void insertMovieToCR() {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.TITLE, title);
        values.put(MovieEntry.RELEASE_DATE, releaseDate);
        values.put(MovieEntry.POSTER, posterUrl);
        values.put(MovieEntry.VOTE_AVERAGE, voteAverage);
        values.put(MovieEntry.PLOT_SYNOPSIS, plotSynopsis);
        values.put(MovieEntry.MOVIE_ID, movieId);
        Uri uri = getActivity().getContentResolver().insert(MovieEntry.CONTENT_URI, values);

        if (uri != null) {
            ivFavourites.setColorFilter(ContextCompat.getColor(getActivity(), R.color.yellow), PorterDuff.Mode.SRC_IN);
            Toast.makeText(getActivity(), R.string.movie_favourited, Toast.LENGTH_SHORT).show();
            hasBeenFavourited = true;
        }
    }

    private void removeMovieMovieFrmCR() {
        String where = MovieEntry.MOVIE_ID + " = " + movieId;
        int rows = getActivity().getContentResolver().delete(MovieEntry.CONTENT_URI, where, null);


        if (rows != 0) {
            ivFavourites.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey), PorterDuff.Mode.SRC_IN);
            Toast.makeText(getActivity(), R.string.movie_unfavour, Toast.LENGTH_SHORT).show();
            hasBeenFavourited = false;

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
    public void onDestroyView() {
        super.onDestroyView();
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(TAG);
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (reviewAdapter != null && reviewAdapter.getItemCount() != 0) {
            outState.putParcelableArrayList(KEY_REVIEWS, (ArrayList<? extends Parcelable>) reviewList);
        }

        if (trailerAdapter != null && trailerAdapter.getItemCount() != 0) {
            outState.putParcelableArrayList(KEY_TRAILERS, (ArrayList<? extends Parcelable>) trailerList);
        }
    }
}
