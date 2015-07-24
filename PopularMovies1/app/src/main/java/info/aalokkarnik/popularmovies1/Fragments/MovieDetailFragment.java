package info.aalokkarnik.popularmovies1.Fragments;

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

import info.aalokkarnik.popularmovies1.Utils.Constants;
import info.aalokkarnik.popularmovies1.Model.MovieItem;
import info.aalokkarnik.popularmovies1.Network.VolleySingleton;

import info.aalokkarnik.popularmovies1.R;
import info.aalokkarnik.popularmovies1.Utils.UrlGenerator;

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

        // * Movie Title
        TextView txtMovieTitle = (TextView) view.findViewById(R.id.movieDetailTitle);
        txtMovieTitle.setText(m.movieTitle);

        // * Movie Release Date
        TextView txtMovieReleaseYear = (TextView) view.findViewById(R.id.movieDetailReleaseYear);
        txtMovieReleaseYear.setText(new SimpleDateFormat("yyyy").format(m.movieReleaseDate));

        // * Movie Poster
        final ImageView imgVwMoviePoster = (ImageView) view.findViewById(R.id.movieDetailPoster);
        if (m.moviePoster.equals(Constants.KEY_POSTER_DEFAULT)) {
            imgVwMoviePoster.setImageResource(R.drawable.cross_64);
        } else {
            imageLoader.get(UrlGenerator.generateImageURL(m.moviePoster), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imgVwMoviePoster.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }

        // * Movie Synopsis
        TextView txtMovieSynopsis = (TextView) view.findViewById(R.id.movieDetailSynopsis);
        txtMovieSynopsis.setText(m.movieSynopsis);

        // * Movie Rating Score
        TextView txtMovieRating = (TextView) view.findViewById(R.id.movieDetailRating);
        txtMovieRating.setText(Float.toString(m.movieRating));

        // * Movie Popularity Score
        TextView txtMoviePopularity = (TextView) view.findViewById(R.id.movieDetailPopularity);
        txtMoviePopularity.setText(Float.toString(m.moviePopularity));
    }

    public void setCommunicator(CommunicatorMovieDetail communicatorMovieDetail) {
        this.communicatorMovieDetail = communicatorMovieDetail;
        Log.d("AAK", "MovieDetail: Communicator Set");
    }

    public interface CommunicatorMovieDetail {
        void movieDetailResponse(String message);
    }
}