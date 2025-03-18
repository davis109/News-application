# AI-Powered News Aggregator App

An Android application that aggregates news from various sources, provides AI-powered recommendations, and supports offline reading with text-to-speech capabilities.

## Features

- Fetches news from NewsAPI and categorizes it by topics
- Uses AI-based recommendations based on user preferences and reading history
- Offline mode to save articles for later reading
- Text-to-Speech feature for listening to news articles
- Customizable news preferences by categories
- Modern UI built with Jetpack Compose

## Tech Stack

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **MVVM Architecture** - Clean separation of concerns
- **Room Database** - Offline storage
- **Retrofit** - API calls
- **NewsAPI** - News data source
- **Firebase** - Analytics and Authentication
- **ML Kit** - Text-to-speech functionality

## Setup Instructions

### 1. Get a NewsAPI Key
- Sign up at [NewsAPI.org](https://newsapi.org/) to get your API key
- In `app/build.gradle.kts`, replace `YOUR_NEWS_API_KEY` with your actual API key:
```kotlin
buildConfigField("String", "NEWS_API_KEY", "\"YOUR_ACTUAL_API_KEY\"")
```

### 2. Set up Firebase (optional)
- Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
- Add an Android app with package name `com.sebastiandavis.news_app`
- Download the `google-services.json` file and place it in the app directory
- This replaces the placeholder file in the project

### 3. Build and Run
- Open the project in Android Studio
- Sync Gradle files
- Run the app on an emulator or physical device

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Data Layer**: API services, Room database, and repositories
- **Domain Layer**: Business logic, recommendation engine
- **Presentation Layer**: ViewModels, Composables (UI)

## Screenshots

[![image](https://github.com/user-attachments/assets/5b62050b-971c-48c5-9e36-624c0e9acf9e)
![image](https://github.com/user-attachments/assets/2fa354ba-d7c4-4931-9d9e-a2e3bcb7747a)
![image](https://github.com/user-attachments/assets/98de0bdc-a016-45ea-a55e-8814bbc89b22)


]

## Future Improvements

- Enhanced recommendation algorithm
- User authentication with Firebase
- Article sharing functionality
- Dark mode support
- Integration with more news sources

## License

[Include license information] 
