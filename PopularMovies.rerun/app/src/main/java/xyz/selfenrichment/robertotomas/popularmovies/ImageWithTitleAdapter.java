package xyz.selfenrichment.robertotomas.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by RobertoTomás on 0019, 19/2/2016.
 *
 * Custom adapter that adds 2 things:
 *  - ArrayAdapter over the Movie type.
 *  - Picasso with scaled images in ArrayAdapter (to populate ImageViews)
 *
 */
public class ImageWithTitleAdapter extends ArrayAdapter {
    private final String LOG_TAG = ImageWithTitleAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater inflater;

    private ArrayList<Movie> mMovies;

    public ImageWithTitleAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.grid_poster, movies);

        mContext = context;
        mMovies = movies;

        inflater = LayoutInflater.from(mContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        final Movie movie = mMovies.get(pos);
        final String id = movie.getId();
        final String type_of_Uri = movie.getPoster_type();
        final String url = movie.getPoster_path();
        final String title = movie.getTitle();

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.grid_poster, parent, false);
        }

        ImageView iv = (ImageView) convertView;

        float[] screen = LayoutUtil.getScreen(mContext);
        int h = LayoutUtil.convertDPtoPixels(screen[1], mContext);
        int w = LayoutUtil.convertDPtoPixels(screen[0], mContext);
        //Log.d(LOG_TAG, "imageView dimensions: " + w + " x " + h);

        iv.setBackgroundColor(Color.LTGRAY);
        iv.getLayoutParams().height = (h/2) - 1;
        iv.getLayoutParams().width = (w/2) - 1;

        PicassoUtil.attachPoster(mContext, (ImageView) convertView, id, type_of_Uri, url, title, h, w);
        convertView.setTag(movie);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, title + " (" + id + ")", Toast.LENGTH_LONG).show();
                mContext.startActivity(
                        new Intent(getContext(), MovieDetailsActivity.class)
                                .putExtra("movie", (Movie)v.getTag() )
                );
            }
        });

        return convertView;
    }
}
