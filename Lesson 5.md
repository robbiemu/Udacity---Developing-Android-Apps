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
* `drawable` -> `drawable-v21` _for Android SDK 21 Lollipop and above_

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

### General UI features programmers must face 

#### [StateList Drawable](http://developer.android.com/guide/topics/resources/drawable-resource.html#StateList)

You can use states associated with an item like a listview to animate or visually represent change to the user. The basic path for this is:

1. save the colors, etc, that will change in `res/values` or `drawable` as appropriate (like in colors.xml)
2. ensure the listView keeps track fo the data with a `res/values/styles.xml` like:
    <resources>
        <style name="ForecastListStyle">
            <item name="android:choiceMode">singleChoice</item>
        </style>
    </resources>
_doing it with a style allows you to easily set different use cases: like if you only want the highlight in tablet mode, you would have a `styles-sw600dp.xml`._
And you would add the style as an attribute to the ListView: `style="@style/ForecastListStyle"`.
3. Provide a drawable to use as the background for selected items:
    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <!-- State when a row is being pressed, but hasn't yet been activated (finger down) -->
        <item android:state_pressed="true">
            <ripple android:color="@color/grey" /><!-- requiresSDK v21+ -->
        </item>
        <!-- When the view is "activated".  In SINGLE_CHOICE_MODE, it flags the active row
            of a ListView -->
        <item android:state_activated="true" android:drawable="@color/sunshine_light_blue" />
        <!-- Default, "just hangin' out" state. -->
        <item android:drawable="@android:color/transparent" />
    </selector>
And you would add the drawable as the background attribute to the ListView **items**: `android:background="@drawable/my_selector"`


_see also_
* [themes](http://developer.android.com/guide/topics/ui/themes.html)
* [ListView attr:choiceMode](http://developer.android.com/reference/android/widget/AbsListView.html#attr_android:choiceMode)

#### Restoring dynamic ListView position

When you rotate your tablet the activity restarts. You need to use the **Bundle** usually called `savedInstateState` in each fragment that should save their state, in order to preserve it through rotation (or going to settings and back). An example would be the listview selection, whihc may not be visible if it was far enough down in the list, once the rotation has completed.

 This is done with the recipe:
 
 1.  create a position variable, and store a handy reference to the Views/Viewgroups who need to keep state data:
    public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        private ListView mListView;
        private int mPosition = ListView.INVALID_POSITION;

        private static final String SELECTED_KEY = "selected_position";
        ...
        mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(mForecastAdapter);

2. Store the position/other data when it is set:
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            ....
            mPosition = position;
        }
    });
    ...
    if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
        // The listview probably hasn't even been populated yet.  Actually perform the
        // swapout in onLoadFinished.
        mPosition = savedInstanceState.getInt(SELECTED_KEY);
    }
3. Override `onSaveInstanceState` to save the state before the activity closes shop:
     @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }
4. and in `onLoadFinished` the listviw should be ready to recieve its previous position:
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
     }
     
A general solution that doesn't specifically take into account dynamic loader based lists is at [denevell's blog](https://blog.denevell.org/android-save-list-position-rotation-backpress.html):
##### Save ListView position after rotation or backpress
You can restore the position and state of a ListView by saving the ListView’s instance state.

You then restore it after the ListView’s adapter is next set.

###### Restoring it after rotation:

1. Save the ListView state on onSaveInstanceState:
    @Override
    public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_INSTANCE_STATE, yourListView.onSaveInstanceState());
    }
2. Then later, get that back out of the Bundle in onCreate:
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            mListInstanceState = savedInstanceState.getParcelable(LIST_INSTANCE_STATE);
        }
    }
3. Then, after the ListView’s adapter is set, you can restore the instance state:
    
###### Restoring it after a backpress:

If on after a backpresss your ListView is recreated via `onResume()`, then

Ensure you save the instance state on `onPause()` instead, or in addition, to the above.
Restore the instance state as above.

#### [Layout Aliases](http://developer.android.com/training/multiscreen/screensizes.html#TaskUseAliasFilters)

Layout aliases let you specify the same layout at different specifications. For example, in both landscape and tablets, you could write the same layout twice under `res/layout-sw600dp` and `res/layout-land`, or you can write it once under `layout` and then write references to it in those two folders. Ultimately you are _increasing_ the number of files in your code, but you now only have to maintain the one copy. 

_notes:_
* Using `res/values[-*]/refs` instead of `res/values[-*]/layout` is the norm now.
* _Don't_ create a `refs.xml` for the default value, ie `res/values/refs.xml`. That one would ahve a duplicate name setting.

#### Conditional formatting

To support different renderings at different screen segments, you commonly will know the screen format at the activity level. You want to plumb that through (or in some other way expose) to the fragments and adapters (for dynamic data) from that point. For example:

* in MainActivity:
    private boolean mTwoPane;
    protected void onCreate(Bundle savedInstanceState) {
        ...
        SomeFragment someFragment = ((SomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_some));
        someFragment.setUseWideLayout(mTwoPane); // mTwoPane has been set in onCreate just above here, reflecting two pane layouts like for landscape orientation
        ...
* then in SomeFragment:
    private boolean mTwoPane;

    public void setUseWideLayout(boolean twoPane) {
        mTwoPane = twoPane;
        if (mSomeAdapter != null){
            mSomeAdapter.setUseWideLayout(mTwoPane);
        }
    }  
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The SomeAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mSomeAdapter = new SomeAdapter(getActivity(), null, 0);
        mSomeAdapter.setUseWideLayout(mTwoPane);
        ...
* and finally in SomeAdapter:
    private boolean mUseWideLayout;
    public void setUseWideLayout(boolean useWideLayout) {
        mUseWideLayout = useWideLayout;
    }
    ...// newView and/or bindView would have conditional selections of the outer ListView, etc

#### Applying Visual Mocks
_note: here is the [Visual Mocks with Redlines for Sunshine](https://www.udacity.com/wiki/ud853/design_assets)_

_see also_

* [Android Design Guide: Metrics and Grids](https://developer.android.com/design/style/metrics-grids.html)
* Basics guide to [Styling the ActionBar](https://developer.android.com/training/basics/actionbar/styling.html)
* [displayOptions attribute](http://developer.android.com/reference/android/R.attr.html#displayOptions)
* [Android Design Guide: Action Bar](http://developer.android.com/design/patterns/actionbar.html)
* [Devloper's Guide to the Action Bar](http://developer.android.com/guide/topics/ui/actionbar.html)
* [Using a Logo Instead of an Icon](http://developer.android.com/guide/topics/ui/actionbar.html#Logo)

#### Accessibility

note: designing for visual impairment: [TalkBack](http://developer.android.com/training/accessibility/accessible-app.html#contentdesc)

You can simply use the checklist (below) to walk through design considerations before publishing the app. If you are considering your accessibility earlier than that, the android design guide on accessability (link below) is a better place to start.

_see also_
* [Documentation on Accessibility](http://developer.android.com/guide/topics/ui/accessibility/index.html) 
* [Accessibility Developer Checklist](http://developer.android.com/guide/topics/ui/accessibility/checklist.html) 
* Android Design Guide: [Accessibility](http://developer.android.com/design/patterns/accessibility.html) 
* [Accessibility Testing Checklist](http://developer.android.com/tools/testing/testing_accessibility.html)

#### Creating a custom view

Inherit from `View`. View is a nice UI base (using a Canvas) for system elements. `SurfaceView` is faster, and meant for video or games.

Provide these three constructors to support the view's creation in code, through a resource or through inflation:
    public class MyView extends View {
        public MyView (Context c){
            super(c);
        }
        public MyView (Context c, AttributeSet attrs) {
            super(c, attrs);
        } 
        public MyView (Context c, AttributeSet attrs, int DefaultStyle) {
            super(c, attrs, DefaultStyle);
        }
    }

Override the `onMeasure(w, h)` handler (method), which indicated the views size. Otherwise it will be 100x100px. For each int passed in, use `MeasureSpec`'s `getMode` to find the mode (exactly, at most, etc) and `getSize` to get the physical size to set. Call the `setMeasuredDimension` method with your heights and widths there, to actually set the displayed size.

Override `onDraw(Canvas c)` in order to provide content to view. This is called often, up to several (dozen even) times per second. Object creation and destruction is not to be handled here, instead move them to the class scope.

_see also_
* [Custom Views](http://developer.android.com/training/custom-views/index.html)
* [Canvas and Drawables](http://developer.android.com/guide/topics/graphics/2d-graphics.html)
* [Custom Drawing](http://developer.android.com/training/custom-views/custom-drawing.html) Guide

##### Adding accessibility to custom views

The simplest way is to use the `contentDescription` attr, of course. You can also do this programmatically from the view in the activity/fragment, when setting up views. The most robust way is within the view itself:

    onSomeDataInViewHasChanged(...){
        if(AccessibilityManager.getInstance(mContext).isEnabled()){
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGE);
    ...
    @Override
    public boolean dispatchPopulateAccessibilityEvent (AccessibilityEvent ev){
        ev.getText().add(somevalue);
        return true;
        
        
_see also_
* [AccessabilityEventSource](http://developer.android.com/reference/android/view/accessibility/AccessibilityEventSource.html)

##### Miscellaneous Interactivity additions to custom views

* [Motion Event Docs](http://developer.android.com/reference/android/view/MotionEvent.html)
* [Making Custom View Interactive](http://developer.android.com/training/custom-views/making-interactive.html)