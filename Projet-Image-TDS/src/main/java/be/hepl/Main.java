package be.hepl;

import be.hepl.IsilImageProcessing.Contours.ContoursLineaire;
import be.hepl.application.Application;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Application().setVisible(true);
            }
        });

        //test unitaire
    }
}

