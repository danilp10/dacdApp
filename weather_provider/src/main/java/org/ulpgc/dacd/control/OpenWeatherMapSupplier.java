package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import javax.jms.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenWeatherMapSupplier {
    private final String apikey;

    public OpenWeatherMapSupplier(String apikey) {
        this.apikey = apikey;
    }

    public List<Weather> getWeather(Location location, Instant instant) throws IOException {
        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";
        String urlStr = apiUrl + "?lat=" + location.getLat() + "&lon=" + location.getLon() +
                "&appid=" + this.apikey + "&units=metric";

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (InputStream inputStream = connection.getInputStream();
             Scanner scanner = new Scanner(inputStream)) {
            StringBuilder responseBody = new StringBuilder();
            scanner.forEachRemaining(responseBody::append);

            return parseWeatherFromJson(responseBody.toString());
        } finally {
            connection.disconnect();
        }
    }

    private static List<Weather> parseWeatherFromJson(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray list = jsonObject.getAsJsonArray("list");
        List<Weather> weathers = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            JsonObject item = list.get(i).getAsJsonObject();
            if (isNoonData(item)) {
                Instant instant = parseInstant(item.get("dt_txt").getAsString());
                Weather weather = createWeatherObject(item, instant);
                weathers.add(weather);
            }
        }
        return weathers;
    }

    private static boolean isNoonData(JsonObject item) {
        LocalTime time = parseInstant(item.get("dt_txt").getAsString()).atZone(ZoneId.of("UTC")).toLocalTime();
        return time.equals(LocalTime.of(12, 0));
    }

    private static Instant parseInstant(String dtTxt) {
        String formattedDtTxt = dtTxt.substring(0, 10) + 'T' + dtTxt.substring(10) + "Z";
        return Instant.parse(formattedDtTxt).truncatedTo(ChronoUnit.SECONDS);
    }


    private static Weather createWeatherObject(JsonObject item, Instant instant) {
        JsonObject main = item.getAsJsonObject("main");
        double temperature = main.get("temp").getAsDouble();
        int humidity = main.get("humidity").getAsInt();
        JsonObject wind = item.getAsJsonObject("wind");
        double windSpeed = wind.get("speed").getAsDouble();
        JsonObject clouds = item.getAsJsonObject("clouds");
        int allClouds = clouds.get("all").getAsInt();
        double pop = item.get("pop").getAsDouble();

        return new Weather();
    }
}
