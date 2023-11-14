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
import java.net.URL;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenWeatherMapSupplier {
    private List<Weather> weathers;

    public static List<Weather> getWeather(Location location, Instant instant) throws IOException {
        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";
        String apiKeyParam = "appid=" + "3f7e30bbced203f4b907d03ba08d8ac6";
        String latParam = "lat=" + location.getLat();
        String lonParam = "lon=" + location.getLon();
        String urlStr = apiUrl + "?" + latParam + "&" + lonParam + "&" + apiKeyParam;

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();

        Scanner scanner = new Scanner(inputStream);
        StringBuilder responseBody = new StringBuilder();
        while (scanner.hasNextLine()) {
            responseBody.append(scanner.nextLine());
        }
        scanner.close();

        List<Weather> weathers2 = parseWeatherFromJson(responseBody.toString());

        connection.disconnect();

        return weathers2;
    }

    private static List<Weather> parseWeatherFromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray list = jsonObject.getAsJsonArray("list");

        List<Weather> weathers = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            JsonObject item = list.get(i).getAsJsonObject();
            String dtTxt = item.get("dt_txt").getAsString();
            dtTxt = dtTxt.substring(0, 10) + "T" + dtTxt.substring(11) + "Z";
            Instant instant = Instant.parse(dtTxt);
            instant = instant.truncatedTo(ChronoUnit.SECONDS);

            if (instant.atZone(ZoneId.of("UTC")).toLocalTime().equals(LocalTime.of(12, 0))) {
                JsonObject main = item.getAsJsonObject("main");
                double temperature = main.get("temp").getAsDouble();
                int humidity = main.get("humidity").getAsInt();
                JsonObject wind = item.getAsJsonObject("wind");
                double windSpeed = wind.get("speed").getAsDouble();
                JsonObject clouds = item.getAsJsonObject("clouds");
                int allClouds = clouds.get("all").getAsInt();
                double pop = item.get("pop").getAsDouble(); // No se necesita un objeto JSON para 'pop'

                Weather weather = new Weather();
                weather.setTs(Instant.parse(dtTxt));
                weather.setTemp(temperature);
                weather.setHumidity(humidity);
                weather.setWindSpeed(windSpeed);
                weather.setClouds(allClouds);
                weather.setRain(pop);
                weathers.add(weather);
            }
        }

        return weathers;
    }

}
