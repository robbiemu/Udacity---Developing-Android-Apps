# [Developing Android Apps](https://www.udacity.com/course/progress#!/c-ud853)
@ [Udacity](https://www.udacity.com)
_via [Springboard](http://www.springboard.com)'s [Android App Development](https://www.springboard.com/learning-paths/android/ path
## Lesson 2

Concepts:

* HttpURLConnection
* Logcat
* AsyncTask
* Adding Menu Buttons
* values/strings.xml
* Permissions
* JSON parsing

### [Logcat](http://developer.android.com/tools/help/logcat.html)

You can view the output to `[Log](http://developer.android.com/reference/android/util/Log.html)` through the [bottom log on the studio view](http://developer.android.com/tools/help/am-logcat.html) (the console when the emulator is running), or through the [Android Debugging Monitor](http://developer.android.com/tools/debugging/ddms.html) (also called the Android DDMS).

A good idea is to prefence the calls to log with a string you define in each class/package context, in order to specify where your log messages are coming from. defining it in this way ensures that they compiler will through an exception if you rename the class but not that class's logs:

    private final String LOG_TAG = MainActivity.class.getSimpleName();

### [HttpURLConnection](http://developer.android.com/reference/java/net/HttpURLConnection.html)

`HttpURLConnection` is a Java class used to send and receive data over the web. We use it to grab the JSON data from the OpenWeatherMap API. The code to do so is introduced in this [gist](https://gist.github.com/anonymous/6b306e1f6a21b3718fa4)

In it we:

1. create a `URL` that will retreive the data from openweathermap.org
2. create a GET `HttpURLConnection` of type `GET` to get the data.
3. create an `InputSteam` to stream the data from the connection, and a `StringBuffer` to buffer the data down to a string.
4. return the JSON as a string.

#### [Permissions](https://developer.android.com/training/permissions/index.html)

To not trigger a security error, you'll need to have your app ask for permissions.

> When targeting Marshmallow or M preview devices, you'll need to be much more careful about checking for permissions each time you make use of them. A user could say yes to your app generally, but no to it's use of their GPS, for instance. It will be your job to check whether the user granted the permission so you can gracefully handle the case where they did not.

> You'll also want to make sure to not store any sort of variable to keep track of whether a permisison has been granted. In the new system, your app is not notified when a permission is revoked, so you'll always need to check with the system, using the Context.checkSelfPermission() method.

_see also [Permissions](http://developer.android.com/guide/topics/security/permissions.html) coverage in the security section of the online documentation_

 Manifest permissions that can be specified are listed in the [Manifest.permission](http://developer.android.com/reference/android/Manifest.permission.html) page. These are specified in the `AndroidManifest.xml` under `main` in the project (same tier as `java` and `res`). They are specified like so:
 
    <uses-permission android:name="android.permission.INTERNET" />

Using any of the strings in the Manifest.permissions document. This is done within the `<manifest>` tag. 

### [AsyncTask](http://developer.android.com/reference/android/os/AsyncTask.html)

AsyncTask provides a way to thread the network connection. The network connection will not run from the UI thread (it throws an error, which we found with the DDMS log viewer). So we want to do move the network call to its own class, and we use this opportunity to refactor our code, moving the whole fragment to it's own java file. 

`doInBackground` specifies the work to be done in the background. It must be specified as an `@Override` in the class. This will be a completely separate background thread tied to the UI fragment (bad practice but it is what we are doing for now).

In general, you'll want to use a `service` to handle asynctasks, rather than tying them to a UI thread. 

### [Menu Items](http://developer.android.com/guide/topics/ui/menus.html)

Menu items are specified under `res/menu` in XML. They must then be inflated in activity or fragment UIs. To inflate the menu in a fragment:

1. override `onCreate` and `setHasOptionsMenu` true:

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

2. override `onCreateOptionsMenu` and [inflate](http://developer.android.com/reference/android/view/MenuInflater.html) the menu xml:

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

3. In `onOptionsItemSelected` you can check which item was selected and react appropriately.

### strings.xml

Android has a specific file for all of the strings in your app, stored in `values/strings.xml`. Why? Well besides further helping separate content from layout, the strings file also makes it easy to localize applications. You simply create a `values-language/strings.xml` files for each locale you want to localize to. For example, if you want to create a Japanese version of your app, you would create a `values-ja/strings.xml`. Note, if you put the flag `translatable="false"` in your string it means the string need not be translated. This is useful when a string is a proper noun.

### JSON Parsing

Often when you request data from an API this data is returned in a format like JSON. This is the case for Open Weather Map API. Once you have this JSON string, you need to parse it.

If you’re unfamiliar with JSON, take a look at [this tutorial](http://www.w3schools.com/json/).

In Android, you can use the `JSONObject` class, documented [here](http://developer.android.com/reference/org/json/JSONObject.html). To use this class you take your JSON string and create a new object:

    JSONObject myJson = new JSONObject(myString);
    
And then you can use various `get` methods to extract data, such as `getJSONArray` and `getLong`.