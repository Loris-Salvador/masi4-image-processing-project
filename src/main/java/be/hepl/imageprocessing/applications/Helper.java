package be.hepl.imageprocessing.applications;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Helper {

    public static int [][][] getRGB(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        int[][][] rgb = new int[3][height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                rgb[0][y][x] = r;
                rgb[1][y][x] = g;
                rgb[2][y][x] = b;
            }
        }

        return rgb;


    }

    public static BufferedImage recombineRGB(int[][] r, int[][] g, int[][] b) {
        int height = r.length;
        int width = r[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = r[y][x];
                int green = g[y][x];
                int blue = b[y][x];

                // S'assurer que les valeurs restent entre 0 et 255
                red = Math.min(255, Math.max(0, red));
                green = Math.min(255, Math.max(0, green));
                blue = Math.min(255, Math.max(0, blue));

                int rgb = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }

    public static BufferedImage matriceBinaireVersImage(int[][] matrice) {
        int height = matrice.length;
        int width = matrice[0].length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = matrice[y][x];
                if (value == 1)
                    image.setRGB(x, y, Color.WHITE.getRGB()); // blanc
                else
                    image.setRGB(x, y, Color.BLACK.getRGB()); // noir
            }
        }

        return image;
    }


    public static int[][] getLuminance(BufferedImage image) {

        int [][][] imageRGB = Helper.getRGB(image);

        int[][] red = imageRGB[0];
        int[][] green = imageRGB[1];
        int[][] blue = imageRGB[2];

        int width = image.getWidth();
        int height = image.getHeight();

        int[][] luminance = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                luminance[y][x] = (int)(0.299 * red[y][x] + 0.587 * green[y][x] + 0.114 * blue[y][x]);
            }
        }

        return luminance;
    }

}
