package xyz.selfenrichment.robertotomas.popularmovies;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MovieDetailsActivity extends AppCompatActivity {
    private final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Movie movie = getIntent().getParcelableExtra("movie");

        //if (getIntent().hasExtra("movie")) {
        //    Toast.makeText(this, movie[3], Toast.LENGTH_SHORT).show();
        //}

        ImageView iv = (ImageView) findViewById(R.id.imagev_movie_title);

        float[] screen = LayoutUtil.getScreen(this);
        int h = LayoutUtil.convertDPtoPixels(screen[1], this);
        int w = LayoutUtil.convertDPtoPixels(screen[0], this);
        //Log.d(LOG_TAG, "imageView dimensions: " + w + " x " + h);

        iv.setBackgroundColor(Color.LTGRAY);
        iv.getLayoutParams().height = (h/2) - 1;
        iv.getLayoutParams().width = (w/2) - 1;

        PicassoUtil.attachPoster(this, iv, movie.getId(), movie.getPoster_type(), movie.getPoster_path(), movie.getTitle(), h, w);

        if (!movie.getBackdrop_path().contentEquals("null")) {
            CustomLayout details_layout = (CustomLayout)findViewById(R.id.movie_details_custom_layout);
            PicassoUtil.attachPosterToLayout(this, details_layout, movie.getId(), movie.getBackdrop_path(), movie.getTitle(), h, w);
        }

        TextView tv = (TextView) findViewById(R.id.textv_movie_title);
        tv.setText(movie.getTitle());

        tv = (TextView)findViewById(R.id.textv_vote_average);
        tv.setText(movie.getVote_average());
        Float avg_rating = Float.valueOf(movie.getVote_average());
        if (avg_rating > 6.5) {
            tv.setTextColor(getResources().getColor(R.color.colorAccent, getTheme())); // color Accent
        } else if (avg_rating > 5) {
            tv.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));// color Primary
        } else if (avg_rating > 4) {
            tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));// color PrimaryDark
        }

        tv = (TextView)findViewById(R.id.tv_movie_description);
        tv.setText(movie.getOverview());

        tv = (TextView)findViewById(R.id.textv_movie_release_date);
        SimpleDateFormat fdf = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            tv.setText(df.format(fdf.parse(movie.getRelease_date())));
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }

        TextView textView = (TextView) findViewById(R.id.textv_movie_genres);
        new TMDB_Async.GetGenres(textView).execute(this, movie.getId());


/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
