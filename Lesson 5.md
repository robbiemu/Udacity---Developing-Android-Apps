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
  