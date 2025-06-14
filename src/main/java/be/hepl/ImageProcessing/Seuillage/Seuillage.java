package be.hepl.ImageProcessing.Seuillage;

public class Seuillage {

    public static int[][] seuillageSimple(int[][] image, int seuil) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result[y][x] = (image[y][x] >= seuil) ? 255 : 0;
            }
        }

        return result;
    }

    public static int[][] seuillageDouble(int[][] image, int seuil1, int seuil2) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int val = image[y][x];
                if (val < seuil1) {
                    result[y][x] = 0;
                } else if (val <= seuil2) {
                    result[y][x] = 128;
                } else {
                    result[y][x] = 255;
                }
            }
        }

        return result;
    }

    public static int[][] seuillageAutomatique(int[][] image) {
        int height = image.length;
        int width = image[0].length;

        // 1. Initialiser le seuil à la moyenne globale
        int total = 0;
        int count = 0;
        for (int[] row : image) {
            for (int val : row) {
                total += val;
                count++;
            }
        }
        int seuil = total / count;

        boolean stable = false;

        while (!stable) {
            int sumA = 0, countA = 0;
            int sumB = 0, countB = 0;

            for (int[] row : image) {
                for (int val : row) {
                    if (val < seuil) {
                        sumA += val;
                        countA++;
                    } else {
                        sumB += val;
                        countB++;
                    }
                }
            }

            int meanA = (countA == 0) ? 0 : sumA / countA;
            int meanB = (countB == 0) ? 0 : sumB / countB;

            int newSeuil = (meanA + meanB) / 2;

            if (newSeuil == seuil) {
                stable = true;
            } else {
                seuil = newSeuil;
            }
        }

        // 2. Appliquer le seuillage simple avec le seuil trouvé
        int[][] result = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result[y][x] = (image[y][x] < seuil) ? 0 : 255;
            }
        }

        return result;
    }


}
