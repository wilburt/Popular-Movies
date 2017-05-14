package com.jadebyte.popularmovies.pojos;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.jadebyte.popularmovies.database.MovieContract.MovieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;

public class Movie implements Parcelable, Collection<Movie> {
    private String title;
    private String releaseDate;
    private String poster;
    private int voteAverage;
    private String plotSynopsis;
    private int id;

    public Movie(JSONObject movieObject) throws JSONException{
        setTitle(movieObject.getString("title"));
        setReleaseDate(movieObject.getString("release_date"));
        setPoster(movieObject.getString("poster_path"));
        setVoteAverage(movieObject.getInt("vote_average"));
        setPlotSynopsis(movieObject.getString("overview"));
        setId(movieObject.getInt("id"));
    }

    public Movie(Cursor cursor) {
        setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.TITLE)));
        setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.RELEASE_DATE)));
        setPoster(cursor.getString(cursor.getColumnIndex(MovieEntry.POSTER)));
        setVoteAverage(cursor.getInt(cursor.getColumnIndex(MovieEntry.VOTE_AVERAGE)));
        setPlotSynopsis(cursor.getString(cursor.getColumnIndex(MovieEntry.PLOT_SYNOPSIS)));
        setId(cursor.getInt(cursor.getColumnIndex(MovieEntry.MOVIE_ID)));
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    private void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    private void setPoster(String poster) {
        this.poster = poster;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    private void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    private void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.poster);
        dest.writeInt(this.voteAverage);
        dest.writeString(this.plotSynopsis);
        dest.writeInt(this.id);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.poster = in.readString();
        this.voteAverage = in.readInt();
        this.plotSynopsis = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Movie> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return null;
    }

    @Override
    public boolean add(Movie movie) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Movie> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}
