package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.ulpgc.dacd.Main;
import org.ulpgc.dacd.model.HotelBasicInfo;

import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelFileReader {

    private static final String FILE = "hotel.json";

    public List<HotelBasicInfo> readHotels() throws IOException {
        List<HotelBasicInfo> hotels = new ArrayList<>();
        URL resource = Main.class.getClassLoader().getResource(FILE);


        if (resource != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }

                HotelBasicInfo[] hotelArray = prepareGson().fromJson(jsonContent.toString(), HotelBasicInfo[].class);
                hotels = Arrays.asList(hotelArray);
            }
        }


        return hotels;
    }


    private Gson prepareGson() {
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
            @Override
            public void write(com.google.gson.stream.JsonWriter out, Instant value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public Instant read(com.google.gson.stream.JsonReader in) throws IOException {
                return Instant.parse(in.nextString());
            }
        }).create();
    }
}
