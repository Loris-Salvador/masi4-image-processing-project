package be.hepl.imageprocessing.filtragelineaire.DFT;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;

import static be.hepl.imageprocessing.filtragelineaire.DFT.TF1DCalculator.TF1D;


public class TF2DCalculator {
    public static Complex[][] TF2D(double[][] image) {
        int hauteur = image.length;
        int largeur = image[0].length; //ici
        Complex[][] tf2d = new Complex[hauteur][largeur];

        for (int y = 0; y < hauteur; y++) {
            tf2d[y] = TF1D(image[y]);
        }

        for (int x = 0; x < largeur; x++) {
            double[] colonne = new double[hauteur];
            for (int y = 0; y < hauteur; y++) {
                colonne[y] = tf2d[y][x].getReal();
            }
            Complex[] dftColonne = TF1D(colonne);

            for (int y = 0; y < hauteur; y++) {
                tf2d[y][x] = dftColonne[y];
            }
        }

        return tf2d;
    }

    public static void main(String[] args) {
        double[][] image = {
                {1.0, 2.0, 3.0, 4.0},
                {5.0, 6.0, 7.0, 8.0},
                {9.0, 10.0, 11.0, 12.0},
                {13.0, 14.0, 15.0, 16.0}
        };

        Complex[][] tf2dResult = TF2D(image);

        for (int y = 0; y < tf2dResult.length; y++) {
            for (int x = 0; x < tf2dResult[0].length; x++) {
                System.out.printf("TF2D[%d][%d] = %s\n", y, x, tf2dResult[y][x]);
            }
        }
    }
}
