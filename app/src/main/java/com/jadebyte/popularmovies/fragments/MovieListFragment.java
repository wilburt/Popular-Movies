package com.jadebyte.popularmovies.fragments;


import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.adapters.MovieAdapter;
import com.jadebyte.popularmovies.listeners.EndlessRecyclerViewScrollListener;
import com.jadebyte.popularmovies.listeners.MovieClickedListener;
import com.jadebyte.popularmovies.pojos.Movie;
import com.jadebyte.popularmovies.utils.Constants;
import com.jadebyte.popularmovies.utils.MyConvert;
import com.jadebyte.popularmovies.utils.MyVolleyError;
import com.jadebyte.popularmovies.utils.VolleyCache;
import com.jadebyte.popularmovies.utils.VolleySingleton;
import com.jadebyte.popularmovies.views.GridAutoFitLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class MovieListFragment extends Fragment {

    //Constants
    private final String KEY_MOVIE_ITEMS = "movieItems";
    private final String TAG = "MovieListFragment";

    //Views
    @BindView(R.id.movie_recycler) RecyclerView recyclerView;
    @BindView(R.id.movie_pro_bar) ProgressBar mProgressBar;
    @BindView(R.id.movie_info_error_root) LinearLayout errorLayout;
    @BindView(R.id.movie_info_error_text) TextView errorText;
    @BindView(R.id.movie_info_error_button) Button retryButton;


    //Fields
    private String movieUrl;
    private String moreMoviesUrl;
    private List<Movie> movieList;
    private MovieAdapter adapter;
    private GridAutoFitLayoutManager layoutManager;
    private Unbinder mUnbinder;
    private boolean hasFailed = false;
    private String errorMessage;
    private MovieClickedListener onMovieClickedListener = sMovieCallbacks;
    private int pageNumber = 2;
    private boolean isMoreLoading = false;
    private boolean canLoadMore;
    private int initialItems;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            hasFailed = savedInstanceState.getBoolean("hasFailed");
            errorMessage = savedInstanceState.getString("errorMessage");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            movieUrl = bundle.getString(Constants.Keys.MOVIE_URL);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_ITEMS)) {
            // A successful network call has been made and parsed previously.
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_ITEMS);
        } else if (hasFailed) {
            // An unsuccessful network call has been made previously. Just show the error layout
            errorText.setText(errorMessage);
            errorLayout.setVisibility(View.VISIBLE);
            movieList = new ArrayList<>();
        } else {
            // No network call has been made or hasn't returned any response
            movieList = new ArrayList<>();
            getMovieArray(movieUrl, false);
        }

        setUpWidgets();
        widgetListeners();

        return view;
    }

    private void setUpWidgets() {
        layoutManager = new GridAutoFitLayoutManager(getActivity(), (int)MyConvert.dpToPx(150, getActivity()));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(movieList);
        recyclerView.setAdapter(adapter);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity
                (), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
    }

    private void widgetListeners() {
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorLayout.setVisibility(View.GONE);
                getMovieArray(movieUrl, false);
            }
        });

        //Add the scroll listener
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                if (canLoadMore && !isMoreLoading) {
                    moreMoviesUrl = movieUrl + "&page=" + getPageNumber();
                    getMovieArray(moreMoviesUrl, true);
                }
            }
        });

        adapter.setOnMovieClickedListener(new MovieClickedListener() {
            @Override
            public void onMovieClicked(Movie movie) {
                onMovieClickedListener.onMovieClicked(movie);
            }

            @Override
            public void onLoadMoreButtonClicked() {
                moreMoviesUrl = movieUrl + "&page=" + getPageNumber();
                getMovieArray(moreMoviesUrl, true);
            }

        });
    }

    // Get movies from themoviesdb.org
    private void getMovieArray(String url, final boolean isLoadMoreMovies){
        if (isLoadMoreMovies) {
            initialItems  = adapter == null ? 0 : adapter.getItemCount();
            isMoreLoading = true;
            processBottomViews(MovieAdapter.Status.PRE_REQUEST);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        //Creating a json request
        JsonObjectRequest moviesRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Successful request
                if (isLoadMoreMovies) {
                    initialItems = adapter.getItemCount();
                    isMoreLoading = false;
                    processBottomViews(MovieAdapter.Status.SUCCESS);
                    parseResponse(response, true);
                } else {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                        if (adapter.getItemCount() == 1) {
                            parseResponse(response, false);
                        }
                    }
                    hasFailed = false;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (isLoadMoreMovies) {
                    isMoreLoading = false;
                    if (initialItems == adapter.getItemCount()) {
                        setPageNumber(getPageNumber() - 1);
                    }
                    processBottomViews(MovieAdapter.Status.FAILED);
                } else {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                    if (adapter.getItemCount() == 1) {
                        errorMessage = MyVolleyError.errorMessage(error, getActivity());
                        errorText.setText(errorMessage);
                        errorLayout.setVisibility(View.VISIBLE);
                        hasFailed = true;
                    }
                }
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Response<JSONObject>  resp = super.parseNetworkResponse(response);
                return Response.success(resp.result, VolleyCache.parseIgnoreCacheHeaders(response, 10800000L)); // cache for 3  hours
            }
        };


        RetryPolicy policy = new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        moviesRequest.setRetryPolicy(policy);
        moviesRequest.setShouldCache(true);
        moviesRequest.setTag(TAG);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(moviesRequest);

    }

    private void parseResponse(JSONObject object, boolean isLoadMoreMovies) {
        try {

            JSONArray array = object.getJSONArray("results");
            for(int i = 0; i<array.length(); i++) {
                Movie movie = new Movie(array.getJSONObject(i));
                movieList.add(movie);

                if (isLoadMoreMovies) {
                    movieList.addAll(movie);
                }
            }

            if (isLoadMoreMovies) {
                int curSize = adapter.getItemCount();
                adapter.notifyItemRangeChanged(curSize, movieList.size()-1);
            } else {
                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            }

            int pages = object.getInt("total_pages");
            canLoadMore = pages > pageNumber;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processBottomViews(MovieAdapter.Status status) {
        adapter.processBottomViews(status);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (adapter != null && adapter.getItemCount() > 1 ) {
            savedInstanceState.putParcelableArrayList(KEY_MOVIE_ITEMS, (ArrayList<? extends Parcelable>) movieList);
        }
        savedInstanceState.putBoolean("hasFailed", hasFailed);
        savedInstanceState.putInt("moviePage", pageNumber);
        savedInstanceState.putString("errorMessage", errorMessage);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            pageNumber = savedInstanceState.getInt("moviePage");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onMovieClickedListener = (MovieClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MovieClickedListener");
        }
    }


    public int getPageNumber() {
        return pageNumber++;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    private static MovieClickedListener sMovieCallbacks = new MovieClickedListener() {
        @Override
        public void onMovieClicked(Movie movie) {

        }

        @Override
        public void onLoadMoreButtonClicked() {

        }
    };

    @Override
    public void onDestroyView(){
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(TAG);
        onMovieClickedListener = null;
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        sortItem.setVisible(true); // Show the sort item
        super.onPrepareOptionsMenu(menu);

    }
}
