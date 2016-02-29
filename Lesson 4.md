# [Developing Android Apps](https://www.udacity.com/course/progress#!/c-ud853)
@ [Udacity](https://www.udacity.com)
_via [Springboard](http://www.springboard.com)'s [Android App Development](https://www.springboard.com/learning-paths/android/ path
## Lesson 2

### [Activity Lifecycle](https://s3.amazonaws.com/content.udacity-data.com/course/ud853/Android_Activity_LifeCyle.png)

![](Android_Activity_LifeCyle.png)

You activty will run though the active life time and visible life time many times.

#### Active Life Cycle

Before the `onPause` is called and the acitivty is obscured.

Pause processes that don't draw the app in the background (modals only partially obscure the app).

Apps can be terminated to preserve resources for the system. When this happens, their onPause and onStop events are triggered. On lod devices (pre honycomb, I think he said), it can happen only at onPause, so it is best when developing for those devices to consider app termination at onPause. For more recent devices, onStop is sufficient. Listeners, etc (for example, an async_task running to update the UI) should be disconnected during onPause or onStop since it is doing work that will not be displayed. In general all work that would not want to be paused could instead be moved outside of an application, as to a Service.

#### Visible Life Cycle

In order to maintain state when an app is paused, there are a series of activity methods:

* _Active_
* onSaveInstanceState
* onPause
* _Terminated_
* onCreate
* onRestoreInstanceState (only used if not first launch of app), called with bundle with which to restore state.

### State data

Android offers 3 options to simplify storing state data: SharedPreferences, Files, and [SQLite Database](http://developer.android.com/guide/topics/data/data-storage.html#db).

_see also: [Data Storage](http://developer.android.com/guide/topics/data/index.html)_

#### SQLLite

To support SQLlite, Android implements a contract -> [content provider](http://developer.android.com/guide/topics/providers/content-providers.html) pattern. The [BaseColumns](http://developer.android.com/reference/android/provider/BaseColumns.html) class can be extended to create schema details about the tables to be queried. I comes with an _ID autoincrement already defined.

The database is managed with the [SQLightOpenHelper](http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html) class.

_see also [SQLite Database documentation](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html)._

#### Queries

Queries can be executed from the `SQLiteDatabase` object (from [getReadableDatabase](http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html#getReadableDatabase(%29)) with the [query](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#query(java.lang.String, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.lang.String%29) method. This is just a helper function for constructing and executing queries. All variations of `query` return a [cursor](http://developer.android.com/reference/android/database/Cursor.html) object over the results of the query. Typically, you would call `moveToFirst` and later `moveToNext` to traverse the return values.  

Typically, you would then validate the results before using them, and close the sql connection.

### Testing

[Tests](http://www.tutorialspoint.com/junit/junit_quick_guide.htm) ensure that functional aspects of the application perform to specification. This helps in the lifecycle of product maintanence and extension. Android provides a [testing structure](http://developer.android.com/tools/testing/testing_android.html) to assist in implementing tests for your application.

Unit Tests ensure function of specific parts of the system. Integration tests ensure the app behaves as expected to user or foreign API input (receving a push, for example).

_see also: [Getting started with testing](http://developer.android.com/training/testing/start/index.html)

### [Content Providers](http://developer.android.com/guide/topics/providers/content-providers.html)

[ContentProviders](http://developer.android.com/reference/android/content/ContentProvider.html) abstract the details of data storage and retreival, generifying it so data can be shard between apps in a standard way. This fulfills the pattern of a _model_.

#### To build a ContentProvider

![](unnamed.png)

determine URIs — this is the URI for queries **to** the `ContentProvider`, in order to retrieve unique data from it. 

