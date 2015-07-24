package info.aalokkarnik.popularmovies0.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import info.aalokkarnik.popularmovies0.Utils.Constants;
import info.aalokkarnik.popularmovies0.Model.MovieItem;
import info.aalokkarnik.popularmovies0.Network.VolleySingleton;

import info.aalokkarnik.popularmovies0.R;

/**
 * Created by Aalok Karnik on 7/12/2015.
 */
public class MovieListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovieItem> movies;

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public MovieListAdapter(Context context, ArrayList<MovieItem> movies) {
        this.context = context;
        this.movies = movies;

        this.volleySingleton = VolleySingleton.getSingletonInstance();
        this.imageLoader = volleySingleton.getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final MovieViewHolder movieViewHolder;

        // STEP 1: Load the MovieViewHolder object which contain actual bitmap image into the View
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.grid_item, parent, false);

            movieViewHolder = new MovieViewHolder();
            movieViewHolder.moviePoster = (ImageView) view.findViewById(R.id.imageView);

            view.setTag(movieViewHolder);

        } else {
            movieViewHolder = (MovieViewHolder) view.getTag();
        }

        // STEP 2: get the current movie item from the data source
        MovieItem movieItem = movies.get(position);

        // STEP 3: assign the content of the 'current movie' object to the MovieViewHolder
        imageLoader.get(Constants.MOVIEDB_POSTERURL + Constants.MOVIEDB_POSTERSIZE_500 + movieItem.moviePoster, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                movieViewHolder.moviePoster.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // STEP 4: return the view
        return view;
    }

    private static class MovieViewHolder {
        ImageView moviePoster;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public MovieItem getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}