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

    public static int[][] convertirEnNiveauDeGris(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] gris = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                // Calcul de la luminance avec pondération classique
                gris[y][x] = (int)(0.3 * r + 0.59 * g + 0.11 * b);
            }
        }
        return gris;
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

    public static int[][] appliquerMasque(int[][] masqueBinaire, int[][] imageGris) {
        int height = imageGris.length;
        int width = imageGris[0].length;
        int[][] resultat = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (masqueBinaire[y][x] != 0) {
                    resultat[y][x] = imageGris[y][x]; // garde la valeur réelle
                } else {
                    resultat[y][x] = 0; // fond noir
                }
            }
        }
        return resultat;
    }

    public static int[][] soustractionBinaire(int[][] a, int[][] b) {
        int h = a.length;
        int w = a[0].length;
        int[][] res = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (a[y][x] == 255 && b[y][x] == 0) {
                    res[y][x] = 255;
                } else {
                    res[y][x] = 0;
                }
            }
        }
        return res;
    }

    public static int[][] amplitudeGradient(int[][] gradX, int[][] gradY) {
        int h = gradX.length;
        int w = gradX[0].length;
        int[][] amplitude = new int[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int gx = gradX[y][x];
                int gy = gradY[y][x];

                // Calcule la norme euclidienne
                int val = (int) Math.min(255, Math.sqrt(gx * gx + gy * gy));
                amplitude[y][x] = val;
            }
        }
        return amplitude;
    }

    public static int[][] inverser(int[][] image) {
        int h = image.length;
        int w = image[0].length;
        int[][] res = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                res[y][x] = (image[y][x] == 255) ? 0 : 255;
            }
        }
        return res;
    }

    public static BufferedImage appliquerMasqueCouleur(BufferedImage image, int[][] masqueBinaire) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Vérification corrigée : largeur == masqueBinaire.length, hauteur == masqueBinaire[0].length
        if (width != masqueBinaire.length || height != masqueBinaire[0].length) {
            throw new IllegalArgumentException("L'image et le masque doivent avoir la même taille");
        }

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Boucle inversée pour respecter [x][y]
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (masqueBinaire[x][y] == 255) {
                    int rgb = image.getRGB(x, y);
                    result.setRGB(x, y, rgb);
                } else {
                    result.setRGB(x, y, 0x00000000); // transparent
                }
            }
        }

        return result;
    }




}
