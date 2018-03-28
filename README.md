# Upcoming Movies Sample App
> Show a list of upcoming movies retrieved from [The Movie Database (TMDb)](https://www.themoviedb.org/) 

The application has a listing of the upcoming movies. Selecting an item from this list loads a screen with details like movie name and overview, the Poster and Backdrop images and other information.

The upcoming movies query is paginated. Once the user scrolls the list to the 5th last item, a request to download the next movies page is triggered. Depending on the internet speed, it is not even possible to notice that there is a network request running on the background.

The project designed around a simple MVVM approach. It use Dependency Injection, to avoid having multiple instances of the network client.

## Known Issues

There are some points that need to be enhanced in this sample app.

* There is a bug that makes the home screen being reloaded on rotation. This is happening because the loading of the first page of upcoming movies is called from the Activity `onCreate`.
* The network errors should be handled more gracefully, letting the user know of changes in the internet connection state.
* The project does not have a unit test suite.

## Dependencies

This project was developed in [Kotlin](https://kotlinlang.org/), targeting version `1.2.31`. It depends on the Kotlin plugins for Android and on the KAPT annotation processor. Please check the top level and app module Gradle files for more details.

### Main Libraries

* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html) is used to build the architecture with View Model and Live Data
* [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html) is used for targeting Android Lollipop as the minimum version
* [Retrofit](http://square.github.io/retrofit/) is used for providing the TMDb client connection
* [Dagger](https://google.github.io/dagger/) is used for injecting a single Retrofit client instance on the view models

## Development Environment Setup

You need to have Gradle and the Android SDK installed. This project is targeted at API 27. 

If you are using Android Studio, Gradle will check if you have the needed Android SDK and will download the libraries.

You can download the code directly from the command line using:

```sh
git clone https://github.com/rafaelwinter/upcoming-movies-sample.git
```

## Installation

The app can be installed on a connected device, from Android Studio, by pressing the Run button.

You can also install an APK from the command line, for example:

```sh
adb install app-debug.apk
```

## Contact Info

Twitter: [@rafael_winter](https://twitter.com/rafael_winter)

LinkedIn: [Rafael Winter](https://www.linkedin.com/in/rafaelwinter/)
