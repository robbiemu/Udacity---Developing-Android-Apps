# [Developing Android Apps](https://www.udacity.com/course/progress#!/c-ud853)
@ [Udacity](https://www.udacity.com)
_via [Springboard](http://www.springboard.com)'s [Android App Development](https://www.springboard.com/learning-paths/android/ path
## Lesson 1

### [Android Studio](http://developer.android.com/tools/studio/index.html)
requires jre, based on IntelliJ IDEA, and HAXM emulation (and to turn off Hyper-V)

_has_ github integration

### Setup

#### Running projects

There is an emulator using HAXM with a defaut Nexus device.

You can connect a real device as well.

#### Gradle

Gradle is the build system that packages up and compiles Android Apps. Android Studio automatically generates Gradle files for your application, including the `build.gradle` for your app and module and the `settings.gradle` for your app. You do not need to create these files, they are created for you by the IDE.

TIP: if your project is having Gradle issues, sometimes clicking the **Sync Project with Gradle Files** button helps. Running clean and rebuilding your project can also help resolve errors.

### New projects
Suggest to set target to current and minimum as far back as you can go based on the features you want to support. Typically this will not be further back than  v.21

#### Application

The application as a whole is what is build and run. 

An application is a loose collection of classes for the user to interact with. The UI components are organized into Activities, which we learned about in this lesson. The behind-the-scenes work is handled by other Android classes including:

* Content Providers (Lesson 4) - Manage app data.
* Services (Lesson 6) - Run background tasks with no UI, such as downloading information or playing music.
* Broadcast Receivers (Lesson 6) - Listen for and respond to system announcements, such as the screen being turned on or losing network connectivity.

#### [Activity](http://developer.android.com/guide/components/activities.html)

An Activity is an application component that provides a screen with which users can interact in order to do something, such as dial the phone, take a photo, send an email, or view a map. Each activity is given a window in which to draw its user interface.

By default a `MainActivity` class is creted for you.

#### [Fragment](http://developer.android.com/guide/components/fragments.html)

A Fragment represents a behavior or a portion of user interface in an Activity. You can combine multiple fragments in a single activity to build a multi-pane UI and reuse a fragment in multiple activities. You can think of a fragment as a modular section of an activity, which has its own lifecycle, receives its own input events, and which you can add or remove while the activity is running (sort of like a "sub activity" that you can reuse in different activities).

A fragment must always be embedded in an activity and the fragment's lifecycle is directly affected by the host activity's lifecycle. 

#### [Views](http://developer.android.com/reference/android/view/View.html) and [ViewGroups](http://developer.android.com/reference/android/view/ViewGroup.html)

Views are the basic building block for user interface components. A View occupies a rectangular area on the screen and is responsible for drawing and event handling. View is the base class for widgets, which are used to create interactive UI components (buttons, text fields, etc.). 

The view group is the base class for layouts and views containers. It is an invisible container for arranging views.

### XML [Layouts](http://developer.android.com/guide/topics/ui/declaring-layout.html)

A layout defines the visual structure for a user interface, such as the UI for an activity or app widget.

Usually a fragment's page will be defined in xml, and the top and outer must element will be a layout or a container-style view:

* [Linear Layout](http://developer.android.com/guide/topics/ui/layout/linear.html)
* [Relative Layout](http://developer.android.com/guide/topics/ui/layout/relative.html)
* [List View](http://developer.android.com/guide/topics/ui/layout/listview.html)
* [Grid View](http://developer.android.com/guide/topics/ui/layout/gridview.html)

Items in a list view (and other scrollable views/layouts) are built with an innermost collection of views, a mid-tier view to contain them, and an out [ScrollView](http://developer.android.com/reference/android/widget/ScrollView.html) to provide scrolling through the data.

### Views and XML

To describe our user interface, we describe layouts using XML. The layout defines a collection of views, view groups and the relationships between them. Our layouts are stored in the app/src/main/res/layout directory. To turn an xml layout into java view objects, we need to **inflate** the layout. After the layout is inflated, we need to associate it with an Activity or Fragment. This process of inflating and associating is a little different depending on whether it’s a layout for an Activity or Fragment.

#### For an Activity

We inflate the layout and associate it with the Activity by calling the `setContentView` method in `onCreate` in our Activity:

`setContentView(R.layout.activity_main);`

#### For a Fragment

In our Fragment classes we inflate the layout in the `onCreateView` method, which includes a `LayoutInflater` as a parameter:

`View rootView = inflater.inflate(R.layout.fragment_main, container, false);`

The root view, or view element which contains all the other views, is returned by the inflate method of the LayoutInflater. We then should return this rootView for the onCreateView.

### ListView

A listview is defined in XML (as in a fragment), then populated with data from a Java class `extends Fragment` with the name of that fragment. This fragment class is called when the application instantiates the fragment. The listview can then be connected to and given data through its element's `id` in the java class via the `R.id.IDofListViewInFragment` property of R (the class that enumerates resources).

#### Adapters with ListViews

Use an adapter to provide data on demand to the list view, rather than generating as many ListView child views as there are elements in the data set, all at once.

An `ArrayAdapter` to a `ListArray` is instatiated with a current application `context`, an id of each list item (the child of listview, defined in XML), an id of the listview, and a List array of data to display. As the Listview requests data (via imput from the ScrollView, I imagine), it seeks it from the ArrayAdapter, which retrieves that data only and formats it with the child view item, which is then returned to the lsit view to push onto the stack.
