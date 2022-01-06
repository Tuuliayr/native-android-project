# native-android-project

| Name   | Tuuli Äyräväinen                                                                                                                                                                                                                                                                                                                                      |
| ------ | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Topic  | A mobile application to remind and motivate people who tend to sit still most of the day to move their body. User can get light exercise suggestions from the app. If the weather is nice the app will push a notification that encourages the user to go outside. For checking weather [OpenWeatherMap](https://openweathermap.org/api) API is used. |
| Target | Android/Kotlin                                                                                                                                                                                                                                                                                                                                        |

## Release 1: 2021-05-12 features

- User gets a notification with dummy data, when opening the app.
- User is able to tap the notification which opens weather view.
- User is able to view weather data from the backend. The data includes current temperature and weather description in Tampere.
- User is able to navigate between home and weather views.

## Release 2: 2021-05-21 features

- User receives a notification two times between 9am and 8pm if weather is clear.
- User is able to view current weather data from their location.
- User is able to turn off notifications from the app's settings.
- The app uses location services only if user has granted them.
- User is able get 11 different exercise suggestions.

## Known bugs

- Turning locations on and off may cause the app to hang for a while before weather data is shown again.
- For location permission "always" and "never" are only recognized commands.

## Getting started

### 1. Java and Kotlin installation (Windows)

- Install latest versions of Java SE (https://jdk.java.net) and Kotlin compiler (https://github.com/JetBrains/kotlin/releases/tag/v1.6.10).
- Unzip the packages and move them to another folder e.g. ```c:\Program Files\```.
- Modify paths. Edit the system environment variables by adding new paths that point to the bin directory of your installation folders.
- Open command prompt and check if you have installed correctly by giving commands
```
javac -version
java -version
kotlinc -version
```
Kotlin requires 1.6+ Java.

### 2. Download Android Studio

https://developer.android.com/studio

### 3. Clone the repo

```
git clone https://github.com/Tuuliayr/native-android-project.git
```

## Usage

- Demonstrating the functionalities of the app with screenshots.

By pressing the "get random exercise" button user receives suggestions of different exercises.                    |  Exercise 1               |    Exercise 2
:----------------------------------------------------------------------------------------------------------------:|:-------------------------:|:-------------------------:
![active](https://user-images.githubusercontent.com/70134583/148447623-13d6a417-93b5-4188-9886-fd48fa19dce3.jpg)  |  ![activity1](https://user-images.githubusercontent.com/70134583/148447647-c6436e40-68b7-491c-9fbb-2b9c498ceb59.jpg) | ![activity2](https://user-images.githubusercontent.com/70134583/148447660-2cdad62f-f4d2-4c20-8a7c-a1a0c835479c.jpg)

Pressing the weather icon button directs user to another view that shows current weather based on user's current location.    |  Demonstration of the notification.
:---------------------------------------------------------------:|:---------------------------------------------------------------:
![weather](https://user-images.githubusercontent.com/70134583/148448794-929f9344-7f81-4f7c-9e8a-ee7e44ea5e32.jpg) |![notif](https://user-images.githubusercontent.com/70134583/148448754-772ca0d0-6fa3-4afe-b77a-47f231b3f47c.jpg)



