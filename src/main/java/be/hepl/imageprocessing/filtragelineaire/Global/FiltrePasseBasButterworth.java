package be.hepl.imageprocessing.filtragelineaire.Global;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;

import static be.hepl.imageprocessing.filtragelineaire.DFT.Fourier.itf2d;
import static be.hepl.imageprocessing.filtragelineaire.DFT.Fourier.tf2d;

public class FiltrePasseBasButterworth {

    public static int[][] applyButterworthLowPass(int[][] image, int frequenceCoupure, int ordre) {
        int hauteur = image.length;
        int largeur = image[0].length;

        double[][] imageCentered = new double[hauteur][largeur];
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                imageCentered[y][x] = image[y][x] * Math.pow(-1, x + y);
            }
        }

        Complex[][] freq = tf2d(imageCentered);
        filterButterworthLowPass(freq, frequenceCoupure, ordre);
        double[][] imageFiltered = itf2d(freq);

        int[][] resultat = new int[hauteur][largeur];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                imageFiltered[y][x] *= Math.pow(-1, x + y);
                min = Math.min(min, imageFiltered[y][x]);
                max = Math.max(max, imageFiltered[y][x]);
            }
        }

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                resultat[y][x] = (int) (255 * (imageFiltered[y][x] - min) / (max - min));
            }
        }

        return resultat;
    }

    public static void filterButterworthLowPass(Complex[][] freq, int D0, int n) {
        int hauteur = freq.length;
        int largeur = freq[0].length;
        int centreY = hauteur / 2;
        int centreX = largeur / 2;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int du = y - centreY;
                int dv = x - centreX;
                double D2 = du * du + dv * dv;
                if (D2 == 0) D2 = 1;

                double ratio = D2 / (double)(D0 * D0);
                double H = 1.0 / (1.0 + Math.pow(ratio, n * 2));

                double real = freq[y][x].getReal() * H;
                double imag = freq[y][x].getImaginary() * H;
                freq[y][x] = new Complex(real, imag);
            }
        }
    }
}
