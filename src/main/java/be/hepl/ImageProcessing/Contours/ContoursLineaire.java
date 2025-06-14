package be.hepl.ImageProcessing.Contours;

public class ContoursLineaire {
    public static int[][] gradientPrewitt(int[][] image, int dir) {
        int[][] kernel;

        if (dir == 1) {
            // Prewitt horizontal
            kernel = new int[][] {
                    {-1, 0, 1},
                    {-1, 0, 1},
                    {-1, 0, 1}
            };
        } else if (dir == 2) {
            // Prewitt vertical
            kernel = new int[][] {
                    {1, 1, 1},
                    {0, 0, 0},
                    {-1, -1, -1}
            };
        } else {
            throw new IllegalArgumentException("La direction doit être 1 (horizontal) ou 2 (vertical)");
        }

        int rows = image.length;
        int cols = image[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                int sum = 0;
                for (int ki = -1; ki <= 1; ki++) {
                    for (int kj = -1; kj <= 1; kj++) {
                        sum += image[i + ki][j + kj] * kernel[ki + 1][kj + 1];
                    }
                }
                result[i][j] = Math.abs(sum);
            }
        }

        return result;
    }


    public static int[][] gradientSobel(int[][] image, int dir) {
        int[][] kernel;

        if (dir == 1) {
            // Sobel horizontal
            kernel = new int[][] {
                    {-1, 0, 1},
                    {-2, 0, 2},
                    {-1, 0, 1}
            };
        } else if (dir == 2) {
            // Sobel vertical
            kernel = new int[][] {
                    {1, 2, 1},
                    {0, 0, 0},
                    {-1, -2, -1}
            };
        } else {
            throw new IllegalArgumentException("La direction doit être 1 (horizontal) ou 2 (vertical)");
        }

        int rows = image.length;
        int cols = image[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                int sum = 0;
                for (int ki = -1; ki <= 1; ki++) {
                    for (int kj = -1; kj <= 1; kj++) {
                        sum += image[i + ki][j + kj] * kernel[ki + 1][kj + 1];
                    }
                }
                result[i][j] = Math.abs(sum);
            }
        }

        return result;
    }


    public static int[][] laplacien4(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int value = image[y - 1][x]     // haut
                        + image[y + 1][x]     // bas
                        + image[y][x - 1]     // gauche
                        + image[y][x + 1]     // droite
                        - 4 * image[y][x];    // centre * -4
                result[y][x] = Math.min(Math.max(value, 0), 255); // clamp
            }
        }

        return result;
    }


    public static int[][] laplacien8(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int value =
                        image[y - 1][x - 1] + image[y - 1][x] + image[y - 1][x + 1] + // ligne du haut
                                image[y][x - 1]     - 8 * image[y][x] + image[y][x + 1]     + // ligne du milieu
                                image[y + 1][x - 1] + image[y + 1][x] + image[y + 1][x + 1];  // ligne du bas

                result[y][x] = Math.min(Math.max(value, 0), 255); // clamp
            }
        }

        return result;
    }

}