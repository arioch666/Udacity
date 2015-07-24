package info.aalokkarnik.popularmovies1.Fragments;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import info.aalokkarnik.popularmovies1.Adapters.MovieListAdapter;
import info.aalokkarnik.popularmovies1.Model.MovieItem;
import info.aalokkarnik.popularmovies1.Network.VolleySingleton;
import info.aalokkarnik.popularmovies1.R;
import info.aalokkarnik.popularmovies1.Utils.Constants;
import info.aalokkarnik.popularmovies1.Utils.Sanitizer;
import info.aalokkarnik.popularmovies1.Utils.UrlGenerator;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListingFragment extends Fragment {

    private GridView gridView = null;
    private MovieListAdapter movieListAdapter = null;

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

        // Step 2: Get an instance to the GridView since layout elements can be referenced post layout inflation
        this.gridView = (GridView) view.findViewById(R.id.gridView);

        // Step 3: Initialize the movie dataset
        if (savedInstanceState == null) {
            // Step 3a: This is the first time the app runs
            this.arrMovieDetails = new ArrayList<>();
            Log.d("AAK", "MovieList: RESTORE STATE: Empty movie data");
        } else {
            // Step 3a: Movie data extracted from bundle as a result of screen rotation
            this.arrMovieDetails = savedInstanceState.getParcelableArrayList(Constants.MOVIE_DATA);

            // setCommunicator((CommunicatorMovieListing) getActivity());

            Log.d("AAK", "MovieList: RESTORE STATE: Movie data from Bundle " + this.arrMovieDetails.size());
            Log.d("AAK", "MovieList: RESTORE STATE: Network call not executed");
        }

        // Step 4: Initialize the data adapter with the movie dataset which could be
        // 1. empty (if app starts for the first time)
        // 2. extracted from the bundle if activity is a result of screen rotation
        movieListAdapter = new MovieListAdapter(getActivity(), arrMovieDetails);

        // Step 5: Link the data adapter and the grdiview
        gridView.setAdapter(movieListAdapter);

        getMovieListing(Constants.SortCriteria.POPULARITY_DESC);

        // Step 6: Register the gridview to listen to item clicks
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

    private void getMovieListing(Constants.SortCriteria sortCriteria) {

        VolleySingleton volleySingleton;
        RequestQueue requestQueue;
        JsonObjectRequest jsonObjectRequest;

        // Step 1: Initialize the Volley and RequestQueue objects to assist with the network calls.
        volleySingleton = VolleySingleton.getSingletonInstance();
        requestQueue = volleySingleton.getRequestQueue();

        // Step 2: Generate the URL to be used for the MovieDB API call.
        String url = UrlGenerator.generateURL(sortCriteria);
        Log.d("AAK", "Fetching movie listings data ...");
        Log.d("AAK", url);

        // Step 3: Make the API call and handle any errors by providing appropriate feedback to the user.
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Populate the arraylist with parsed JSON data from the API call
                arrMovieDetails.clear();
                arrMovieDetails = parseMovieListJSONResponse(response);

                movieListAdapter.updateDataSet(arrMovieDetails);

                // Notify the data adapter that the underlying data has changed
                movieListAdapter.notifyDataSetChanged();

                // Scrolls the gridview to the top. If this is not done then the gridview remains at the current position
                // This hampers the user experience of the app since user is confused whether to scroll up or down.
                gridView.smoothScrollToPosition(0);

                Log.d("AAK", "SUCCESS: JSON Response received. " + arrMovieDetails.size() + " movies parsed.");
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

    private ArrayList<MovieItem> parseMovieListJSONResponse(JSONObject response) {

        // Step 1: Create an empty arraylist which will serve as the placeholder for the data.
        // Arraylist will comprise of MovieItem custom object mimicking fields of Movie data items returned from MovieDB API call.
        ArrayList<MovieItem> arrParsedMovieData = new ArrayList<>();

        // Step 2: Sanity check for null or zero length response.
        if ((response == null) || (response.length() == 0)) {
            Log.d("AAK", "No entries found");
            Toast.makeText(getActivity(), "** ERROR ** MovieDB Error: Movie data request has generated an empty response. Please refresh.", Toast.LENGTH_SHORT).show();
            return null;
        }

        try {

            // Step 3: Extract the movie array from the JSON response.
            JSONArray jsonMovies = response.getJSONArray(Constants.KEY_LISTRESULTS);
            JSONObject jsonMovieObject;
            MovieItem movieItem;

            // Step 4: Loop through the individual JSON movie objects
            // Save them to the MovieItem model
            for (int i = 0; i < jsonMovies.length(); i++) {
                jsonMovieObject = jsonMovies.getJSONObject(i);
                movieItem = Sanitizer.sanitizeData(jsonMovieObject);
                arrParsedMovieData.add(movieItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrParsedMovieData;
    }

    public void setCommunicator(CommunicatorMovieListing communicatorMovieListing) {
        this.communicatorMovieListing = communicatorMovieListing;
        Log.d("AAK", "MovieListing: Communicator Set");
    }

    // *************************
    // Sorting logic to sort movies listings by (popularity/ratings) in an (ASC/DESC) order respectively
    // *************************

    public void sortByPopularity(boolean sortByPopularity_DESC) {

        if (sortByPopularity_DESC) {
            Log.d("AAK", "* Sorting Most Popular to Least Popular");
            getMovieListing(Constants.SortCriteria.POPULARITY_DESC);
        } else {
            Log.d("AAK", "* Sorting Least Popular to Most Popular");
            getMovieListing(Constants.SortCriteria.POPULARITY_ASC);
        }
    }

    public void sortByRating(boolean sortByRating_DESC) {

        if (sortByRating_DESC) {
            Log.d("AAK", "* Sorting Highest Rated to Lowest Rated");
            getMovieListing(Constants.SortCriteria.RATING_DESC);
        } else {
            Log.d("AAK", "* Sorting Lowest Rated to Highest Rated");
            getMovieListing(Constants.SortCriteria.RATING_ASC);
        }
    }

    public interface CommunicatorMovieListing {
        void movieListingResponse(MovieItem m);
    }
}