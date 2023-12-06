package org.ulpgc.dacd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class JsonStore {
    private static String topicName = "prediction.Weather";

    public void storeEvent(String event) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(event);
        String tsValue = jsonNode.get("ts").asText();
        String ssValue = jsonNode.get("ss").asText();
        String formattedDate = formatInstantDate(tsValue);
        String directoryPath = "event_store/" + topicName + "/" + ssValue + "/";
        String filePath = directoryPath + formattedDate + ".events";

        try {
            new java.io.File(directoryPath).mkdirs();
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(event.replaceAll("\\s", ""));
                writer.write(System.lineSeparator());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String formatInstantDate(String instant) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime dateTime = LocalDateTime.parse(instant, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dateTime.format(outputFormatter);
    }
}
