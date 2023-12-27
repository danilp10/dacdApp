package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.ulpgc.dacd.model.HotelBasicInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelFileReader {

    private String filePath;

    public HotelFileReader(String filePath) {
        this.filePath = filePath;
    }

    public List<HotelBasicInfo> readHotels() throws IOException {
        List<HotelBasicInfo> hotels = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            HotelBasicInfo[] hotelArray = prepareGson().fromJson(jsonContent.toString(), HotelBasicInfo[].class);
            hotels = Arrays.asList(hotelArray);
        }

        return hotels;
    }


    private HotelBasicInfo parseHotelBasicInfo(String line) {
        return prepareGson().fromJson(line, HotelBasicInfo.class);
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
