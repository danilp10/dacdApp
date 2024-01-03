# Weather Prediction Java Application

## Front Page

- **Tittle:** Weather Prediction Java Application
- **Subject:** Development of applications for Data Science (DACD)
- **Course:** 2º
- **Degree:** Degree in Data Science and Engineering (GCID)
- **School:** Computer Engineering School (EII)
- **University:** Las Palmas de Gran Canaria University (ULPGC)

## Functionality Summary and Description

This Java application periodically obtains weather forecasts from the OpenWeatherMap API and hotel availability information and sends them to the broker, allowing these forecasts and information to be read through a subscription system. Provides forecasts for multiple cities, including temperature, chance of precipitation, humidity, clouds, and wind speed for the next 5 days at 12:00 p.m. It also provides detailed information on the availability of some of the best hotels in the area, along with their price per night, total price including taxes, number of nights and other variables for the next 5 days. Additionally, forecasts and hotel information are written as serialized events in a directory, with one event per line.

## Key Features

- **API Integration:** The application periodically obtains weather forecasts from the OpenWeatherMap API and information on the availability and price of some hotels from the Xotelo API.

- **Message Broker:** Uses a message broker to send and manage weather forecasts and hotel information, allowing access through a subscription system.

- **Multi-City Forecasts:** Provides multi-city forecasts including temperature, chance of precipitation, humidity, clouds and wind speed for the next 5 days at 12:00 PM.

- **Event Serialization:** Serializes weather forecasts and hotel information into events and stores them in a directory, with each event written on a separate line.

- **Scheduled fetch:** Automatically queries both APIs every 6 hours to update information.

-**Datamart and GUI:** the broker's data is sent to a datamart in the form of an SQL database that stores the information of both providers. This information from the datamart is what is used for the implementation of the GUI, which, depending on the location, provides information on the availability of hotels in the area, along with the corresponding weather predictions.

## Project Structure

The project is structured into different modules and packages:

- `weather_provider: org.ulpgc.dacd.model:` Contains model classes representing locations and weather data.

- `weather_provider: org.ulpgc.dacd.control:` Includes controllers for handling weather data, connecting to the API, storing data in the database, and managing JMS messaging.

- `org.ulpgc.dacd.control.JmsWeatherStore:` Implements the `WeatherStore` interface for saving weather data to a JMS (Java Message Service) topic.
  
- `hotel_provider: org.ulpgc.dacd.model:` Contains model classes representing hotel data.

- `hotel_provider: org.ulpgc.dacd.control:` Includes controllers for handling hotel data, connecting to the API, storing data in the database, and managing JMS messaging.

- `org.ulpgc.dacd.control.JmsHotelStore:` Implements the `HotelStore` interface for saving hotel data to a JMS (Java Message Service) topic.

- `org.ulpgc.dacd.control.EventStoreBuilder:` Listens for weather and hotel data on a JMS durable subscription and processes it using `JsonStore`.

- `org.ulpgc.dacd.JsonStore:` Stores weather and hotel data in JSON format in a specific directory structure.

- `travel_business_unit: org.ulpgc.dacd.view:` Contains the GUI.

- `travel_business_unit: org.ulpgc.dacd.control:` Responsible of recieving the information of the broker and store it in the datamart.

## Design

###Design Patterns and Principles

The project follows several design principles and uses common patterns to ensure modularity, extensibility, and code clarity. Some of the design principles used are SOLID (Single Responsibility Principle, Open/Closed Principle) and the use of patterns such as Observer for updating the database.

### Class diagrams
   ![imagen](https://github.com/danilp10/dacdFirstPractice/assets/97803190/ecdbd6f6-b626-46c8-8b65-fbf50bedb8d9)
   ![imagen](https://github.com/danilp10/dacdFirstPractice/assets/97803190/0ae0bfa4-afcb-40e3-9467-07c697eb8018)
   ![travel_unit](https://github.com/danilp10/dacdApp/assets/97803190/12fc0ad5-6482-4440-91ee-f373d4ef2f9a)
   ![hotel_provider](https://github.com/danilp10/dacdApp/assets/97803190/ed5495ed-f52f-4302-960f-7900622eb2da)





### Dependency relationships

1. `WeatherController` depends on `OpenWeatherMapSupplier` and `JmsWeatherStore` to obtain and store weather data.
2. `JmsWeatherStore` implements the `WeatherStore` interface and sends weather data to a JMS topic.
3. `EventStoreBuilder` listens for weather and hotel data on a JMS durable subscription and processes it using `JsonStore`.
4. `JsonStore` stores weather and hotel data in JSON format.
5. `HotelController` depends on `OpenHotelMapSupplier` and `JmsHotelStore` to obtain and store hotel data.
6. `JmsHotelStore` implements the `HotelStore` interface and sends hotel data to a JMS topic.
7. `DatamartConnection` recieves weather and hotel information and stores it in a SQL database.

## Resources Used

- Java Development Kit (JDK) 8 or higher
- OpenWeatherMap API
- Xotelo API
- Message broker for subscription system (e.g., Apache ActiveMQ)
- Git
- Markdown
- SQL 


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

1. Open Main.java on weather_provider module and update the apikey variable with your OpenWeatherMap API key.
2. Verify and update the city locations.
3. Open Main.java on datalake_builder and update the rootDirectory with the directory you want the events to be written.

## Usage:

The application, "Travel App", allows users to select a travel destination from a drop-down list. Once a destination is selected, the application displays:
Weather Predictions: Information about the weather of the selected destination, including details such as date, rainfall, wind speed, temperature, humidity, cloud cover and geographic coordinates.
Hotel Information: The availability and details of hotels in the selected destination, including hotel name, code, nightly rate, tax, and check-in and check-out dates. Additionally, the total price of the stay is shown, calculated by multiplying the rate per night by the total number of nights.
The application is presented using a simple and user-friendly graphical user interface (GUI).

## Adittional Information:
This project includes a release for easy testing and execution. The release consists of pre-compiled JAR files and associated dependencies. Follow the steps below to run the application:

Download the Release: Visit the Releases section on GitHub.

Choose the Latest Release: Select the latest release from the list. Releases are tagged with version numbers.

Download the JAR Files: Download the JAR files from the assets section of the chosen release. Ensure that you download the JAR files and any accompanying dependencies.

Configure API Key: Open the Main.java file on the weather_provider module and update the apikey variable with your OpenWeatherMap API key.

Configure the rootDirectory: Open the Main.java file on the datalake_builder module and update the rootDirectory variable with the directory you want the events to be written in.

Run the Application: Open a terminal, navigate to the directory containing the JAR files, and run the application using the following command:
java -cp ".:lib/*" org.ulpgc.dacd.control.Main
This command assumes that the JAR files and dependencies are in the same directory.


## Clarification:
The xotelo application may take between half an hour and an hour to perform the data query the first time the application is run.
