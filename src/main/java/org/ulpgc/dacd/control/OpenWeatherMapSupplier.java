package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class OpenWeatherMapSupplier {
    private List<Weather> cache;

    public static Weather getWeather(Location location, Instant instant) throws IOException {
        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";
        String apiKeyParam = "appid=" + "3f7e30bbced203f4b907d03ba08d8ac6";
        String latParam = "lat=" + location.getLat();
        String lonParam = "lon=" + location.getLon();
        String urlStr = apiUrl + "?" + latParam + "&" + lonParam + "&" + apiKeyParam;

        // Crea una conexión HTTP y abre un InputStream para leer la respuesta
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();

        // Lee la respuesta de la API
        Scanner scanner = new Scanner(inputStream);
        StringBuilder responseBody = new StringBuilder();
        while (scanner.hasNext()) {
            responseBody.append(scanner.next());
        }
        scanner.close();

        // Parsea la respuesta JSON para obtener los datos meteorológicos
        Weather weatherData = parseWeatherDataFromJson(responseBody.toString());

        connection.disconnect();

        return weatherData;
    }

    private static Weather parseWeatherDataFromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        JsonArray list = jsonObject.getAsJsonArray("list");

        if (!list.isEmpty()) {
            JsonObject firstItem = list.get(0).getAsJsonObject();

            String dtTxt = firstItem.get("dt_txt").getAsString();
            Instant instant = Instant.parse(dtTxt);

            JsonObject filteredJson = new JsonObject();
            filteredJson.addProperty("dt_txt", instant.toString());
            filteredJson.addProperty("temp", "temp");
            filteredJson.addProperty("pop", "pop");
            filteredJson.addProperty("humidity", "humidity");
            filteredJson.addProperty("speed", "speed");
            filteredJson.addProperty("clouds", "clouds");

            return gson.fromJson(filteredJson, Weather.class);
        } else {
            return null;
        }
    }

}
