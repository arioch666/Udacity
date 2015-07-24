package info.aalokkarnik.popularmovies1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import info.aalokkarnik.popularmovies1.Model.MovieItem;
import info.aalokkarnik.popularmovies1.Network.VolleySingleton;
import info.aalokkarnik.popularmovies1.R;
import info.aalokkarnik.popularmovies1.Utils.Constants;
import info.aalokkarnik.popularmovies1.Utils.UrlGenerator;

public class MovieListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovieItem> movies;

    public MovieListAdapter(Context context, ArrayList<MovieItem> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final MovieViewHolder movieViewHolder;
        final MovieNoPosterViewHolder movieNoPosterViewHolder;

        // STEP 1: get the current movie item from the data source
        MovieItem movieItem = movies.get(position);

        /*
        if (movieItem.moviePoster.equals((Constants.KEY_POSTER_DEFAULT))) {
            Log.d("AAK", "########### No poster found");
            view = getNoPosterView(view, parent, movieItem);
        } else {
            Log.d("AAK", "$$$$$$$$$$$ Normal movie item");
            view = getNormalView(view, parent, movieItem);
        }
        */

        // STEP 2: Load the MovieViewHolder object which contain actual bitmap image into the View
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.grid_item, parent, false);

            movieViewHolder = new MovieViewHolder();
            movieViewHolder.moviePoster = (ImageView) view.findViewById(R.id.imageView);

            view.setTag(movieViewHolder);

        } else {
            movieViewHolder = (MovieViewHolder) view.getTag();
        }

        // STEP 3: assign the content of the 'current movie' object to the MovieViewHolder
        VolleySingleton volleySingleton = VolleySingleton.getSingletonInstance();
        ImageLoader imageLoader = volleySingleton.getImageLoader();

        if (movieItem.moviePoster.equals(Constants.KEY_POSTER_DEFAULT)) {
            movieViewHolder.moviePoster.setImageResource(R.drawable.cross_64);
        } else {

            imageLoader.get(UrlGenerator.generateImageURL(movieItem.moviePoster), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    movieViewHolder.moviePoster.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }

        // STEP 4: return the view
        return view;
    }

    private View getNormalView(View view, ViewGroup parent, MovieItem movieItem) {
        final MovieViewHolder movieViewHolder;

        // STEP 2: Load the MovieViewHolder object which contain actual bitmap image into the View
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.grid_item, parent, false);

            movieViewHolder = new MovieViewHolder();
            movieViewHolder.moviePoster = (ImageView) view.findViewById(R.id.imageView);

            view.setTag(movieViewHolder);

        } else {
            movieViewHolder = (MovieViewHolder) view.getTag();
        }

        // STEP 3: assign the content of the 'current movie' object to the MovieViewHolder
        VolleySingleton volleySingleton = VolleySingleton.getSingletonInstance();
        ImageLoader imageLoader = volleySingleton.getImageLoader();

        imageLoader.get(UrlGenerator.generateImageURL(movieItem.moviePoster), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                movieViewHolder.moviePoster.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return view;
    }

    private View getNoPosterView(View view, ViewGroup parent, MovieItem movieItem) {
        final MovieNoPosterViewHolder movieNoPosterViewHolder;

        // STEP 2: Load the MovieViewHolder object which contain actual bitmap image into the View
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.grid_item_noposter, parent, false);

            movieNoPosterViewHolder = new MovieNoPosterViewHolder();
            movieNoPosterViewHolder.moviePoster = (ImageView) view.findViewById(R.id.imageView);
            movieNoPosterViewHolder.movieTitle = (TextView) view.findViewById(R.id.textView);

            view.setTag(movieNoPosterViewHolder);

        } else {
            movieNoPosterViewHolder = (MovieNoPosterViewHolder) view.getTag();
        }

        // STEP 3: assign the content of the 'current movie' object to the MovieViewHolder
        movieNoPosterViewHolder.moviePoster.setImageResource(R.drawable.cross_64);
        movieNoPosterViewHolder.movieTitle.setText(movieItem.movieTitle);

        return view;
    }

    public void updateDataSet(ArrayList<MovieItem> mList) {
        movies.clear();
        this.movies.addAll(mList);
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

    private static class MovieViewHolder {
        ImageView moviePoster;
    }

    private static class MovieNoPosterViewHolder {
        ImageView moviePoster;
        TextView movieTitle;
    }
}