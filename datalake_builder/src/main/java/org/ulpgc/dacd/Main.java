package org.ulpgc.dacd;

public class Main {
    public static void main(String[] args) {
        String rootDirectory = args[0];
        EventStoreBuilder eventStoreBuilder = new EventStoreBuilder(rootDirectory);
        eventStoreBuilder.start();
    }
}
