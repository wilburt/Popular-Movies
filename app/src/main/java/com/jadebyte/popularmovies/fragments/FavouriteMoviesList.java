package com.jadebyte.popularmovies.fragments;


import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.adapters.FavouriteAdapter;
import com.jadebyte.popularmovies.database.MovieContract.MovieEntry;
import com.jadebyte.popularmovies.listeners.MovieClickedListener;
import com.jadebyte.popularmovies.pojos.Movie;
import com.jadebyte.popularmovies.utils.MyConvert;
import com.jadebyte.popularmovies.views.GridAutoFitLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavouriteMoviesList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int ID_MOVIES_LOADER = 2222;
    private final String TAG = "FavouriteMoviesList";

    @BindView(R.id.movie_recycler) RecyclerView recyclerView;
    @BindView(R.id.movie_pro_bar) ProgressBar mProgressBar;
    @BindView(R.id.favourite_movie_error) TextView textView;

    private Unbinder unbinder;
    private FavouriteAdapter adapter;
    private MovieClickedListener onMovieClickedListener = sMovieCallbacks;

    public FavouriteMoviesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_movies_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        getLoaderManager().initLoader(ID_MOVIES_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_MOVIES_LOADER:
                Uri queryUri = MovieEntry.CONTENT_URI;
                String sortOrder = MovieEntry._ID + " ASC";
                return new CursorLoader(getActivity(), queryUri, null,  null, null, sortOrder);

            default:
                throw new RuntimeException("Loaded not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setupRecyclerView(data);
        mProgressBar.setVisibility(View.GONE);
        if (data != null && data.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.notifyDataSetChanged();

    }

    private void setupRecyclerView(Cursor cursor) {

        recyclerView.setLayoutManager(new GridAutoFitLayoutManager(getActivity(), (int) MyConvert.dpToPx(150, getActivity())));
        adapter = new FavouriteAdapter(cursor);
        recyclerView.setAdapter(adapter);
        adapter.setClickedListener(new MovieClickedListener() {
            @Override
            public void onMovieClicked(Movie movie) {
                onMovieClickedListener.onMovieClicked(movie);
            }


            @Override
            public void onLoadMoreButtonClicked() {

            }
        });

        adapter.notifyDataSetChanged();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onMovieClickedListener = (MovieClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MovieClickedListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onMovieClickedListener = null;
        unbinder.unbind();
    }
}
