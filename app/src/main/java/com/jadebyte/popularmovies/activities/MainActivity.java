package com.jadebyte.popularmovies.activities;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jadebyte.popularmovies.R;
import com.jadebyte.popularmovies.fragments.BlankFragment;
import com.jadebyte.popularmovies.fragments.FavouriteMoviesList;
import com.jadebyte.popularmovies.fragments.MovieDetailsFragment;
import com.jadebyte.popularmovies.fragments.MovieListFragment;
import com.jadebyte.popularmovies.listeners.MovieClickedListener;
import com.jadebyte.popularmovies.pojos.Movie;
import com.jadebyte.popularmovies.utils.Constants;
import com.jadebyte.popularmovies.utils.MyFile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieClickedListener {

    //Views
    @BindView(R.id.movie_toolbar) Toolbar toolbar;

    //Fields
    private boolean hasTwoPanes;
    private Fragment mContent;
    private int checkItemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        hasTwoPanes = getResources().getBoolean(R.bool.has_two_panes);

        if (savedInstanceState == null) {
            checkItemIndex = 0;
            launchMovieListFragment(Constants.URLS.getPopularUrl());
            launchBlankFragment();
        } else {
            checkItemIndex = savedInstanceState.getInt("checkItemIndex");
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }

    }


    // Start a fragment that does nothing but display
    // {@link com.jadebyte.popularmovies.R.string#details_stub } on the right pane if the device is a
    // tablet
    private void launchBlankFragment() {
        if (hasTwoPanes) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
            ft.add(R.id.movie_content_details, new BlankFragment());
            ft.commit();
        }
    }

    private void launchMovieListFragment(String moviesUrl) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Keys.MOVIE_URL, moviesUrl);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.movie_content_frame, fragment, "MovieListFragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }

    private void launchFavoriteListFragment() {
        FavouriteMoviesList fragment = new FavouriteMoviesList();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.movie_content_frame, fragment, "FavouriteMoviesList");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_license:
                showLicenseDialog();
                break;
            case R.id.action_sort:
                showLabelsPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Start {@link MovieDetailsFragment
    public void launchDetailsFrag(@NonNull Movie movie) {
        MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.Keys.MOVIE_OBJECT, movie);
        detailsFragment.setArguments(bundle);

        int container = hasTwoPanes ? R.id.movie_content_details : R.id.movie_content_frame;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.add(container, detailsFragment, "MovieDetailsFragment");
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mContent != null) {
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        }
        outState.putInt("checkItemIndex", checkItemIndex);
        super.onSaveInstanceState(outState);
    }

    private void showLicenseDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_license, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        TextView textView = (TextView) view.findViewById(R.id.license_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(MyFile.readFromAssets(this, "license.html"), Html
                    .FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(MyFile.readFromAssets(this, "license.html")));
        }
        builder.show();
    }

    private void showLabelsPopup() {
        View view = findViewById(R.id.action_sort);
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String moviesUrl = null;
                
                switch (item.getItemId()) {
                    case R.id.sort_popular:
                        moviesUrl = Constants.URLS.getPopularUrl();
                        checkItemIndex = 0;
                        break;

                    case R.id.sort_rating:
                        moviesUrl = Constants.URLS.getHighestRatingUrl();
                        checkItemIndex = 1;
                        break;

                    case R.id.sort_favourites:
                        checkItemIndex = 2;
                    
                    default:
                        break;
                }

                if (!item.isChecked()) {
                    if (moviesUrl != null) {
                        launchMovieListFragment(moviesUrl);

                    } else {
                        launchFavoriteListFragment();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.inflate(R.menu.sort_menu);
        popup.show();
        final MenuItem popupItem = popup.getMenu().getItem(checkItemIndex);
        popupItem.setCheckable(true);
        popupItem.setChecked(true);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        launchDetailsFrag(movie);
    }

    @Override
    public void onLoadMoreButtonClicked() {
        // Do nothing. This is not for your consumption.
    }
}
