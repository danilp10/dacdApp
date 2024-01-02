# Weather Prediction Java Application

## Front Page

- **Tittle:** Weather Prediction Java Application
- **Subject:** Development of applications for Data Science (DACD)
- **Course:** 2º
- **Degree:** Degree in Data Science and Engineering (GCID)
- **School:** Computer Engineering School (EII)
- **University:** Las Palmas de Gran Canaria University (ULPGC)

## Functionality Summary and Description

The Java Weather Forecasting Application periodically obtains weather forecasts from the OpenWeatherMap API and sends them to the broker, allowing to read these forecasts through a subscription system. It provides multi-island forecasts, including temperature, precipitation probability, humidity, clouds and wind speed for the next 5 days at 12:00 p.m. In addition, forecasts in the form of events are written serialised in a directory, with one event per line.

## Key Features

- **API Integration:** The application fetches weather forecasts from the OpenWeatherMap API periodically.

- **Message Broker:** Utilizes a message broker to send and manage weather forecasts, allowing access through a subscription system.

- **Multi-Island Forecasts:** Provides forecasts for multiple islands, including temperature, precipitation probability, humidity, clouds, and wind speed for the next 5 days at 12:00 p.m.

- **Event Serialization:** Serializes weather forecasts into events and stores them in a directory, with each event written on a separate line.

- **Scheduled Fetching:** Automatically queries the API every 6 hours to update weather predictions.

## Project Structure

The project is structured into different modules and packages:

- `org.ulpgc.dacd.model:` Contains model classes representing locations and weather data.

- `org.ulpgc.dacd.control:` Includes controllers for handling weather data, connecting to the API, storing data in the database, and managing JMS messaging.

- `org.ulpgc.dacd.control.JmsWeatherStore:` Implements the `WeatherStore` interface for saving weather data to a JMS (Java Message Service) topic.

- `org.ulpgc.dacd.control.EventStoreBuilder:` Listens for weather data on a JMS durable subscription and processes it using `JsonStore`.

- `org.ulpgc.dacd.JsonStore:` Stores weather data in JSON format in a specific directory structure.

## Design

###Design Patterns and Principles

The project follows several design principles and uses common patterns to ensure modularity, extensibility, and code clarity. Some of the design principles used are SOLID (Single Responsibility Principle, Open/Closed Principle) and the use of patterns such as Observer for updating the database.

### Class diagrams
   ![imagen](https://github.com/danilp10/dacdFirstPractice/assets/97803190/ecdbd6f6-b626-46c8-8b65-fbf50bedb8d9)
   ![imagen](https://github.com/danilp10/dacdFirstPractice/assets/97803190/0ae0bfa4-afcb-40e3-9467-07c697eb8018)




### Dependency relationships

1. `WeatherController` depends on `OpenWeatherMapSupplier` and `JmsWeatherStore` to obtain and store weather data.
2. `JmsWeatherStore` implements the `WeatherStore` interface and sends weather data to a JMS topic.
3. `EventStoreBuilder` listens for weather data on a JMS durable subscription and processes it using `JsonStore`.
4. `JsonStore` stores weather data in JSON format.

## Resources Used

- Java Development Kit (JDK) 8 or higher
- OpenWeatherMap API
- Message broker for subscription system (e.g., Apache ActiveMQ)
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

## Configuration:

1. Open Main.java and update the apikey variable with your OpenWeatherMap API key.
2. Verify and update the island locations.

## Usage:

The application will automatically run at specified intervals, querying the API for weather predictions for the next 5 days at 12:00 p.m. for each island. The data will be sent to the broker and written in a new directory.

## Adittional Information:
This project includes a release for easy testing and execution. The release consists of pre-compiled JAR files and associated dependencies. Follow the steps below to run the application:

Download the Release: Visit the Releases section on GitHub.

Choose the Latest Release: Select the latest release from the list. Releases are tagged with version numbers.

Download the JAR Files: Download the JAR files from the assets section of the chosen release. Ensure that you download the JAR files and any accompanying dependencies.

Configure API Key: Open the Main.java file and update the apikey variable with your OpenWeatherMap API key.

Run the Application: Open a terminal, navigate to the directory containing the JAR files, and run the application using the following command:
java -cp ".:lib/*" org.ulpgc.dacd.control.Main
This command assumes that the JAR files and dependencies are in the same directory.


