package org.ulpgc.dacd;

public class Main {
    public static void main(String[] args) {
        EventStoreBuilder eventStoreBuilder = new EventStoreBuilder();
        eventStoreBuilder.start();
    }
}