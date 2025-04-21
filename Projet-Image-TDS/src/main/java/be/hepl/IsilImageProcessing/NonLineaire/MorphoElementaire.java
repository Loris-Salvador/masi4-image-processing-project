package be.hepl.IsilImageProcessing.NonLineaire;

public class MorphoElementaire
{
    // Erosion: minimum filter over nxn neighborhood
    public static int[][] erosion(int[][] image, int tailleMasque)
    {
        int height = image.length;
        int width = image[0].length;
        int offset = tailleMasque / 2;
        int[][] result = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int min = Integer.MAX_VALUE;
                for (int dy = -offset; dy <= offset; dy++) {
                    for (int dx = -offset; dx <= offset; dx++) {
                        int yy = y + dy;
                        int xx = x + dx;
                        if (yy >= 0 && yy < height && xx >= 0 && xx < width) {
                            min = Math.min(min, image[yy][xx]);
                        }
                    }
                }
                result[y][x] = (min == Integer.MAX_VALUE ? image[y][x] : min);
            }
        }
        return result;
    }

    // Dilation: maximum filter over nxn neighborhood
    public static int[][] dilatation(int[][] image, int tailleMasque)
    {
        int height = image.length;
        int width = image[0].length;
        int offset = tailleMasque / 2;
        int[][] result = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int max = Integer.MIN_VALUE;
                for (int dy = -offset; dy <= offset; dy++) {
                    for (int dx = -offset; dx <= offset; dx++) {
                        int yy = y + dy;
                        int xx = x + dx;
                        if (yy >= 0 && yy < height && xx >= 0 && xx < width) {
                            max = Math.max(max, image[yy][xx]);
                        }
                    }
                }
                result[y][x] = (max == Integer.MIN_VALUE ? image[y][x] : max);
            }
        }
        return result;
    }

    // Opening: erosion followed by dilation
    public static int[][] ouverture(int[][] image, int tailleMasque)
    {
        int[][] eroded = erosion(image, tailleMasque);
        return dilatation(eroded, tailleMasque);
    }

    // Closing: dilation followed by erosion
    public static int[][] fermeture(int[][] image, int tailleMasque)
    {
        int[][] dilated = dilatation(image, tailleMasque);
        return erosion(dilated, tailleMasque);
    }
}
