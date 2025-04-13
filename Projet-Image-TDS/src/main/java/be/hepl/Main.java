package be.hepl;

import be.hepl.application.Application;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Exécution dans le thread EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Application().setVisible(true);
            }
        });
    }
}

