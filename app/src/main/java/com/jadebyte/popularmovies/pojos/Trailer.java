package com.jadebyte.popularmovies.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.jadebyte.popularmovies.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wilbur on 5/13/17 at 12:39 PM.
 */

public class Trailer implements Parcelable{
    private String thumbUrl;
    private String videoUrl;

    public Trailer(JSONObject object) throws JSONException {
        String key = object.getString("key");
        setThumbUrl(Constants.URLS.getThumbUrl(key));
        setVideoUrl(Constants.URLS.getVideoUrl(key));
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    private void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    private void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbUrl);
        dest.writeString(this.videoUrl);
    }

    protected Trailer(Parcel in) {
        this.thumbUrl = in.readString();
        this.videoUrl = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
