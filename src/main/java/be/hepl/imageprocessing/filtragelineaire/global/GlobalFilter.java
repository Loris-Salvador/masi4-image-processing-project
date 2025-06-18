package be.hepl.imageprocessing.filtragelineaire.global;

import be.hepl.imageprocessing.filtragelineaire.complex.Complex;
import be.hepl.imageprocessing.filtragelineaire.dft.Fourier;

import static be.hepl.imageprocessing.filtragelineaire.global.LowPassButterworthFilter.filterButterworthLowPass;
import static be.hepl.imageprocessing.filtragelineaire.global.HighPassButterworthFilter.filterButterworthHighPass;
import static be.hepl.imageprocessing.filtragelineaire.global.HighPassFilter.filterHighPass;
import static be.hepl.imageprocessing.filtragelineaire.global.LowPassFilter.filterLowPass;

public class GlobalFilter {

    public static int[][] applyIdealLowPassFilter(int[][] image, int tailleMasque) {
        return apply(image, tailleMasque, 0, 0);
    }

    public static int[][] applyIdealHighPassFilter(int[][] image, int tailleMasque) {
        return apply(image, tailleMasque, 0, 1);
    }

    public static int[][] applyButterworthLowPassFilter(int[][] image, int frequenceCoupure, int ordre) {
        return apply(image, frequenceCoupure, ordre, 2);
    }

    public static int[][] applyButterworthHighPassFilter(int[][] image, int frequenceCoupure, int ordre) {
        return apply(image, frequenceCoupure, ordre, 3);
    }



    public static int[][] apply(int[][] image, int frequenceCoupure, int ordre, int type) {
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
        Complex[][] freq = Fourier.tf2d(imageCentered);

        int tailleMasque = frequenceCoupure;

        switch (type) {
            case 0: // Filtre passe-bas idéal
                filterLowPass(freq, tailleMasque);
                break;
            case 1: // Filtre passe-haut idéal
                filterHighPass(freq, tailleMasque);
                break;
            case 2: // Filtre passe-bas Butterworth
                System.out.println("Applying Butterworth Low Pass Filter with cutoff frequency: " + frequenceCoupure + " and order: " + ordre);
                filterButterworthLowPass(freq, frequenceCoupure, ordre);
                break;
            case 3: // Filtre passe-haut Butterworth
                System.out.println("Applying Butterworth High Pass Filter with cutoff frequency: " + frequenceCoupure + " and order: " + ordre);
                filterButterworthHighPass(freq, frequenceCoupure, ordre);
                break;
        }

        // Transformée de Fourier inverse 2D
        double[][] imageFiltered = Fourier.itf2d(freq);

        int[][] resultat = new int[hauteur][largeur];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        // Réappliquer (-1)^{x+y} et normalisation
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
}
