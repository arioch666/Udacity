package info.aalokkarnik.popularmovies0;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import info.aalokkarnik.popularmovies0.Fragments.MovieDetailFragment;
import info.aalokkarnik.popularmovies0.Fragments.MovieListingFragment;
import info.aalokkarnik.popularmovies0.Fragments.MovieListingFragment.CommunicatorMovieListing;
import info.aalokkarnik.popularmovies0.Model.MovieItem;
import info.aalokkarnik.popularmovies0.Utils.Constants;

public class HomeActivity extends AppCompatActivity implements CommunicatorMovieListing, MovieDetailFragment.CommunicatorMovieDetail {

    private FragmentManager fragmentManager;
    private MovieListingFragment movieListingFragment;
    private MovieDetailFragment movieDetailFragment;

    private boolean sortByPopularity_DESC, sortByRating_DESC;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the sorting criteria prior to app-pause / screen-rotation
        if (outState == null)
            outState = new Bundle();

        outState.putBoolean("sortByPopularity_DESC", sortByPopularity_DESC);
        outState.putBoolean("sortByRating_DESC", sortByRating_DESC);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d("AAK", "======================================");
        Log.d("AAK", "----------- NEW RUN : HOME -----------");

        // Retrieve the last sorting criteria prior to screen-rotation / default-values
        if (savedInstanceState == null) {

            sortByPopularity_DESC = true;
            sortByRating_DESC = true;

        } else {

            sortByPopularity_DESC = savedInstanceState.getBoolean("sortByPopularity_DESC");
            sortByRating_DESC = savedInstanceState.getBoolean("sortByRating_DESC");

        }

        // Initialize the fragments
        fragmentManager = getFragmentManager();

        movieListingFragment = new MovieListingFragment();
        movieDetailFragment = new MovieDetailFragment();

        movieListingFragment.setCommunicator(this);
        movieDetailFragment.setCommunicator(this);

        if (savedInstanceState == null) {
            // This is done so that subsequent screen rotations do not keep adding fragments to the app.
            // Previous fragment view is reused
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, movieListingFragment, "TAG_MOVIELISTING_FRAGMENT");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void movieListingResponse(MovieItem m) {

        ArrayList<MovieItem> mList = new ArrayList<>();
        mList.add(m);

        Bundle savedInstanceState = movieDetailFragment.getArguments();
        if (savedInstanceState == null) {
            savedInstanceState = new Bundle();
        }

        savedInstanceState.putParcelableArrayList(Constants.CURRENT_MOVIE, mList);

        movieDetailFragment.setArguments(savedInstanceState);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(movieListingFragment.getTag());

        // call to replace has been replaced with add.
        // .replace causes the MovieListing fragment to somehow cause the bundle to be null.
        // When the back button is pressed the bundle is null which results in a network call being made.
        // This is inefficient behaviour
        // fragmentTransaction.replace(R.id.container, movieDetailFragment);

        fragmentTransaction.hide(movieListingFragment);
        fragmentTransaction.add(R.id.container, movieDetailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void movieDetailResponse(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        Log.d("AAK", "HomeActivity: onCreateOptionsMenu");

        changeMenuItemTitle(menu.findItem(R.id.action_sortByMoviePopularity_DESC));
        changeMenuItemTitle(menu.findItem(R.id.action_sortByMovieRating_DESC));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Sorting criteria is
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sortByMoviePopularity_DESC: {

                sortByPopularity_DESC = !sortByPopularity_DESC;
                changeMenuItemTitle(item);
                movieListingFragment.sortByPopularity(sortByPopularity_DESC);

                return true;

            }
            case R.id.action_sortByMovieRating_DESC: {

                sortByRating_DESC = !sortByRating_DESC;
                changeMenuItemTitle(item);
                movieListingFragment.sortByRating(sortByRating_DESC);

                return true;

            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    // Changes the menu item title to indicate to the user sorting order to be used
    private void changeMenuItemTitle(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_sortByMoviePopularity_DESC: {
                menuItem.setTitle((sortByPopularity_DESC) ? "Least Popular first" : "Most Popular first");
                break;

            }
            case R.id.action_sortByMovieRating_DESC: {
                menuItem.setTitle((sortByRating_DESC) ? "Lowest Rating first" : "Highest Rating first");
                break;

            }
        }
    }
}
