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

- User receives a notification two times between 9am and 8pm if weather is clear
- User is able to view current weather data from their location
- User is able to turn off notifications from the app's settings
- The app uses location services only if user has granted them
- User is able get 11 different exercise suggestions

## Known bugs

- App sends a notification every time user goes from weather view to home view.
