package be.hepl.imageprocessing.filtragelineaire.Global;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;

import static be.hepl.imageprocessing.filtragelineaire.DFT.Fourier.itf2d;
import static be.hepl.imageprocessing.filtragelineaire.DFT.Fourier.tf2d;

public class FourierHighPassFilter {

    public static int[][] apply(int[][] image, int rayon) {
        int hauteur = image.length;
        int largeur = image[0].length;

        // Centrage de l'image avec (-1)^{x+y} et conversion en double[][]
        double[][] imageCentered = new double[hauteur][largeur];
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                imageCentered[y][x] = image[y][x] * Math.pow(-1, x + y);
            }
        }

        // Transformée de Fourier 2D
        Complex[][] freq = tf2d(imageCentered);

        // Application du filtre passe-haut
        filterHighPass(freq, rayon);

        // Transformée de Fourier inverse 2D
        double[][] imageFiltered = itf2d(freq);

        int[][] resultat = new int[hauteur][largeur];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        // Réappliquer (-1)^{x+y}
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                imageFiltered[y][x] *= Math.pow(-1, x + y);
                min = Math.min(min, imageFiltered[y][x]);
                max = Math.max(max, imageFiltered[y][x]);
            }
        }


        // Mise à l'échelle entre 0 et 255

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                resultat[y][x] = (int) (255 * (imageFiltered[y][x] - min) / (max - min));
            }
        }

        return resultat;
    }


    public static void filterHighPass(Complex[][] freq, int rayon) {
        int hauteur = freq.length;
        int largeur = freq[0].length;
        int centreY = hauteur / 2;
        int centreX = largeur / 2;

        // Application du filtre passe-haut idéal
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {

                // Calcul de la distance du pixel au centre
                double dist = Math.sqrt(Math.pow(x - centreX, 2) + Math.pow(y - centreY, 2));
                if (dist <= rayon) {
                    freq[y][x] = new Complex(0, 0);
                }
            }
        }
    }
}
