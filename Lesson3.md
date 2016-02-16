# [Developing Android Apps](https://www.udacity.com/course/progress#!/c-ud853)
@ [Udacity](https://www.udacity.com)
_via [Springboard](http://www.springboard.com)'s [Android App Development](https://www.springboard.com/learning-paths/android/ path
## Lesson 3

### UX mock-up

Although only breifly touched upon, they do in fact mock up the intended interface when planning to take the course app project "Sunshine" to the next level. Here we will add a detail page for each item.. which at first can just repeat the same item in the list but later we can do a lot of fun gui stuff with it.

The UX design shows the current listView activity, and a new details fragment. He illustrated the click even as well.

### Implmenting a click in [ListView](http://developer.android.com/reference/android/widget/ListView.html)

`ListView` is a descendant of `AbsListView`, which has a "processItemClick" method which calls `onItemClickLinstener` to handle the click event. It is from here that sound, etc, are generated. 

### Using [toast](http://developer.android.com/guide/topics/ui/notifiers/toasts.html#Basics) to debug

Toasts are modal popup that display some information for a short while, and are a finde way to debug the passing of information in the UI.

### [Creating a new activity within a project](http://developer.android.com/training/basics/firstapp/starting-activity.html#CreateActivity)

To add a new interaction page to the app, you are addding essentially a completely new activity. from the `java` folder, you can select new->activity -> blank activity. Add the main activity as the parent in order for the navigation features to work, and use the project's package name.

This will create a layout XML file, and a new activity java file.

### [Intents](http://developer.android.com/guide/components/intents-common.html)

When switching activities within an app, you call `startActivity`. You do not dirctly pass the class that you are handing off control to, instead you use something called intents. Intents can explicitly name a class, or not. Ones that do are called _explicit intents_. By comparison, `implicit intents` pass an action and data (such as a URI), to a the system, to locate apps that have registered to handle this intent. This is commonly done to view web pages, dial an number, or working with contact data.

Calls to `intent` can pass a value with `putExtra(valueTag, value)`. This is then received by the `activity` or one of it's `fragments` like so:

    Intent intent = getActivity().getIntent();
    if (intent != null && intent.hasExtra(valueTag)) {
        String value = intent.getStringExtra(valueTag);
        // do stuff
    }


#### [ShareAction](http://developer.android.com/training/sharing/shareaction.html)

You can use an `ActionProvider` to manage the appearance and function of menu items for you, in order to provide a sense of integration into the external app.

You decalre the item in the appropriate `menu.xml` like:

    <item
            android:id="@+id/menu_item_share"
            android:showAsAction="ifRoom"
            android:title="Share"
            android:actionProviderClass=
                "android.widget.ShareActionProvider" />
                
And then initialize it in the `fragment` or `activity` with:

    private ShareActionProvider mShareActionProvider;

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);

        // - ShareAction
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider(); // MenuItemCompat.getActionProvider(item) for v7 compat apps

        String shareString = ((TextView) findViewById(R.id.TEXT_IN_APP)).getText() + " #TEXT_MESSAGE";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);

        if(mShareActionProvider !=null){
    //      Log.d(DetailActivity.class.getSimpleName(), "INTO share Provider--" + mShareActionProvider.toString());
            mShareActionProvider.setShareIntent(shareIntent);
        }
        else{
    //      Log.d(DetailActivity.class.getSimpleName(), "NO share Provider--");
        }


    
### [Settings](http://developer.android.com/guide/topics/ui/settings.html)

Preferences can be rendered for you by the system. It will create the UI necessary for the preferences specified. Each preference is stored in the [Shared Preferences](http://developer.android.com/reference/android/content/SharedPreferences.html) file for you, in k/v pairs.

The steps to adding preferences to your app are:

1. ensure there is a `res/xml` folder. If you need to create it, make it of type xml data.
2. create a `pref_general.xml` file to specify the prefs the system should crate a panel for. The [Defining Prefs](https://developer.android.com/guide/topics/ui/settings.html#DefiningPrefs) documenttion helps spell this out. _Ensure that values are not hard coded in, but instead coded in to `strings`._
3. set up a `SettingsActivity` extending `PreferenceActivity` to View the settings.

    package com.example.android.sunshine.app;

    import android.os.Bundle;
    import android.preference.ListPreference;
    import android.preference.Preference;
    import android.preference.PreferenceActivity;
    import android.preference.PreferenceManager;

    /**
    * A {@link PreferenceActivity} that presents a set of application settings.
    * <p>
    * See <a href="http://developer.android.com/design/patterns/settings.html">
    * Android Design: Settings</a> for design guidelines and the <a
    * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
    * API Guide</a> for more information on developing a Settings UI.
    */
    public class SettingsActivity extends PreferenceActivity
            implements Preference.OnPreferenceChangeListener {


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_general);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));

            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        }

        /**
        * Attaches a listener so the summary is always updated with the preference value.
        * Also fires the listener once, to initialize the summary (so it shows up before the value
        * is changed.)
        */
        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(this);

            // Trigger the listener immediately with the preference's
            // current value.
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            } else {
                // For other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }

    }

4. change your `ActionBarActivities` to bring up the settings activity, like so:

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
5. Add code like the following to a `fragment` or `activity` to retreive a value:

    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
    String location = SP.getString(getString(R.string.KEY), getString(R.string.KEY_DEFAULT_VALUE));

### BroadcastReceiver

You use special intents sent with `sendBroadcast` to trigger broadcast messages any app can subscribe to. A receiving app must provide a `BroadcastReceiver` class to handle whatever action it should trigger with the broadcast. You can register the class to receive broadcasts in one of two ways:

* in the manifast

    <receiver android:name=".MyReceiver">
        <intent-filter></intent-filter>
    </receiver>

or 

* dynamically

    registerReceiver( myReceiver, intentFilter );
    
The intent-filter must specify the type of broadcast you want to receive: `<action android:name="com.myapp.MYBROADCASTTYPE"/>` or:

    IntentFilter if = new IntentFilter("com.myapp.MYBROADCASTTYPE"); 
    registerReceiver(myReceiver, intentFilter);

Manifest intents will receive broadcasts, even starting your app if need be to process the broadcast. Dynamic intents will only receive broadcasts while the app is running.
