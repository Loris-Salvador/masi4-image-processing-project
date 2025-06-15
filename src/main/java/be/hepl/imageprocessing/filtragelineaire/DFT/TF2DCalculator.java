package be.hepl.imageprocessing.filtragelineaire.DFT;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;

import static be.hepl.imageprocessing.filtragelineaire.DFT.TF1DCalculator.ITF1D;
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

    public static int[][] ITF2D(Complex[][] freqDomain) {
        int hauteur = freqDomain.length;
        int largeur = freqDomain[0].length;
        Complex[][] temp = new Complex[hauteur][largeur];

        // Inverse TF sur les colonnes
        for (int x = 0; x < largeur; x++) {
            Complex[] colonne = new Complex[hauteur];
            for (int y = 0; y < hauteur; y++) {
                colonne[y] = freqDomain[y][x];
            }
            Complex[] itfColonne = ITF1D(colonne);
            for (int y = 0; y < hauteur; y++) {
                temp[y][x] = itfColonne[y];
            }
        }

        // Inverse TF sur les lignes
        int[][] imageRecons = new int[hauteur][largeur];
        for (int y = 0; y < hauteur; y++) {
            Complex[] ligne = ITF1D(temp[y]);
            for (int x = 0; x < largeur; x++) {
                imageRecons[y][x] = (int) ligne[x].getReal(); // On récupère la partie réelle
            }
        }

        return imageRecons;
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
