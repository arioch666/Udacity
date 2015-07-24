package info.aalokkarnik.popularmovies0.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import info.aalokkarnik.popularmovies0.Utils.Constants;
import info.aalokkarnik.popularmovies0.Model.MovieItem;
import info.aalokkarnik.popularmovies0.Network.VolleySingleton;

import info.aalokkarnik.popularmovies0.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private CommunicatorMovieDetail communicatorMovieDetail;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        MovieItem m = getCurrentMovieDetails(this.getArguments());

        Log.d("AAK", "MovieDetail: Populating Layout for " + m.movieTitle);
        populatePlaceHolders(view, m);

        return view;
    }

    private MovieItem getCurrentMovieDetails(Bundle bundle) {

        ArrayList<MovieItem> mList = bundle.getParcelableArrayList(Constants.CURRENT_MOVIE);
        return mList.get(0);
    }

    private void populatePlaceHolders(View view, MovieItem m) {

        VolleySingleton volleySingleton = VolleySingleton.getSingletonInstance();
        ImageLoader imageLoader = volleySingleton.getImageLoader();

        // Identify the layout items and populate appropriate values
        TextView txtMovieTitle = (TextView) view.findViewById(R.id.movieDetailTitle);
        txtMovieTitle.setText(m.movieTitle);

        TextView txtMovieSynopsis = (TextView) view.findViewById(R.id.movieDetailSynopsis);
        txtMovieSynopsis.setText(m.movieSynopsis);

        TextView txtMovieRating = (TextView) view.findViewById(R.id.movieDetailRating);
        txtMovieRating.setText(Float.toString(m.movieRating));

        TextView txtMoviePopularity = (TextView) view.findViewById(R.id.movieDetailPopularity);
        txtMoviePopularity.setText(Float.toString(m.moviePopularity));

        // Extract the year from the movie release date in YYYY-MM-DD format
        TextView txtMovieReleaseYear = (TextView) view.findViewById(R.id.movieDetailReleaseYear);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        txtMovieReleaseYear.setText(simpleDateFormat.format(m.movieReleaseDate));

        final ImageView imgVwMoviePoster = (ImageView) view.findViewById(R.id.movieDetailPoster);
        imageLoader.get(Constants.MOVIEDB_POSTERURL + Constants.MOVIEDB_POSTERSIZE_500 + m.moviePoster, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imgVwMoviePoster.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void setCommunicator(CommunicatorMovieDetail communicatorMovieDetail) {
        this.communicatorMovieDetail = communicatorMovieDetail;
        Log.d("AAK", "MovieDetail: Communicator Set");
    }

    public interface CommunicatorMovieDetail {
        void movieDetailResponse(String message);
    }
}