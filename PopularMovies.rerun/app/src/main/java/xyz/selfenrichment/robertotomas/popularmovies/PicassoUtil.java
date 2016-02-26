package xyz.selfenrichment.robertotomas.popularmovies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

/**
 * Created by RobertoTomás on 0024, 24/2/2016.
 *
 * Helper functions for use with <a href="http://square.github.io/picasso/">Picasso library</a>.
 *
 */
public class PicassoUtil {
    /* Singleton Pattern */
    private static final String LOG_TAG = PicassoUtil.class.getSimpleName();

    private static PicassoUtil mInstance = null;

    private PicassoUtil(){
        mInstance = new PicassoUtil();
    }

    public static PicassoUtil getInstance() {
        if (mInstance == null) {
            Class clazz = PicassoUtil.class;
            synchronized (clazz) {
                mInstance = new PicassoUtil();
            }
        }
        return mInstance;
    }
    /* /endSingleton Pattern */

    /**
     * Fetches and attaches an image poster to a received {@link ImageView}.
     * @param context
     * @param iv
     * @param id
     * @param type_of_Uri
     * @param url
     * @param title
     * @param h
     * @param w
     *
     * Two types of image posters can be specified. Either a URL for download, or a string resource SVG.
     *
     */
    public static void attachPoster(Context context, ImageView iv, String id, String type_of_Uri, String url, String title, int h, int w) {
        switch (type_of_Uri){
            case "tmdb":
                Log.d(LOG_TAG, "loading poster for movie " + title);
                Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + url)
                        .fit().into(iv);
                break;
            case "local-svg":
                Log.d(LOG_TAG, Prefs.getString(url, "<svg></svg>"));

                SVG svg = null;
                try {
                    svg = SVG.getFromString(Prefs.getString(url, "<svg></svg>"));
                } catch (SVGParseException e) {
                    e.printStackTrace();
                }

                svg.setDocumentHeight(h);
                svg.setDocumentWidth(w);

                Log.d(LOG_TAG, String.format("SVG size: %f x %f", svg.getDocumentWidth(), svg.getDocumentHeight()));

                Drawable drawable = null;
                if (svg != null) {
                    //Log.d(LOG_TAG, "rendering SVG to drawable");
                    drawable = new PictureDrawable(svg.renderToPicture());
                }
                // disable hardware acceleration from causing svg not to render
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
                iv.setImageDrawable(drawable);
                iv.setTag(id);

                break;
        }
    }

    /**
     * Fetches and attaches an image poster to a CustomLayout
     * @param context
     * @param details_layout
     * @param id
     * @param poster_url
     * @param title
     * @param h
     * @param w
     *
     */
    public static void attachPosterToLayout(Context context, CustomLayout details_layout, String id, String poster_url, String title, int h, int w) {
        Log.d(LOG_TAG, "loading backdrop image for movie " + title);
        Picasso.with(context).load("http://image.tmdb.org/t/p/original" + poster_url)
                .into(details_layout);

    }
}
