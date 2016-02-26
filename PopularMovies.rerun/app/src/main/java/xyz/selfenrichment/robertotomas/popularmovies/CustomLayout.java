package xyz.selfenrichment.robertotomas.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by RobertoTomás on 0024, 24/2/2016.
 *
 * Adds support for Picasso to add images to the background of the layout
 */
public class CustomLayout extends LinearLayout implements Target {

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        //Set your error drawable
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        //Set your placeholder
    }
}