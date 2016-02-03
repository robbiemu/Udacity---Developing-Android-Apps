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

gradle manages the build process for you.

### New projects
Suggest to set target to current and minimum as far back as you can go based on the features you want to support. Typically this will not be further back than  v.21

#### Application

The application as a whole is what is build and run

#### [Activity](http://developer.android.com/guide/components/activities.html)

An Activity is an application component that provides a screen with which users can interact in order to do something, such as dial the phone, take a photo, send an email, or view a map. Each activity is given a window in which to draw its user interface.

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

### ListView

A listview is defined in XML (as in a fragment), then populated with data from a Java class `extends Fragment` with the name of that fragment. This fragment class is called when the application instantiates the fragment. The listview can then be connected to and given data through its element's `id` in the java class via the `R.id.IDofListViewInFragment` property of R (the class that enumerates resources).

#### Adapters with ListViews

Use an adapter to provide data on demand to the list view, rather than generating as many ListView child views as there are elements in the data set, all at once.

An `ArrayAdapter` to a `ListArray` is instatiated with a current application `context`, an id of each list item (the child of listview, defined in XML), an id of the listview, and a List array of data to display. As the Listview requests data (via imput from the ScrollView, I imagine), it seeks it from the ArrayAdapter, which retrieves that data only and formats it with the child view item, which is then returned to the lsit view to push onto the stack.
