package xyz.selfenrichment.robertotomas.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This singleton holds the async calls to get data from tmdb.
 */
public class TMDB_Async {
    /* Singleton Pattern */
    private static TMDB_Async mInstance = null;

    private TMDB_Async(){
        mInstance = new TMDB_Async();
    }

    public static TMDB_Async getInstance() {
        if (mInstance == null) {
            Class clazz = TMDB_Async.class;
            synchronized (clazz) {
                mInstance = new TMDB_Async();
            }
        }
        return mInstance;
    }
    /* /end Singleton Pattern */

    /**
     *  An {@link AsyncTask} for movie data for MovieDetailsActivity
     */
    public static class GetGenres extends CustomAsyncTask<Object, Void, String> {
        private final String LOG_TAG = GetGenres.class.getSimpleName();

        String getLogTag(){
            return LOG_TAG;
        }

        private Context mContext;
        private TextView mTextView;

        public GetGenres(TextView textView) {
            mTextView = textView;
        }

        @Override
        protected String doInBackground(Object... params) {
            mContext = (Context) params[0];
            String id = (String) params[1];

            String resultsJSON = getGenresOfMovieJSON(id);
            try {
                return getGenresOfMoviesFromJSON(resultsJSON);
            } catch (JSONException e) {
                Log.e(getLogTag(), e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String genres) {
            mTextView.setText(genres);
        }

        private String getGenresOfMovieJSON(String id) {
            Uri builtUri = Uri.parse(mContext.getString(R.string.tmdb_mg_BASE_URL)).buildUpon()
                    .appendPath(id)
                    .appendQueryParameter("api_key", BuildConfig.API_KEY_THEMOVIEDB)
                    .build();

            Log.d(LOG_TAG, builtUri.toString());

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String returnString = getJSONStringFromAPI(builtUri, urlConnection, reader);

            return (returnString == null)? null: returnString;
        }

        private String getGenresOfMoviesFromJSON(String json_str) throws JSONException {
            JSONObject movieGenresJson = new JSONObject(json_str);
            JSONArray movieGenresArray = movieGenresJson.getJSONArray("genres");

            List<String> resultGenres = new ArrayList<>();
            for (int i = 0; i < movieGenresArray.length(); i++) {
                JSONObject genre = movieGenresArray.getJSONObject(i);
//            String id = genre.getString("id");
                String name = genre.getString("name");

                resultGenres.add(name);
//            Log.v(LOG_TAG, String.format("Movie genre found: id: %s, name: %s", id, name);
            }

            StringBuilder builder = new StringBuilder();
            Boolean firstpass = true;
            for (String value : resultGenres) {
                builder.append(firstpass?value:", "+value);
                firstpass = false;
            }
            String genres = builder.toString();

            return genres;
        }
    }

    /**
     * Created by RobertoTomás on 0019, 19/2/2016.
     * An {@link AsyncTask} that queries the Movie DB and populates a passed-in arrayadapter
     * with the results. This is designed for the PosterBoardActivity
     * <p/>
     * see [themovieDP API](https://www.themoviedb.org/documentation/api)
     */
    public static class GetMovies extends CustomAsyncTask<Object, Void, Movie[]> {
        private final String LOG_TAG = GetMovies.class.getSimpleName();

        String getLogTag(){
            return LOG_TAG;
        }

        private Context mContext;
        private ArrayAdapter<Movie> mAdapter;

        /**
         * doInBackground
         * @param params
         * @return
         * called from an instance of the class with `instance.execute(Context c);`, returns a
         * String[][] array of movies from the json results, with the elements of the inner array:
         * _id_, _poster-path_, and _title_
         *
         * see {@link AsyncTask}
         */
        @Override
        protected Movie[] doInBackground(Object... params) {
            if (params.length == 0) {
                return null;
            }
            mContext = (Context) params[0];
            mAdapter = (ImageWithTitleAdapter) params[1];

            String resultsJSON = getMovies();
            try {
                return getMoviesFromJSON(resultsJSON, mContext.getResources().getInteger(R.integer.pref_tmdb_results_num_movies));
            } catch (JSONException e) {
                Log.e(getLogTag(), e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        /**
         * getMoviesFromJSON
         * @param json_str
         * @param num_movies
         * @return
         * @throws JSONException
         * composites the results from the JSON string into a String[][] array to be returned from this AsyncTask.
         */
        private Movie [] getMoviesFromJSON(String json_str, int num_movies) throws JSONException {
            JSONObject moviesJson = new JSONObject(json_str);
            JSONArray moviesArray = moviesJson.getJSONArray("results");

            Movie[] resultMovies = new Movie[num_movies];
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieJSON = moviesArray.getJSONObject(i);
                Map<String,String> mapMovie = new HashMap<>();
                mapMovie.put("id", movieJSON.getString("id"));
                mapMovie.put("poster_path", movieJSON.getString("poster_path"));
                mapMovie.put("poster_type", null);
                mapMovie.put("title", movieJSON.getString("title"));
                mapMovie.put("vote_average", movieJSON.getString("vote_average"));
                mapMovie.put("backdrop_path", movieJSON.getString("backdrop_path"));
                mapMovie.put("overview", movieJSON.getString("overview"));
                mapMovie.put("release_date", movieJSON.getString("release_date"));

                Movie movie = new Movie(mapMovie);

                if (movieJSON.getString("poster_path").equals("null")) {
                    PosterSVG psvg;
                    if (!(movieJSON.getString("title").equals("null"))) {
                        psvg = new PosterSVG(movieJSON.getString("title"));
                    }else{
                        Log.e(LOG_TAG,"Movie with no title and no poster in results from themoviedb");
                        psvg = new PosterSVG("themoviedb error");
                    }


                    Prefs.putString(movieJSON.getString("id"), psvg.toString());
                    movie.setPoster_path(movieJSON.getString("id"));
                    movie.setPoster_type("local-svg");
                }else{
                    movie.setPoster_type("tmdb");
                }

                resultMovies[i] = movie;
                Log.d(LOG_TAG, String.format("id: %s, poster_path: %s, title: %s, avg.vote: %s, backdrop_path: %s, and %s overview",
                        movieJSON.getString("id"),
                        movie.getPoster_type() + "://" + movieJSON.getString("poster_path"),
                        movieJSON.getString("title"),
                        movieJSON.getString("vote_average"),
                        movieJSON.getString("backdrop_path"),
                        ((movieJSON.getString("overview")=="null")?"no":"an") ));

            }
            return resultMovies;
        }

        /**
         * getMovies
         * @return
         * Handles the request to themoviedb and returns the JSON string.
         */
        private String getMovies(){
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(mContext);

            // I'm using `pref_text_tmdb_sort_dir_asc` instead of hard coding it (ie, "asc") like the
            // other parameters because the value once set comes from these fields.
            // We should only define the values in one place.
            String sort_by = SP.getString("pref_tmdb_bp_query_sort_by",
                    mContext.getString(R.string.pref_tmdb_defaults_sort_by)) +
                    "." +
                    (SP.getBoolean("pref_tmdb_bp_query_sort_dir", mContext.getResources().getBoolean(R.bool.pref_tmdb_defaults_sort_dir))?
                            "desc":
                            "asc");

        /*Log.d(LOG_TAG, "Sort dir default: " +
                 mContext.getString(R.bool.pref_tmdb_defaults_sort_dir) + " => " +
                (mContext.getResources().getBoolean(R.bool.pref_tmdb_defaults_sort_dir)?
                        "desc":
                        "asc")
        );*/

            Uri builtUri = Uri.parse(mContext.getString(R.string.tmdb_bp_BASE_URL)).buildUpon()
                    .appendQueryParameter("api_key", BuildConfig.API_KEY_THEMOVIEDB)
                    .appendQueryParameter("sort_by", sort_by)
                    .build();

            Log.d(LOG_TAG, builtUri.toString());

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String returnString = getJSONStringFromAPI(builtUri, urlConnection, reader);

        /*int maxLogSize = 1000;
        for(int i = 0; i <= returnString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > returnString.length() ? returnString.length() : end;
            Log.v(LOG_TAG, returnString.substring(start, end));
        }*/

            return returnString;
        }

        /**
         * {@inheritDoc}
         *
         * In this case, we are adding the Movie objects to the adapter
         */
        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies != null) {
                this.mAdapter.clear();
                for(Movie movie : movies) {
                    this.mAdapter.add(movie);
                }
            }
        }

    }
}