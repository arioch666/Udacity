package info.aalokkarnik.popularmovies1.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import info.aalokkarnik.popularmovies1.Model.MovieItem;

public class Sanitizer {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static MovieItem sanitizeData(JSONObject jsonMovieObject) {

        MovieItem movieItem = new MovieItem();
        try {

            // * Movie ID
            movieItem.movieID = jsonMovieObject.getLong(Constants.KEY_MOVIEID);

            // * Movie Title
            movieItem.movieTitle = jsonMovieObject.getString(Constants.KEY_TITLE);

            // * Movie Release Date
            // Movie could have an unspecified release date. Reason: new movie / lack of data
            if (jsonMovieObject.has(Constants.KEY_RELEASEDATE)) {
                String releaseDate = jsonMovieObject.getString(Constants.KEY_RELEASEDATE);
                if ( (releaseDate.equals("")) || (releaseDate.isEmpty()) || (releaseDate.equals("null")) )
                    movieItem.movieReleaseDate = dateFormat.parse("1111-11-11");
                else
                    movieItem.movieReleaseDate = dateFormat.parse(releaseDate);
            }

            // * Movie Poster
            // Movie could have a missing poster in which case we have to assign a blank image
            if (jsonMovieObject.has(Constants.KEY_POSTER)) {
                movieItem.moviePoster = jsonMovieObject.getString(Constants.KEY_POSTER);
                if ( (movieItem.moviePoster == null) || (movieItem.moviePoster.equals("null")) || (movieItem.moviePoster.length() == 0))
                    movieItem.moviePoster = Constants.KEY_POSTER_DEFAULT;
            }

            // * Movie Synopsis
            // Movie could have an empty synopsis
            if (jsonMovieObject.has(Constants.KEY_SYNOPSIS)) {
                movieItem.movieSynopsis = jsonMovieObject.getString(Constants.KEY_SYNOPSIS);
                if ( (movieItem.movieSynopsis == null) || (movieItem.movieSynopsis.equals("null")) || (movieItem.movieSynopsis.length() == 0))
                    movieItem.movieSynopsis = "No synopsis specified.";
            }

            // * Movie Rating Score
            // a movie could potentially have no rating score
            if (jsonMovieObject.has(Constants.KEY_RATING))
                movieItem.movieRating = Float.parseFloat(String.format(jsonMovieObject.getString(Constants.KEY_RATING), "%.2f%n"));
            else
                movieItem.movieRating = 0.0f;

            // * Movie Popularity Score
            // a movie could potentially have no popularity score
            if (jsonMovieObject.has(Constants.KEY_POPULARITY))
                movieItem.moviePopularity = Float.parseFloat(String.format(jsonMovieObject.getString(Constants.KEY_POPULARITY), "%.2f%n"));
            else
                movieItem.moviePopularity = 0.0f;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return movieItem;
    }
}
