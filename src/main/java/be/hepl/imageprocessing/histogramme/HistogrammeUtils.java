package be.hepl.imageprocessing.histogramme;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HistogrammeUtils
{

    public static int[][] convertToMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] matrix = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                // Conversion en niveau de gris (méthode standard)
                int gray = (int)(0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
                matrix[x][y] = gray;
            }
        }
        return matrix;
    }

    public static BufferedImage convertToBufferedImage(int[][] matrix) {
        int width = matrix.length;
        int height = matrix[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gray = Math.max(0, Math.min(255, matrix[x][y]));
                Color color = new Color(gray, gray, gray);
                image.setRGB(x, y, color.getRGB());
            }
        }
        return image;
    }

    // Version pour les images couleur
    public static BufferedImage convertToColorBufferedImage(int[][][] rgbMatrix) {
        int width = rgbMatrix.length;
        int height = rgbMatrix[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int r = Math.max(0, Math.min(255, rgbMatrix[x][y][0]));
                int g = Math.max(0, Math.min(255, rgbMatrix[x][y][1]));
                int b = Math.max(0, Math.min(255, rgbMatrix[x][y][2]));
                Color color = new Color(r, g, b);
                image.setRGB(x, y, color.getRGB());
            }
        }
        return image;
    }
}
