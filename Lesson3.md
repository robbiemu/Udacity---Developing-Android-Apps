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
    
### [Settings](http://developer.android.com/guide/topics/ui/settings.html)

Preferences can be rendered for you by the system. It will create the UI necessary for the preferences specified. Each preference is stored in the [Shared Preferences](http://developer.android.com/reference/android/content/SharedPreferences.html) file for you, in k/v pairs.

