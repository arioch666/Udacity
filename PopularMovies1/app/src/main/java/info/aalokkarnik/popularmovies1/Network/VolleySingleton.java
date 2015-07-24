package info.aalokkarnik.popularmovies1.Network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import info.aalokkarnik.popularmovies1.Model.PopularMoviesApplication;

/**
 * Created by Aalok Karnik on 7/14/2015.
 */
public class VolleySingleton {
    private static VolleySingleton volleySingletonInstance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleySingleton() {
        this.requestQueue = Volley.newRequestQueue(PopularMoviesApplication.getAppContext());
        this.imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            // Lru Cache sizse is under debate for efficiency. Depends on usage

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static VolleySingleton getSingletonInstance() {
        if (volleySingletonInstance == null) volleySingletonInstance = new VolleySingleton();

        return volleySingletonInstance;
    }

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }

    public ImageLoader getImageLoader() {
        return this.imageLoader;
    }
}
