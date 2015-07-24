package info.aalokkarnik.popularmovies0.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import info.aalokkarnik.popularmovies0.Adapters.MovieListAdapter;
import info.aalokkarnik.popularmovies0.Model.MovieItem;
import info.aalokkarnik.popularmovies0.Network.VolleySingleton;
import info.aalokkarnik.popularmovies0.Utils.Constants;

import info.aalokkarnik.popularmovies0.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListingFragment extends Fragment {

    private GridView gridView;
    private MovieListAdapter movieListAdapter;

    private ArrayList<MovieItem> arrMovieDetails;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private CommunicatorMovieListing communicatorMovieListing;

    public MovieListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("AAK", "----------- NEW RUN : MOVIELISTING -----------");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Create a new bundle to save the parsed movie data prior to the app losing focus
        if (outState == null)
            outState = new Bundle();

        Log.d("AAK", "MovieList: SAVE STATE: Saving State " + this.arrMovieDetails.size());
        outState.putParcelableArrayList(Constants.MOVIE_DATA, this.arrMovieDetails);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Step 1: Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_listing, container, false);

        // Step 2: Get an instance to the GridView
        this.gridView = (GridView) view.findViewById(R.id.gridView);

        // Step 3: Extract the movieData array from bundle
        if (savedInstanceState == null) {
            // Step 3a: This is the first time the app runs
            this.arrMovieDetails = new ArrayList<>();
            getMovieListing();
            Log.d("AAK", "MovieList: RESTORE STATE: Empty movie data");
        } else {
            // Step 3a: Result of screen rotation
            this.arrMovieDetails = savedInstanceState.getParcelableArrayList(Constants.MOVIE_DATA);
            movieListAdapter = new MovieListAdapter(getActivity(), this.arrMovieDetails);
            gridView.setAdapter(this.movieListAdapter);

            setCommunicator((CommunicatorMovieListing) getActivity());

            Log.d("AAK", "MovieList: RESTORE STATE: Movie data from Bundle " + this.arrMovieDetails.size());
            Log.d("AAK", "MovieList: RESTORE STATE: Network call not executed");
        }

        // Step 4: Register the gridview to listen to item clicks
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MovieItem m = arrMovieDetails.get(position);
                communicatorMovieListing.movieListingResponse(m);
            }
        });

        return view;
    }

    public ArrayList<MovieItem> getMovieData() {
        return this.arrMovieDetails;
    }

    private void getMovieListing() {

        VolleySingleton volleySingleton;
        RequestQueue requestQueue;
        JsonObjectRequest jsonObjectRequest;

        // Step 1: Initialize the Volley and RequestQueue objects to assist with the network calls.
        volleySingleton = VolleySingleton.getSingletonInstance();
        requestQueue = volleySingleton.getRequestQueue();

        // Step 2: Generate the URL to be used for the MovieDB API call.
        String url = generateURL();
        Log.d("AAK", url);

        Log.d("AAK", "Fetching movie listings data ...");

        // Step 3: Make the API call and handle any errors by providing appropriate feedback to the user.
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Populate the arraylist with parsed JSON data from the API call
                arrMovieDetails = parseJSONResponse(response);

                // Set the gridview data adapter to
                movieListAdapter = new MovieListAdapter(getActivity(), arrMovieDetails);
                gridView.setAdapter(movieListAdapter);

                Log.d("AAK", "SUCCESS: JSON Response received ...");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TO-DO
                // Handle network errors issues here.
            }
        });

        // Step 4: Add the custom request to Volley's request queue worker thread.
        requestQueue.add(jsonObjectRequest);
    }

    private String generateURL() {
        return Constants.MOVIEDB_DISCOVER_MOVIE + "?" + Constants.MOVIEDB_SORTBY_POPULARITY_DESC + "&page=1&year=2015" + Constants.MOVIEDB_APIKEY;
    }

    private ArrayList<MovieItem> parseJSONResponse(JSONObject response) {

        Log.d("AAK", "Parsing response ...");

        // Step 1: Create an empty arraylist which will serve as the placeholder for the data.
        // Arraylist will comprise of custom object mimicking the required fields of Movie items returned from MovieDB API call
        ArrayList<MovieItem> arrParsedMovieData = new ArrayList<>();

        // Step 2: Sanity check for null or zero length response.
        if ((response == null) || (response.length() == 0)) {
            Log.d("AAK", "No entries found");
            Toast.makeText(getActivity(), "MovieDB Error: Movie data request has generated an empty response. Please refresh.", Toast.LENGTH_SHORT).show();
            return null;
        }

        try {

            JSONObject jsonMovieObject;
            MovieItem movieItem;

            // Step 3: Extract the movie array from the JSON response.
            JSONArray jsonMovies = response.getJSONArray(Constants.KEY_LISTRESULTS);

            // Step 4: Loop through the individual JSON movie objects
            // Save them to the MovieItem model

            for (int i = 0; i < jsonMovies.length(); i++) {
                jsonMovieObject = jsonMovies.getJSONObject(i);
                movieItem = this.assignMovieFields(jsonMovieObject);
                arrParsedMovieData.add(movieItem);
            }

            Log.d("AAK", "Parsed data for " + arrParsedMovieData.size() + " movies ...");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrParsedMovieData;
    }

    private MovieItem assignMovieFields(JSONObject jsonMovieObject) {
        MovieItem movieItem = new MovieItem();
        try {
            movieItem.movieID = jsonMovieObject.getLong(Constants.KEY_MOVIEID);
            movieItem.movieTitle = jsonMovieObject.getString(Constants.KEY_TITLE);

            movieItem.movieReleaseDate = dateFormat.parse(jsonMovieObject.getString(Constants.KEY_RELEASEDATE));

            if (jsonMovieObject.has(Constants.KEY_POSTER))
                movieItem.moviePoster = jsonMovieObject.getString(Constants.KEY_POSTER);
            else
                movieItem.moviePoster = Constants.KEY_POSTER_DEFAULT;

            movieItem.movieSynopsis = jsonMovieObject.getString(Constants.KEY_SYNOPSIS);

            // a movie could potentially have no rating score
            if (jsonMovieObject.has(Constants.KEY_RATING))
                movieItem.movieRating = Float.parseFloat(String.format(jsonMovieObject.getString(Constants.KEY_RATING), "%.2f%n"));
            else
                movieItem.movieRating = 0.0f;

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

    public void setCommunicator(CommunicatorMovieListing communicatorMovieListing) {
        this.communicatorMovieListing = communicatorMovieListing;
        Log.d("AAK", "MovieListing: Communicator Set");
    }

    public interface CommunicatorMovieListing {
        void movieListingResponse(MovieItem m);
    }

    // *************************
    // Sorting logic to sort movies listings by (popularity/ratings) in an (ASC/DESC) order respectively
    // *************************

    private class SortByMovieRating implements Comparator<MovieItem> {

        private boolean sortRating_DESC;

        private SortByMovieRating(boolean sortRating_DESC) {
            this.sortRating_DESC = sortRating_DESC;
        }

        @Override
        public int compare(MovieItem lhs, MovieItem rhs) {

            if (this.sortRating_DESC) {

                // descending rating sort order
                if (lhs.movieRating < rhs.movieRating) {
                    return 1;
                } else if (lhs.movieRating > rhs.movieRating) {
                    return -1;
                } else return 0;

            } else {

                // ascending rating sort order
                if (lhs.movieRating < rhs.movieRating) {
                    return -1;
                } else if (lhs.movieRating > rhs.movieRating) {
                    return 1;
                } else return 0;

            }

        }
    }

    public void sortByRating(boolean sortByRating_DESC) {

        Collections.sort(this.arrMovieDetails, new SortByMovieRating(sortByRating_DESC));
        this.movieListAdapter.notifyDataSetChanged();

        if (sortByRating_DESC)
            Log.d("AAK", "Sorting Highest Rated to Lowest Rated");
        else
            Log.d("AAK", "Sorting Lowest Rated to Highest Rated");
    }

    private class SortByMoviePopularity implements Comparator<MovieItem> {

        private boolean sortPopularity_DESC;

        private SortByMoviePopularity(boolean sortPopularity_DESC) {
            this.sortPopularity_DESC = sortPopularity_DESC;
        }

        @Override
        public int compare(MovieItem lhs, MovieItem rhs) {

            if (this.sortPopularity_DESC) {

                // descending popularity sort order
                if (lhs.moviePopularity < rhs.moviePopularity) {
                    return 1;
                } else if (lhs.moviePopularity > rhs.moviePopularity) {
                    return -1;
                } else return 0;

            } else {

                // ascending popularity sort order
                if (lhs.moviePopularity < rhs.moviePopularity) {
                    return -1;
                } else if (lhs.moviePopularity > rhs.moviePopularity) {
                    return 1;
                } else return 0;

            }

        }

    }

    public void sortByPopularity(boolean sortByPopularity_DESC) {

        Collections.sort(this.arrMovieDetails, new SortByMoviePopularity(sortByPopularity_DESC));
        this.movieListAdapter.notifyDataSetChanged();

        if (sortByPopularity_DESC)
            Log.d("AAK", "Sorting Most Popular to Least Popular");
        else
            Log.d("AAK", "Sorting Least Popular to Most Popular");
    }
}