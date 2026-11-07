# LegendDayNight
A Huawei Mate 20 Pro inspired Android live wallpaper that changes with the time of day.

Download the signed APK from [GitHub Releases](https://github.com/rexmont/LegendDayNight/releases/latest). Install it, then select **Legend Day Night** in Android's live wallpaper picker. There is no launcher icon.

## Build
Use JDK 17 or newer and Android SDK 36. Set ANDROID_HOME or sdk.dir in local.properties, then run:

```
./gradlew assembleRelease testDebugUnitTest lintRelease
```

The release output is unsigned; sign it with your private release keystore using Android SDK apksigner. Never commit signing keys or passwords. This GitHub release retains target SDK 29 and minimum Android 5.0; the Google Play target SDK lint policy is excluded because this distribution is through GitHub.

## Validation for v1.0
- Release build, existing unit test, and lint completed successfully (lint warnings remain).
- Signed release APK installed on the Android 13 (API 33) x86_64 emulator.
- Wallpaper preview, home-screen activation, and portrait/landscape rendering checked.
- At simulated 12:00 and 22:00, returning from Settings to home rendered the appropriate day/night image; no app crashes observed.
- The full 15-minute scheduled refresh interval and physical devices were not tested.

## Roadmap
* Retrieve sunset/sunrise times for the geographical location.
