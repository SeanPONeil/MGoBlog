[![Build Status](https://travis-ci.org/SeanPONeil/MGoBlog.png)](https://travis-ci.org/SeanPONeil/MGoBlog?branch=dev)

MGoBlog
=======

MGoBlog written from the ground up to be ICS friendly

Compilation
-----------

MGoBlog is built using Maven and there is a very minimal amount of setup required for compilation.

Two environment variables are required which point to your Android SDK and native Android SDK. A common
place to put these is in a `.bash_profile` file in your home directory.

    export ANDROID_HOME=/path/to/android/sdk
    export ANDROID_NDK_HOME=/path/to/android/ndk

With these two environment variables loaded you can compile the modules and sample application by running
`sh get_dependencies.sh`, which will clone and install some libraries that are not in Maven Central, followed by `mvn clean install`. Each module's built artifact will be in its respective `target/` folder.

## Acknowledgements

This project uses many open source libraries, such as

* [ActionBarSherlock](https://github.com/JakeWharton/ActionBarSherlock)
* [Android-PullToRefresh](https://github.com/chrisbanes/Android-PullToRefresh)
* [android-menudrawer](https://github.com/SimonVT/android-menudrawer)
* [android-maven-plugin](https://github.com/jayway/maven-android-plugin)
* [Otto](https://github.com/square/otto)
* [Retrofit](https://github.com/square/retrofit)
* [Tape](https://github.com/square/tape)
* [Dagger](https://github.com/square/dagger)
* [Jsoup](http://jsoup.org/)
* [PrettyTime](http://ocpsoft.org/prettytime/)
