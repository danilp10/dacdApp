package org.ulpgc.dacd.control;

import com.google.gson.*;
import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.HotelBasicInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenHotelMapSupplier implements HotelSupplier{

    public List<Hotel> getHotel(String filename, Instant chkIn, Instant chkOut) throws IOException {
        List<Hotel> hotels = new ArrayList<>();
        List<HotelBasicInfo> hotelsBasicInfo = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
            JsonArray jsonArray = new Gson().fromJson(jsonContent.toString(), JsonArray.class);
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String hotelName = jsonObject.get("Hotel_name").getAsString();
                String hotelKey = jsonObject.get("key").getAsString();
                String location = jsonObject.get("Location").getAsString();

                hotelsBasicInfo.add(new HotelBasicInfo(hotelName, hotelKey, location));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (HotelBasicInfo info : hotelsBasicInfo) {
            String hotelName = info.getHotelName();
            String hotelKey = info.getHotelKey();
            String location = info.getLocation();

            hotels.addAll(fetchHotelData(hotelKey, chkIn, chkOut, hotelName, location));
        }

        return hotels;
    }


    private List<Hotel> fetchHotelData(String hotelKey, Instant chkIn, Instant chkOut, String hotelName, String location) throws IOException {
        String apiUrl = "https://data.xotelo.com/api/rates";
        String urlStr = apiUrl + "?hotel_key=" + hotelKey + "&chk_in=" + chkIn + "&chk_out=" + chkOut;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (InputStream inputStream = connection.getInputStream();
             Scanner scanner = new Scanner(inputStream)) {
            StringBuilder responseBody = new StringBuilder();
            scanner.forEachRemaining(responseBody::append);
            List<Hotel> hotelList = parseHotelFromJson(responseBody.toString(), hotelName, hotelKey, location, chkIn, chkOut);
            return hotelList;
        } finally {
            connection.disconnect();
        }
    }

    private static List<Hotel> parseHotelFromJson(String json, String hotelName, String hotelKey, String location, Instant chkIn, Instant chkOut) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapt()).create();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        List<Hotel> hotels = new ArrayList<>();
        if (jsonObject.has("result") && jsonObject.get("result").isJsonObject()) {
            JsonObject resultObject = jsonObject.getAsJsonObject("result");
            if (resultObject.has("rates") && resultObject.get("rates").isJsonArray()) {
                JsonArray ratesArray = resultObject.getAsJsonArray("rates");
                for (int i = 0; i < ratesArray.size(); i++) {
                    JsonObject item = ratesArray.get(i).getAsJsonObject();
                    Hotel hotel = createHotelObject(item, hotelName, hotelKey, location, chkIn, chkOut);
                    hotels.add(hotel);
                }
            }
        }
        return hotels;
    }


    private static Hotel createHotelObject(JsonObject item, String hotelName, String hotelKey, String location, Instant chkIn, Instant chkOut) {
        String code = item.get("code").getAsString();
        double rate = item.get("rate").getAsDouble();
        double tax = item.get("tax").getAsDouble();
        String rateName = item.get("name").getAsString();

        return new Hotel(Instant.now().truncatedTo(ChronoUnit.SECONDS), hotelName, location, hotelKey, code, rateName, rate, tax, chkIn, chkOut, "rate.Hotel");
    }
}
