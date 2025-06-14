package be.hepl.IsilImageProcessing.Contours;

import be.hepl.IsilImageProcessing.NonLineaire.MorphoElementaire;

public class ContoursNonLineaire {
    public static int[][] gradientErosion(int[][] image) {
        int height = image.length;
        int width = image[0].length;

        // Erosion avec un élément structurant 3x3
        int[][] eroded = MorphoElementaire.erosion(image, 3);
        int[][] result = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = image[y][x] - eroded[y][x];
                result[y][x] = Math.min(Math.max(value, 0), 255); // Clamp entre 0 et 255
            }
        }

        return result;
    }

    public static int[][] gradientDilatation(int[][] image) {
        int height = image.length;
        int width = image[0].length;

        // Dilatation avec un élément structurant 3x3
        int[][] dilated = MorphoElementaire.dilatation(image, 3);
        int[][] result = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = dilated[y][x] - image[y][x];
                result[y][x] = Math.min(Math.max(value, 0), 255); // Clamp
            }
        }

        return result;
    }

    public static int[][] gradientBeucher(int[][] image) {
        int height = image.length;
        int width = image[0].length;

        int[][] dilated = MorphoElementaire.dilatation(image, 3);
        int[][] eroded = MorphoElementaire.erosion(image, 3);

        int[][] result = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = dilated[y][x] - eroded[y][x];
                result[y][x] = Math.min(Math.max(value, 0), 255); // clamp
            }
        }

        return result;
    }

    public static int[][] laplacienNonLineaire(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int min = 255;
                int max = 0;

                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int val = image[y + dy][x + dx];
                        if (val < min) min = val;
                        if (val > max) max = val;
                    }
                }

                int value = max + min - 2 * image[y][x];
                result[y][x] = Math.min(Math.max(value, 0), 255); // clamp
            }
        }

        return result;
    }

}
