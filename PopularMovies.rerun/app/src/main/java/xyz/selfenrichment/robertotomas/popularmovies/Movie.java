package xyz.selfenrichment.robertotomas.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by RobertoTomás on 0025, 25/2/2016.
 *
 * A {@link Parcelable} Object wrapping basically a map of movie data from TMDB
 *
 */
public class Movie implements Parcelable {

    private String id;
    private String poster_path;
    private String poster_type;
    private String title;
    private String vote_average;
    private String backdrop_path;
    private String overview;
    private String release_date;

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            Movie mMovie = new Movie();
            mMovie.release_date = source.readString();
            mMovie.id = source.readString();
            mMovie.poster_path = source.readString();
            mMovie.poster_type = source.readString();
            mMovie.title = source.readString();
            mMovie.vote_average = source.readString();
            mMovie.backdrop_path = source.readString();
            mMovie.overview = source.readString();
            return mMovie;
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(release_date);
        parcel.writeString(id);
        parcel.writeString(poster_path);
        parcel.writeString(poster_type);
        parcel.writeString(title);
        parcel.writeString(vote_average);
        parcel.writeString(backdrop_path);
        parcel.writeString(overview);
    }

    public Movie(){
    }

    public Movie(Map<String,String> mapMovie) {
        release_date = mapMovie.get("release_date");
        id = mapMovie.get("id");
        poster_path = mapMovie.get("poster_path");
        poster_type = mapMovie.get("poster_type");
        title = mapMovie.get("title");
        vote_average = mapMovie.get("vote_average");
        backdrop_path = mapMovie.get("backdrop_path");
        overview = mapMovie.get("overview");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getPoster_type() {
        return poster_type;
    }

    public void setPoster_type(String poster_type) {
        this.poster_type = poster_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}

