package be.hepl.imageprocessing.applications;

import java.awt.image.BufferedImage;

public class SeuillageCouleur {
    public static int[][] seuillageRouge(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] binaire = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // SeuillageCouleur rouge
                if (r > 150 && g < 100 && b < 100)
                    binaire[y][x] = 1;
                else
                    binaire[y][x] = 0;
            }
        }

        return binaire;
    }

    public static int[][] seuillageBleu(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] binaire = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                if (b > 150 && r < 100 && g < 100)
                    binaire[y][x] = 1;
                else
                    binaire[y][x] = 0;
            }
        }

        return binaire;
    }
}
