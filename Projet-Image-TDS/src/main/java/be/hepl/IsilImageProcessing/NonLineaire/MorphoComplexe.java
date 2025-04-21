package be.hepl.IsilImageProcessing.NonLineaire;

import java.util.Arrays;

public class MorphoComplexe
{
    // Geodesic dilation: iterate dilation but mask by geodesic
    public static int[][] dilatationGeodesique(int[][] image, int[][] geodesicMask, int nbIter)
    {
        int[][] marker = copy(image);
        for (int i = 0; i < nbIter; i++)
        {
            marker = dilateOnce(marker);
            marker = pointwiseMin(marker, geodesicMask);
        }
        return marker;
    }

    // Reconstruction geodesique: iterate until stability
    public static int[][] reconstructionGeodesique(int[][] image, int[][] geodesicMask)
    {
        int[][] marker = copy(image);
        while (true)
        {
            int[][] prev = marker;
            marker = dilateOnce(marker);
            marker = pointwiseMin(marker, geodesicMask);
            if (Arrays.deepEquals(prev, marker)) break;
        }
        return marker;
    }

    // Median filter over nxn neighborhood
    public static int[][] filtreMedian(int[][] image, int tailleMasque)
    {
        int height = image.length;
        int width = image[0].length;
        int offset = tailleMasque / 2;
        int[][] result = new int[height][width];
        int size = tailleMasque * tailleMasque;
        int[] window = new int[size];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int idx = 0;
                for (int dy = -offset; dy <= offset; dy++)
                {
                    for (int dx = -offset; dx <= offset; dx++) {
                        int yy = clamp(y + dy, 0, height - 1);
                        int xx = clamp(x + dx, 0, width - 1);
                        window[idx++] = image[yy][xx];
                    }
                }
                Arrays.sort(window);
                result[y][x] = window[size / 2];
            }
        }
        return result;
    }

    // Helper: single dilation (max in 3x3)
    private static int[][] dilateOnce(int[][] img)
    {
        return MorphoElementaire.dilatation(img, 3);
    }

    private static int[][] copy(int[][] src)
    {
        int h = src.length, w = src[0].length;
        int[][] dst = new int[h][w];
        for (int i = 0; i < h; i++) System.arraycopy(src[i], 0, dst[i], 0, w);
        return dst;
    }

    private static int[][] pointwiseMin(int[][] a, int[][] b)
    {
        int h = a.length, w = a[0].length;
        int[][] c = new int[h][w];
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                c[i][j] = Math.min(a[i][j], b[i][j]);
        return c;
    }

    private static int clamp(int v, int min, int max)
    {
        return v < min ? min : Math.min(v, max);
    }
}
