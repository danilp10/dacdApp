package org.ulpgc.dacd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonStore {
    private String topicName;
    private String rootDirectory;

    public JsonStore(String topicName, String rootDirectory) {
        this.topicName = topicName;
        this.rootDirectory = rootDirectory;
    }

    public void storeEvent(String event) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(event);
        String tsValue = jsonNode.get("ts").asText();
        String ssValue = jsonNode.get("ss").asText();
        String formattedDate = formatInstantDate(tsValue);

        String directoryPath = Paths.get(rootDirectory, "datalake", topicName, ssValue).toString();
        String filePath = Paths.get(directoryPath, formattedDate + ".events").toString();

        try {
            java.nio.file.Files.createDirectories(Paths.get(directoryPath));
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
