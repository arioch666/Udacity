package info.aalokkarnik.popularmovies0.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import info.aalokkarnik.popularmovies0.Model.MovieItem;
import info.aalokkarnik.popularmovies0.Network.VolleySingleton;
import info.aalokkarnik.popularmovies0.Utils.Constants;

import info.aalokkarnik.popularmovies0.R;

/**
 * Created by Aalok Karnik on 7/15/2015.
 */
public class MovieDetailRecyclerAdapter extends RecyclerView.Adapter<MovieDetailRecyclerAdapter.MovieDetailViewHolder> {

    private ArrayList<MovieItem> movieList = new ArrayList<>();

    private LayoutInflater layoutInflater;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public MovieDetailRecyclerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);

        volleySingleton = VolleySingleton.getSingletonInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMovieList(ArrayList<MovieItem> movieList) {
        this.movieList = movieList;
        notifyItemRangeChanged(0, movieList.size());
    }

    @Override
    public MovieDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.grid_item, parent, false);
        MovieDetailViewHolder movieDetailViewHolder = new MovieDetailViewHolder(view);

        return movieDetailViewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieDetailViewHolder holder, int position) {
        MovieItem currentMovie = movieList.get(position);

        holder.movieTitle.setText(currentMovie.movieTitle);
        holder.moviewReleaseDate.setText(dateFormat.format(currentMovie.movieReleaseDate));
        holder.movieRating.setText(""+currentMovie.movieRating);
        holder.movieSynopsis.setText(currentMovie.movieSynopsis);

        String moviePoster = currentMovie.moviePoster;
        imageLoader.get(Constants.MOVIEDB_POSTERURL + Constants.MOVIEDB_POSTERSIZE_500 + moviePoster, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.moviePoster.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                holder.moviePoster.setImageResource(R.drawable.navigation_cancel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieDetailViewHolder extends RecyclerView.ViewHolder {

        private TextView movieTitle;
        private TextView movieSynopsis;
        private TextView movieRating;
        private TextView moviewReleaseDate;
        private ImageView moviePoster;

        public MovieDetailViewHolder(View view) {
            super(view);

            movieTitle = (TextView) view.findViewById(R.id.movieDetailTitle);
            movieSynopsis = (TextView) view.findViewById(R.id.movieDetailSynopsis);
            movieRating = (TextView) view.findViewById(R.id.movieDetailRating);
            moviewReleaseDate = (TextView) view.findViewById(R.id.movieDetailReleaseYear);
            moviePoster = (ImageView) view.findViewById(R.id.movieDetailPoster);

        }
    }
}
