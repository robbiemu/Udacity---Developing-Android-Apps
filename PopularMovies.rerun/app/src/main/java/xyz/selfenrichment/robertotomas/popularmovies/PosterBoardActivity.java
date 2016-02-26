package xyz.selfenrichment.robertotomas.popularmovies;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

public class PosterBoardActivity extends AppCompatActivity {
    private final String LOG_TAG = PosterBoardActivity.class.getSimpleName();

    private TMDB_Async.GetMovies mTMDB_task;

    public GridView posterGrid;
    public ImageWithTitleAdapter mAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_board);

        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                view.getContext().startActivity(
                        new Intent(view.getContext(), SettingsActivity.class)
                                .putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, "xyz.selfenrichment.robertotomas.popularmovies.SettingsActivity$TMDBPreferenceFragment")
                                .putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true)
                );
            }
        });

        mAdapter = new ImageWithTitleAdapter(this, new ArrayList<Movie>());

        posterGrid = (GridView) findViewById(R.id.posters_grid);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            posterGrid.setNestedScrollingEnabled(true);
        }

        posterGrid.setAdapter(mAdapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();
        update_posters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poster_board, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(
                    new Intent(this, SettingsActivity.class)
                            .putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, "xyz.selfenrichment.robertotomas.popularmovies.SettingsActivity$TMDBPreferenceFragment" )
                            .putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true)
            );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void update_posters(){
        mTMDB_task = new TMDB_Async.GetMovies();
        mTMDB_task.execute(this, mAdapter);
    }
}
