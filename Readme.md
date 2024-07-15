# KidFind

## Project Description

KidFind is an Android application designed to help parents track the location of their children and receive alerts from them. Parents can see their children's locations on a map and receive notifications, while children can send alerts to their parents with the push of a button.

## Key Features

- **Register**: Users can register as either a kid or a parent.
- **Login**: Users can log in to access the application's features.
- **Integration**: The application integrates with an existing Spring Boot backend, allowing users to utilize shared functionalities and data.
- **Send Notification**: Children can send alerts to their parents.

## Backend Integration

KidFind integrates with an existing Spring Boot backend (MiniHeros). This integration allows for shared user data, meaning if a user already exists in the backend, they do not need to register again.

## Prerequisites

Before running the application, ensure you have the following prerequisites installed:

- Java 21
- Android Studio with Gradle 8.4.1
- Retrofit library
- Docker
- H2 Database
- Google Maps API Key

## Installation and Setup

### Backend Setup

1. Clone the backend repository [MiniHeros Backend](#).
2. Open the project in Spring Tool Suite (STS).
3. Run the Spring Boot application.
4. Note the base URL for the Android Studio API service (Retrofit).

### Android App Setup

1. Clone this repository.
2. Open the project folder in Android Studio.
3. Ensure the MiniHeros backend is running.
4. Modify the Google Maps API key in `AndroidManifest.xml`.
5. Update the backend base URL in the Retrofit configuration (default: `10.0.2.2:8084`).

## Usage

### User Registration and Login

- **Registration**: Users can register by clicking on "Don't have an account? Sign up".
- **Login**: Registered users can log in using their credentials.

### Parent Functionality

- Parents can view the locations of their children on a map.
- Parents receive notifications from their children.

### Kid Functionality

- Kids have a button on the map screen to send an alert to their parents.

## Technology Stack

- Java
- Docker
- H2 Database
- Swagger UI
- Google Maps
