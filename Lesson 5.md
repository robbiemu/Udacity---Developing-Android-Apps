# [Developing Android Apps](https://www.udacity.com/course/progress#!/c-ud853)
@ [Udacity](https://www.udacity.com)
_via [Springboard](http://www.springboard.com)'s [Android App Development](https://www.springboard.com/learning-paths/android/ path
## Lesson 5

### UX/Design

Designing apps for UX (user experience) and visual appeal is important. Users make decisions about apps wihtin the first 30 seconds.

* [Material Design for Android](https://developer.android.com/design/material/index.html)
* [Material Design specifications](http://www.google.com/design/spec/material-design/introduction.html#)

— for a fuller listing see [Design Building Blocks](http://www.androiddocs.com/design/building-blocks/index.html)

### Adapting the Adapter to more complex views

So far we've used an array or cursor adapter to bind data to a list view with just a simple TextView. To bind it to more complicated visual rows, you need to replace the xml for that with the appropriate XML, ensuring to give each item in the view its own `id` if it will accept data from the cursor. Then, change the `bindView` method in your adapter class to apply different columns (from the projection) to different elements in the layout. For example:

    public void bindView(View view, Context context, Cursor cursor) {
        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID); // this int is a predefined projection for our query

        // Use placeholder image for now
        ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon); // this id is in the xml layout
        iconView.setImageResource(Utility.getImageResource(weatherId)); // this will ultimately be a R.drawable._id_ that was already defined for the icon for the weather

* see more about: ViewTypes
* ViewHolder — and see:
  * [Making ListView Scrolling Smooth - with a ViewHolder](http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder))
  * [Adding a contect Badge to a Listview](http://developer.android.com/training/contacts-provider/display-contact-badge.html#ListView)
* string localization: 
  * [xliff tags](https://developer.android.com/distribute/tools/localization-checklist.html)
  * [Formatting and styling](http://developer.android.com/guide/topics/resources/string-resource.html#FormattingAndStyling)
  * [context.getString()](http://developer.android.com/reference/android/content/Context.html#getString(int,) method

### Note on complex layouts
Complex layouts can be expensive. The guideline they give is to go shallow: "More siblings and less children". A rule-of-thumb might be "no more than roughly 80 total views, about 10 per view".

You can inspect your layout hierarchy with the [Hierarchy Viewer](http://developer.android.com/tools/help/hierarchy-viewer.html).

_see also [Optimizing your UI](http://developer.android.com/tools/debugging/debugging-ui.html), and [Lint](http://developer.android.com/tools/help/lint.html)._

### Responsive design
Responsive design is ensuring that your design will look good on devices with [different display characteristics](http://developer.android.com/guide/practices/screens_support.html#ConfigurationExamples). Particularly, you want to consider tablet vs (portrait mode) phone displays.

Common accepted solutions to too wide a space include narrowing content, and loading detail pages on the right side of the main activity rather than in asecond activity.

It is important to consider other screen formats early, because the types of devices you want to support may impart significant changes across designs.

_see also_
* [Tablet app quality](http://developer.android.com/distribute/essentials/quality/tablets.html)
* Android Design Guide: [Multi-Pane Layouts](https://developer.android.com/design/patterns/multi-pane-layouts.html)
* Android Design in Action: [Responsive Design (youtube video)](https://www.youtube.com/watch?v=zHirwKGEfoE)

#### Developing for multiple devices

The `res` folders and files can have specifications for different targets. This does not only differentiate screens, but other qualities like languages:

_examples of specific-target folders_
*  `values` -> `values-fr` _for french language_
* `drawable` -> `drawable-xxxhdpi` _for high density devices_
* `layout` -> `layout-land` _for landscape orientation_
* **important** `layout` -> `layout-sw720dp` _for smallest width supportable with this layout in dp_
* `layout` -> `layout-desk` _for docked devices_
* `layout` -> `laout-stylus` _for specific input devices -- is there one for mouse?_

_see also_
* [supporting multiple screens](http://developer.android.com/guide/practices/screens_support.html)
* utlitiy function: [converting dp units to pixel units](http://developer.android.com/guide/practices/screens_support.html#dips-pels)

Resources for consideration of what device layout specifications your app should be developed to support
* [Configuration Examples](http://developer.android.com/guide/practices/screens_support.html#ConfigurationExamples)
* [Metrics and grids](https://www.google.com/design/spec/layout/metrics-keylines.html#metrics-keylines-keylines-spacing)
* [Devices and Displays](https://developer.android.com/design/style/devices-displays.html)
* [Building a Flexible UI](http://developer.android.com/training/basics/fragments/fragment-ui.html)

### Master-Detail Flow design pattern

The basic means of adding a fragment to an activity is to replace a container with the fragment, like this:

    FragmentManager fm = getFragmentManager();
    Fragment myFragment = new MyFragment(); // the fragment class asociated to the layout
    String tag = null; // tags are mostly used for framgents without a UI
    FragmentTransaction ft = fm.beginTransaction();
    ft.replace(containerID, myFragment);
    ft.addToBackStack(tag);
    ft.commit();

To _design_ a non-UI fragment (which are run during the activity, but never need a UI):

* _in the Fragment's_ `onCreate`: `setRetainInstance(true);`
* _in the fragments's_ `onCreateView`: `return null`

You would then create two activities under `layout` and `layout-600dp` or some other subset of display types. In the activity for the non-master-detail-flow variant, you would have a separate detail activity, that need not be changed. In the main activity class (this could actually be two different classes since we ahve 2 different activities), you need to conditionally load the detail fragment in the right-side container (a `FrameLayout`). Since this will have the framelayout, detecting that feature is a good way to do it:

_in onCreate_
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();

In the _master view_ (the list view that populates details to the detail view), instead of using an intent to launch a new activity, you need to change the fragment loaded in the master-detail-view:

1. put this Callback interface from the gist in ForecastFragment, and implement it in MainActivity:
    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }
2. in the listView's `onItemClickListener` (likely set up in ForecastFragment, rendered from the CursorLoader), replace the intent call with a call to `onItemSelected` from the Callback implementation in MainActivity (hint: you might want to store pointer to the activity in the method in which a Fragment is associated with its activity, for reuse here).
3. in the MainActivity, override the `onItemSelected` callback using a property set to flag which state (twopane or single pane) the device is rendering in. Render an intent if it is false, but use a fragment transaction to add the fragment to the container in the current layout if true. Use a Bundle to pass to `onCreateLoader` in ForecastFragment, to pass in the URI in this case.
4. Use this code in other activities, such as the settings activity, to return back to the main activity rather than restarting it when navigating up:
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

_see also_
* [Fragments](http://developer.android.com/guide/components/fragments.html)

### StateListDrawable
