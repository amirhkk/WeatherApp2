# WeatherApp2
Weather App for **Cambridge (Student) Cyclists** 

## Dependencies
Requires google/gson library - `Maven: com.google.gson:gson:2.9.0`

Runs on Windows and Mac

## 1. Choice of tools

### API integration to fetch weather data
- Https://openweathermap.org/api, a free api providing us with all the required data.
- We read the api data and store as a JSON object using google's GSON library (this needs to be installed as an external library).
- This JSON object can then easily be queried by specifying the relevant fields.

### Graphics
- Java Swing and AWT were used for the graphics of the app.

## 2. Use instructions
The app is run from `Main.java`

### Main page
- This is what will first be shown upon opening the app.
- The temperature (Celsius by default) and precipitation are shown at the bottom. Both actual and felt temperature will be shown as default.
- The slider may be used to look at hourly forecasts for up to 48 hours and daily forecast for up to a week.
- The settings button at the top leads to the Settings page.
- When a weather warning is issued, a warning will appear below the settings button with the following possible emojis:
    - 🌩️: Thunderstorm
    - 🌧️: Heavy rain
    - ☂: Imminent rain
    - 🔥: Extreme high temperature
    - ❄: Extreme low temperature
- The warning may be clicked to enter the Warning page, which shows greater detail on the warning issued.

### Settings page
- Accessed through the settings button on the Main page.
- Custom notifications may be set for specific days of the week and times. [Note: notifications are not implemented because this app is not run on mobile.]
- Notifications for different weather warnings may be enabled/disabled by selecting checkboxes next to them.
- The specific thresholds for extreme temperature and precipitation may be set by selecting the corresponding buttons and entering values in the textbox that pops up.
- The formatting (whether temperature is displayed in Celsius or Fahrenheit; whether actual/felt/both temperatures are shown) may be set by selecting the corresponding radio button.

