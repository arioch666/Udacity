package info.aalokkarnik.popularmovies1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Aalok Karnik on 7/12/2015.
 */
public class MovieItem implements Parcelable {
    public long movieID;
    public String moviePoster;
    public Date movieReleaseDate;
    public Float movieRating;
    public Float moviePopularity;
    public String movieTitle;
    public String movieSynopsis;

    public MovieItem() {
    }

    protected MovieItem(Parcel in) {
        movieID = in.readLong();
        movieTitle = in.readString();
        moviePoster = in.readString();
        movieSynopsis = in.readString();
        movieRating = in.readFloat();
        moviePopularity = in.readFloat();
        movieReleaseDate = new Date(in.readLong());
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {

        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(movieID);
        dest.writeString(movieTitle);
        dest.writeString(moviePoster);
        dest.writeString(movieSynopsis);
        dest.writeFloat(movieRating);
        dest.writeFloat(moviePopularity);
        dest.writeLong(movieReleaseDate.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}