package info.aalokkarnik.popularmovies1.Utils;

/**
 * Created by Aalok Karnik on 7/23/2015.
 */
public class UrlGenerator {

    public static String generateURL(Constants.SortCriteria sortCriteria) {

        StringBuilder url = new StringBuilder(Constants.MOVIEDB_DISCOVER_MOVIE + "?");

        switch (sortCriteria) {
            case POPULARITY_DESC: {
                url.append(Constants.MOVIEDB_SORTBY_POPULARITY_DESC);
                break;
            }
            case POPULARITY_ASC: {
                url.append(Constants.MOVIEDB_SORTBY_POPULARITY_ASC);
                break;
            }
            case RATING_DESC: {
                url.append(Constants.MOVIEDB_SORTBY_RATING_DESC);
                break;
            }
            case RATING_ASC: {
                url.append(Constants.MOVIEDB_SORTBY_RATING_ASC);
                break;
            }
        }

        url.append("&page=1&year=2015" + Constants.MOVIEDB_APIKEY);

        return url.toString();

    }

    public static String generateImageURL(String relativeImagePath) {
        return (Constants.MOVIEDB_POSTERURL + Constants.MOVIEDB_POSTERSIZE_500 + relativeImagePath);
    }
}
