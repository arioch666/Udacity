package info.aalokkarnik.popularmovies0.Model;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aalok Karnik on 7/14/2015.
 */
public class PopularMoviesApplication extends Application {
    private static PopularMoviesApplication popularMoviesApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        popularMoviesApplication = this;
    }

    public static PopularMoviesApplication getInstance() {
        return popularMoviesApplication;
    }

    public static Context getAppContext() {
        return popularMoviesApplication.getApplicationContext();
    }
}
