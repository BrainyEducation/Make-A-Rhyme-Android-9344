# Brainy Make-A-Rhyme Android - Delivery Documentation
Version 1.0.0 (Build 5)

## Release Notes

### New Software Features For Current Release

* Student Sign-In Uses Name Selection (Not Text Entry)
* Improved User Interface for email entry to be more intuitive for users

### Bug Fixes Since Previous Release

* Scaling issues for API 26+
* Vertical scaling issues for graphs
* Help Button not integrated (causes crashes)
* Incorrect behavior for API 23 with automatically dismissing activities
* Pausing the audio when playing a rhyme crashes the app
* Friends images are missing transparent backgrounds
* Analytics - Created rhymes screen is completely blank when no rhymes exist for a child
* Incorrect audio file loaded for Muddy Park and Stranger Parade
* Several images do not show up properly when attempting to take a quiz
* Application Welcome lacks dynamic scaling and cuts off buttons
* Keyboard does not dismiss and blocks the buttons on the screen


### Known Bugs and Defects

* No automatic font scaling for API 25 and lower (some fonts are smaller than necessary)
* There is no restriction/recommendation for which words children place in blanks
* There is no capability of removing students (from the device and from a teacher's class)

## Install Guide
* Pre-requisites: Android Device
* Windows 7, 8, or 10 required if testing Android Studio on Android device (excluding emulators)
* Must connect the device to the computer's USB port so that Android Studio recognizes the device
* Device Manager -> Right-click name of device connected to USB port -> Update Driver Software ->
  Browse computer for driver software -> Locate the USB driver folder -> Install
* Select the USB-connected device in the dropdown list of devices in Android Studio to run the application.
