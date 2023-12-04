package org.ulpgc.dacd;

import java.io.IOException;
import java.util.ArrayList;

public class EventController {

    public void execute(){
        EventStoreBuilder eventStoreBuilder = new EventStoreBuilder();
        JsonStore jsonStore = new JsonStore();

        ArrayList<String> weatherList = eventStoreBuilder.start();
        try {
            jsonStore.storeEvent(weatherList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
