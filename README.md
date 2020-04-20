# Brainy Make-A-Rhyme Android - Delivery Documentation
Version 1.0.0 (Build 5)

## Release Notes

### New Software Features For Current Release

* Student Sign-In uses name selection (not text entry)
* Improved User Interface for email entry to be more intuitive for users

### Bug Fixes Since Previous Release

* Scaling issues for API 26+
* Vertical scaling issues for graphs
* Help Button not integrated (causes crashes)
* Incorrect behavior for API 23 for automatically dismissing activities
* Pausing the audio when playing a rhyme crashes the app
* 'Friends' images are missing transparent backgrounds
* Analytics - 'Created Rhymes' screen is completely blank when a child has no rhymes
* Incorrect audio file loaded for Muddy Park and Stranger Parade
* Several images do not show up properly when attempting to take a quiz
* 'Application Welcome' screen lacks dynamic scaling and cuts off buttons
* Keyboard does not dismiss and blocks the buttons on the screen

### Known Bugs and Defects

* No automatic font scaling for API 25 and lower (some fonts are smaller than necessary)
* There is no restriction/recommendation for which words children place in blanks
* There is no ability to remove students (from the device and from a teacher's class)

## Install Guide

### Acquiring The Application

* Pre-requisites: GitHub Account
* Dependent Libraries: None
* Download Instructions
  * Go to the GitHub page for Brainy Make-A-Rhyme Android
  * Click on the green button labeled: "Clone or download"
  * Copy the URL that shows up
  * In Terminal/Command Prompt, use the command "git clone <git-url>"
  * This will clone the repository into your local directory

### Using The Application

* Pre-requisites: Android Device (API 22+), Android Studio
* Dependent Libraries: Everything should be automatically installed in Android Studio's first build
* Windows 7, 8, or 10 (macOS Yosemite or newer)
* Physical Device Instructions
  * Must connect the device to the computer's USB port so that Android Studio recognizes the device
  * Device Manager -> Right-click the name of the device connected to the USB port -> Update Driver Software ->
  Browse computer for the driver software -> Locate the USB driver folder -> Install
  * Select the USB-connected device in the dropdown list of devices in Android Studio to run the application
* Emulator Instructions
  * Enter the project in Android Studio
  * Select Run -> Run 'app'
  * Select a virtual device (or select 'Create New Virtual Device')
  * Click 'OK' to run the application in the emulator
  
 ### Troubleshooting
 
 * Common Issues for Android Studio
  * Gradle sync issues - Check versions of the dependencies listed in build.gradle (if recently updated, consider reverting a package to its previous version)
  * Build failing unexpectedly - First, try Build -> Clean Project. Second, try restarting Android Studio. Last, try restarting your computer.
  * Miscellaneous errors - In Android Studio, press 'Run' in the bottom left corner of the screen -> Look for red text -> Examine red text for error name -> copy the error message into a search engine; other people often have experience with the same issue
