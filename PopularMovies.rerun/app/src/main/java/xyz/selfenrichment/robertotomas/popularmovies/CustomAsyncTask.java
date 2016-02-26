package xyz.selfenrichment.robertotomas.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by RobertoTomás on 0025, 25/2/2016.
 *
 * Common method for TMDB {@link AsyncTask}'s tasks pulling API data. In general any RESTful api
 * probably could use this helper.
 */
public abstract class CustomAsyncTask<Object,Void,T> extends AsyncTask<Object,Void,T> {
    abstract String getLogTag();

    @Nullable
    protected String getJSONStringFromAPI(Uri builtUri, HttpURLConnection urlConnection, BufferedReader reader) {
        String returnString;
        try {
            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            returnString = buffer.toString();
        }catch(IOException e){
            Log.e(getLogTag(), "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(getLogTag(), "Error closing stream", e);
                }
            }
        }
        return returnString;
    }
}
