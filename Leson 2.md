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

### [AsyncTask](http://developer.android.com/reference/android/os/AsyncTask.html)

AsyncTask provides a way to thread the network connection. The network connection will not run from the UI thread (it throws an error, which we found with the DDMS log viewer). So we want to do move the network call to its own class, and we use this opportunity to refactor our code, moving the whole fragment to it's own java file. 

`doInBackground` specifies the work to be done in the background. It must be specified as an `@Override` in the class. This will be a completely separate background thread tied to the UI fragment (bad practice but it is what we are doing for now).

