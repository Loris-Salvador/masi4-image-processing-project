package be.hepl.imageprocessing.nonlineaire;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MorphoComplexe
{
    public static int[][] dilatationGeodesique(int[][] image, int[][] masqueGeodesique, int nbIter) {
        int[][] marqueur = copy(image);

        if (nbIter < 1)
        {
            throw new IllegalArgumentException("Le nombre d'itérations doit être >= 1.");
        }

        for (int i = 0; i < nbIter; i++) {
            marqueur = dilateOnce(marqueur);
            marqueur = intersection(marqueur, masqueGeodesique);
        }
        return marqueur;
    }

    /*public static int[][] reconstructionGeodesique(int[][] image, int[][] masqueGeodesique) {
        int[][] marqueur = copy(image);
        int[][] precedent;

        do {
            precedent = copy(marqueur);
            marqueur = dilateOnce(marqueur);
            marqueur = intersection(marqueur, masqueGeodesique);
        } while (!Arrays.deepEquals(precedent, marqueur));

        return marqueur;
    }*/

    public static int[][] reconstructionGeodesique(int[][] image, int[][] masqueGeodesique) {
        // Validation des entrées
        if (image.length != masqueGeodesique.length || image[0].length != masqueGeodesique[0].length)
        {
            throw new IllegalArgumentException("Image et masque doivent avoir la même taille.");
        }

        int[][] marqueur = copy(image);
        int[][] precedent;
        int iter = 0;
        // Sécurité anti-boucle infinie
        int maxIter = 1000;

        do
        {
            precedent = copy(marqueur);
            marqueur = intersection(dilateOnce(marqueur), masqueGeodesique);
            iter++;
            //System.out.println(STR."Iteration \{iter}: \{Arrays.deepToString(marqueur)}");
            System.out.println("iteration :" + iter);
        } while (!Arrays.deepEquals(precedent, marqueur) && iter < maxIter);

        return marqueur;
    }

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

    public static BufferedImage filtreMedianCouleur(BufferedImage image, int tailleMasque) {
        int width = image.getWidth();
        int height = image.getHeight();
        int offset = tailleMasque / 2;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int size = tailleMasque * tailleMasque;

        int[] rWindow = new int[size];
        int[] gWindow = new int[size];
        int[] bWindow = new int[size];

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int idx = 0;
                for (int dy = -offset; dy <= offset; dy++)
                {
                    for (int dx = -offset; dx <= offset; dx++)
                    {
                        int yy = clamp(y + dy, 0, height - 1);
                        int xx = clamp(x + dx, 0, width - 1);
                        int rgb = image.getRGB(xx, yy);
                        rWindow[idx] = (rgb >> 16) & 0xFF;
                        gWindow[idx] = (rgb >> 8) & 0xFF;
                        bWindow[idx] = rgb & 0xFF;
                        idx++;
                    }
                }
                Arrays.sort(rWindow);
                Arrays.sort(gWindow);
                Arrays.sort(bWindow);
                int r = rWindow[size / 2];
                int g = gWindow[size / 2];
                int b = bWindow[size / 2];
                result.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return result;
    }


    // Helper: single dilation (max in 3x3)
    private static int[][] dilateOnce(int[][] img)
    {
        return MorphoElementaire.dilatation(img, 3);
    }

    private static int[][] intersection(int[][] a, int[][] b)
    {
        int[][] res = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++)
        {
            for (int j = 0; j < a[0].length; j++)
            {
                res[i][j] = Math.min(a[i][j], b[i][j]);
            }
        }
        return res;
    }

    private static int[][] copy(int[][] matrix)
    {
        return Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
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
