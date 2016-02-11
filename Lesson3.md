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

