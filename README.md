# Weather Prediction Java Application

## Front Page

- **Tittle:** Weather Prediction Java Application
- **Subject:** Development of applications for Data Science (DACD)
- **Course:** 2º
- **Degree:** Degree in Data Science and Engineering (GCID)
- **School:** Computer Engineering School (EII)
- **University:** Las Palmas de Gran Canaria University (ULPGC)

## Functionality Summary and Description

The Weather Prediction Java Application periodically fetches weather predictions from the OpenWeatherMap API and stores them in an SQLite database. It provides forecasts for multiple islands, including temperature, precipitation probability, humidity, clouds, and wind speed for the next 5 days at 12:00 p.m.

## Key Features

- **API Integration:** Utilizes the OpenWeatherMap API to fetch accurate and up-to-date weather predictions.

- **SQLite Database:** Persists weather data in an SQLite database, organized by island and date.

- **Island-based Tables:** Each island has its own table in the database for easy retrieval and management.

- **Scheduled Fetching:** Automatically queries the API every 6 hours to update weather predictions.

## Project Structure

The project is structured into different packages:

- `org.ulpgc.dacd.model:` Contains model classes representing locations and weather data.

- `org.ulpgc.dacd.control:` Includes controllers for handling weather data, connecting to the API, and storing data in the database.

## Design

###Design Patterns and Principles

The project follows several design principles and uses common patterns to ensure modularity, extensibility, and code clarity. Some of the design principles used are SOLID (Single Responsibility Principle, Open/Closed Principle) and the use of patterns such as Observer for updating the database.

### Class diagram
   ![imagen](https://github.com/danilp10/dacdFirstPractice/assets/97803190/849e857d-ebaf-4d0d-a0c1-d4cc31d9f836)


### Dependency relationships

1.`WeatherController` depends on `OpenWeatherMapSupplier` and `SqliteWeatherStore` to obtain and store weather data.
2. `SqliteWeatherStore` interacts with the SQLite database for data persistence.
3. The classes in `org.ulpgc.dacd.model` represent the fundamental entities of the system, such as `Location` and `Weather`.

## Resources used

- Java Development Kit (JDK) 8 or higher
- SQLite database
- Git
- Markdown

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/weather-prediction-java.git

2. Navigate to the project directory:

   ```bash
   cd weather-prediction-java

3. Compile the code:

   ```bash
   javac -cp ".:lib/gson-2.8.8.jar" org/ulpgc/dacd/control/Main.java

4. Run the application:

   ```bash
   java -cp ".:lib/gson-2.8.8.jar" org.ulpgc.dacd.control.Main

## Configuration

1. Open Main.java and update the apikey variable with your OpenWeatherMap API key.
2. Verify and update the island locations in the WeatherController class.

## Usage

The application will automatically run at specified intervals, querying the API for weather predictions for the next 5 days at 12:00 p.m. for each island. The data will be stored in the SQLite database.
