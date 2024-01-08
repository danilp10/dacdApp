package org.ulpgc.dacd;

import org.ulpgc.dacd.control.EventConsumer;
import org.ulpgc.dacd.view.TravelAppGUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        EventConsumer consumer = new EventConsumer();
        consumer.consumeEvents();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TravelAppGUI();
            }
        });
    }
}
