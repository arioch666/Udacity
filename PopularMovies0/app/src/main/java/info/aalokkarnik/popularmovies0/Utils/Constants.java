package info.aalokkarnik.popularmovies0.Utils;

/**
 * Created by Aalok Karnik on 7/14/2015.
 */
public class Constants {

    // various fields in the JSON respons from the movieDB API call
    public static  final String KEY_LISTRESULTS = "results"; // array of movie
    public static  final String KEY_MOVIEID = "id"; // internal ID
    public static  final String KEY_TITLE = "original_title"; // movie title
    public static  final String KEY_RELEASEDATE = "release_date"; // release date in YYYY-MM-DD format
    public static  final String KEY_RATING = "vote_average"; // user rating score
    public static  final String KEY_POPULARITY = "popularity"; // popularity score
    public static  final String KEY_SYNOPSIS = "overview"; // synopsis of movie
    public static  final String KEY_POSTER = "poster_path"; // relative URL to the poster image
    public static  final String KEY_POSTER_DEFAULT = "navigation_cancel"; // relative URL to a local drawable for poster_not_found edge case

    // ** KEEP SECRET
    public static  final String MOVIEDB_APIKEY = "&api_key=9c7b60a3a452f389b1e7294e7efcc6aa"; // movieDB API key (** KEEP SECRET**)
    // ** KEEP SECRET

    public static  final String MOVIEDB_POSTERURL = "http://image.tmdb.org/t/p"; // base URL to retrieve images
    public static  final String MOVIEDB_POSTERSIZE_185 = "/w185"; // retrieve images of size 185px
    public static  final String MOVIEDB_POSTERSIZE_342 = "/w342"; // retrieve images of size 342px
    public static  final String MOVIEDB_POSTERSIZE_500 = "/w500"; // retrieve images of size 500px
    public static  final String MOVIEDB_POSTERSIZE_780 = "/w780"; // retrieve images of size 780px

    public static  final String MOVIEDB_DISCOVER_MOVIE = "http://api.themoviedb.org/3/discover/movie"; // movie

    public static  final String MOVIEDB_SORTBY_POPULARITY_DESC = "sort_by=popularity.desc";
    public static  final String MOVIEDB_SORTBY_POPULARITY_ASC = "sort_by=popularity.asc";

    public static  final String CURRENT_MOVIE = "CURRENT_MOVIE";
    public static  final String MOVIE_DATA = "MOVIE_DATA";
}
