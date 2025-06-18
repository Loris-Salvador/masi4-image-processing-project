package be.hepl.imageprocessing.filtragelineaire.global;

import be.hepl.imageprocessing.filtragelineaire.complex.Complex;


public class LowPassFilter {

    public static void filterLowPass(Complex[][] freq, int rayon) {
        int hauteur = freq.length;
        int largeur = freq[0].length;
        int centreY = hauteur / 2;
        int centreX = largeur / 2;

        // Application du filtre passe-bas idéal
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                // Calcul de la distance au centre
                double dist = Math.sqrt(Math.pow(x - centreX, 2) + Math.pow(y - centreY, 2));

                // Si la distance est supérieure au rayon, on met le coefficient à zéro
                if (dist > rayon) {
                    freq[y][x] = new Complex(0, 0);
                }
            }
        }
    }
}